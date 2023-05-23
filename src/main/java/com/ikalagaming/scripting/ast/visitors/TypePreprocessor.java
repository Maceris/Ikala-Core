package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.Block;
import com.ikalagaming.scripting.ast.Call;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprAssign;
import com.ikalagaming.scripting.ast.ExprTernary;
import com.ikalagaming.scripting.ast.ForLoop;
import com.ikalagaming.scripting.ast.Identifier;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.scripting.ast.VarDeclaration;
import com.ikalagaming.scripting.ast.VarDeclarationList;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Processes the types for the tree, updating nodes if we can determine what
 * they are based on their children.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class TypePreprocessor implements ASTVisitor {

	/**
	 * A stack of variable type maps. When we enter a new block-like context, we
	 * push a new map on, and when we leave we pop it back off. This allows us
	 * to maintain nested variable contexts while still using the standard
	 * visitor pattern.
	 */
	private Deque<VariableTypeMap> variableMaps = new ArrayDeque<>();

	/**
	 * Calculate the types for addition, subtraction, division, multiplication.
	 *
	 * @param node The node we are processing.
	 * @param firstType The type of the left node.
	 * @param secondType The type of the right node.
	 */
	private void calculateFourFunctionType(ExprArithmetic node,
		final Type firstType, final Type secondType) {
		if (node.getChildren().size() < 2) {
			node.setType(firstType);
			return;
		}
		// invalid types
		if (firstType.anyOf(Base.BOOLEAN, Base.IDENTIFIER, Base.VOID)
			|| secondType.anyOf(Base.BOOLEAN, Base.IDENTIFIER, Base.VOID)) {
			TypePreprocessor.log.warn(
				SafeResourceLoader.getString("INVALID_TYPES",
					ScriptManager.getResourceBundle()),
				firstType.toString(), node.getOperator().getReadable(),
				secondType.toString());
			node.setType(Type.voidType());
			return;
		}

		// Unknown
		if (firstType.anyOf(Base.UNKNOWN) || secondType.anyOf(Base.UNKNOWN)) {
			node.setType(Type.unknownType());
			return;
		}

		// Same type
		if (firstType.equals(secondType) || (firstType.anyOf(Base.DOUBLE)
			&& secondType.anyOf(Base.CHAR, Base.INT))) {
			node.setType(firstType);
			return;
		}
		// different types
		if (secondType.anyOf(Base.DOUBLE)
			&& firstType.anyOf(Base.CHAR, Base.INT)) {
			node.setType(secondType);
			return;
		}
		if (firstType.anyOf(Base.INT) && secondType.anyOf(Base.CHAR)) {
			node.setType(firstType);
			return;
		}
		if (secondType.anyOf(Base.INT) && firstType.anyOf(Base.CHAR)) {
			node.setType(secondType);
			return;
		}
		TypePreprocessor.log.warn(
			SafeResourceLoader.getString("INVALID_CAST",
				ScriptManager.getResourceBundle()),
			firstType.toString(), secondType.toString());
		node.setType(Type.voidType());
	}

	/**
	 * Calculate the types for modulus.
	 *
	 * @param node The node we are processing.
	 * @param firstType The type of the left node.
	 * @param secondType The type of the right node.
	 */
	private void calculateModulusType(ExprArithmetic node, final Type firstType,
		final Type secondType) {
		if (node.getChildren().size() < 2) {
			TypePreprocessor.log
				.warn(SafeResourceLoader.getString("MISSING_SECOND_CHILD",
					ScriptManager.getResourceBundle()), this.toString());
			node.setType(Type.voidType());
			return;
		}
		if ((firstType.anyOf(Base.INT) && secondType.anyOf(Base.INT, Base.CHAR))
			|| (firstType.anyOf(Base.DOUBLE)
				&& secondType.anyOf(Base.INT, Base.DOUBLE, Base.CHAR))
			|| (firstType.anyOf(Base.CHAR) && secondType.anyOf(Base.CHAR))) {
			node.setType(firstType);
			return;
		}
		if (firstType.anyOf(Base.INT, Base.DOUBLE, Base.CHAR)
			&& secondType.anyOf(Base.DOUBLE)) {
			node.setType(secondType);
			return;
		}
		TypePreprocessor.log.warn(
			SafeResourceLoader.getString("INVALID_CAST",
				ScriptManager.getResourceBundle()),
			firstType.toString(), secondType.toString());
		node.setType(Type.voidType());
	}

	/**
	 * Process the types for the tree. Intended for use only on the root node.
	 *
	 * @param ast The tree to process.
	 */
	public void processTreeTypes(@NonNull CompilationUnit ast) {
		VariableTypeMap variables = new VariableTypeMap();
		this.variableMaps.clear();

		this.variableMaps.push(variables);
		LabelPass labels = new LabelPass(variables);
		labels.processLabels(ast);

		this.processTypes(ast);
	}

	/**
	 * Process the types for the tree. Intended for use only on the root node.
	 *
	 * @param root The current root node.
	 */
	private void processTypes(Node root) {
		final boolean newContext =
			(root instanceof Block || root instanceof ForLoop);

		if (newContext) {
			this.variableMaps.push(this.variableMaps.peek().clone());
		}

		if (!root.getChildren().isEmpty()) {
			for (int i = 0; i < root.getChildren().size(); ++i) {
				Node child = root.getChildren().get(i);
				if (root instanceof Call call && (!call.isPrimary() && i == 0
					|| call.isPrimary() && i == 1)) {
					// Skip the method name lookup
					child.setType(Type.unknownType());
					continue;
				}

				this.processTypes(child);
			}
		}
		root.process(this);

		if (newContext) {
			this.variableMaps.pop();
		}
	}

	@Override
	public void visit(Call node) {
		node.setType(Type.unknownType());
	}

	@Override
	public void visit(ExprArithmetic node) {
		if (node.getChildren().isEmpty()) {
			TypePreprocessor.log
				.warn(SafeResourceLoader.getString("MISSING_FIRST_CHILD",
					ScriptManager.getResourceBundle()), this.toString());
			node.setType(Type.voidType());
			return;
		}
		final Type firstType = node.getChildren().get(0).getType();

		Type secondType;
		if (node.getChildren().size() > 1) {
			secondType = node.getChildren().get(1).getType();
		}
		else {
			secondType = Type.voidType();
		}

		switch (node.getOperator()) {
			case DIV, MUL:
				if (node.getChildren().size() < 2) {
					TypePreprocessor.log.warn(
						SafeResourceLoader.getString("MISSING_SECOND_CHILD",
							ScriptManager.getResourceBundle()),
						this.toString());
					node.setType(Type.voidType());
					return;
				}
				// fallthrough
			case ADD:
				if (node.getChildren().size() > 1
					&& firstType.anyOf(Base.STRING) && !secondType
						.anyOf(Base.IDENTIFIER, Base.LABEL, Base.VOID)) {
					// String concatenation
					node.setType(firstType);
					return;
				}
				if (node.getChildren().size() > 1
					&& secondType.anyOf(Base.STRING) && !firstType
						.anyOf(Base.IDENTIFIER, Base.LABEL, Base.VOID)) {
					// String concatenation
					node.setType(secondType);
					return;
				}
				// fallthrough
			case SUB:
				this.calculateFourFunctionType(node, firstType, secondType);
				break;
			case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
				if (firstType.anyOf(Base.INT, Base.CHAR, Base.DOUBLE,
					Base.UNKNOWN)) {
					node.setType(firstType);
				}
				else {
					TypePreprocessor.log.warn(
						SafeResourceLoader.getString("INVALID_OPERATOR",
							ScriptManager.getResourceBundle()),
						node.getOperator().toString(), firstType.toString());
					node.setType(Type.voidType());
				}
				break;
			case MOD:
				this.calculateModulusType(node, firstType, secondType);
				break;
			default:
				TypePreprocessor.log.warn(
					SafeResourceLoader.getString("UNKNOWN_OPERATOR",
						ScriptManager.getResourceBundle()),
					node.getOperator().toString());
				node.setType(Type.voidType());
				break;
		}
	}

	@Override
	public void visit(ExprAssign node) {
		node.setType(node.getChildren().get(0).getType());
	}

	@Override
	public void visit(ExprTernary node) {
		Type ifTrue = node.getChildren().get(1).getType();
		Type ifFalse = node.getChildren().get(2).getType();

		if (ifTrue.equals(ifFalse)) {
			node.setType(ifTrue);
			return;
		}

		// Both numeric
		if (ifTrue.anyOf(Base.CHAR, Base.INT, Base.DOUBLE, Base.UNKNOWN)
			&& ifFalse.anyOf(Base.CHAR, Base.INT, Base.DOUBLE, Base.UNKNOWN)) {
			// go with the broadest type
			if (ifTrue.anyOf(Base.DOUBLE)) {
				node.setType(ifTrue);
				return;
			}
			if (ifFalse.anyOf(Base.DOUBLE)) {
				node.setType(ifFalse);
				return;
			}
			if (ifTrue.anyOf(Base.INT)) {
				node.setType(ifTrue);
				return;
			}
			if (ifFalse.anyOf(Base.INT)) {
				node.setType(ifFalse);
				return;
			}
			if (ifTrue.anyOf(Base.CHAR)) {
				node.setType(ifTrue);
				return;
			}
			if (ifFalse.anyOf(Base.CHAR)) {
				node.setType(ifFalse);
				return;
			}
		}

		TypePreprocessor.log.warn(SafeResourceLoader
			.getString("NON_MATCHING_TYPES", ScriptManager.getResourceBundle()),
			ifTrue, ifFalse);
		node.setType(Type.voidType());
	}

	@Override
	public void visit(Identifier node) {
		node.setType(this.variableMaps.peek().get(node.getName()));
	}

	@Override
	public void visit(VarDeclarationList node) {
		Type declaredType = node.getChildren().get(0).getType();

		for (int i = 1; i < node.getChildren().size(); ++i) {
			VarDeclaration decl = (VarDeclaration) node.getChildren().get(i);
			decl.setType(declaredType);
			Identifier id = (Identifier) decl.getChildren().get(0);
			id.setType(declaredType);
			this.variableMaps.peek().put(id.getName(), declaredType);
		}
	}
}
