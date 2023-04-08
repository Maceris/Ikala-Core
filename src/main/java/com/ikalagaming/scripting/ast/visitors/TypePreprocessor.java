package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.VariableTypeMap;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.ArrayAccess;
import com.ikalagaming.scripting.ast.Block;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprAssign;
import com.ikalagaming.scripting.ast.ExprTernary;
import com.ikalagaming.scripting.ast.FieldAccess;
import com.ikalagaming.scripting.ast.ForLoop;
import com.ikalagaming.scripting.ast.Identifier;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.scripting.ast.TypeName;
import com.ikalagaming.scripting.ast.VarDeclaration;
import com.ikalagaming.scripting.ast.VarDeclarationList;
import com.ikalagaming.scripting.ast.Type.Base;

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
	 * Process the types for the tree. Intended for use only on the root node.
	 *
	 * @param root The current root node.
	 */
	private void process(Node root) {
		final boolean newContext =
			(root instanceof Block || root instanceof ForLoop);

		if (newContext) {
			variableMaps.push(variableMaps.peek().clone());
		}

		if (root.getChildren().size() > 0) {
			root.getChildren().forEach(child -> this.process(child));
		}
		root.process(this);

		if (newContext) {
			variableMaps.pop();
		}
	}

	/**
	 * Process the types for the tree. Intended for use only on the root node.
	 * 
	 * @param ast The tree to process.
	 */
	public void processTreeTypes(CompilationUnit ast) {
		VariableTypeMap variables = new VariableTypeMap();

		variableMaps.push(variables);
		LabelPass labels = new LabelPass(variables);
		labels.process(ast);

		this.process(ast);
	}

	@Override
	public void visit(ArrayAccess node) {
		Type actual = node.getChildren().get(0).getType()
			.dereference(node.getChildren().size() - 1);
		node.setType(actual);
	}

	@Override
	public void visit(ExprArithmetic node) {
		if (node.getChildren().size() < 1) {
			log.warn("Missing child for node {}", this.toString());
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
			case DIV:
			case MUL:
				if (node.getChildren().size() < 2) {
					log.warn("Missing second child for node {}",
						this.toString());
					return;
				}
			case ADD:
			case SUB:
				if (node.getChildren().size() < 2) {
					node.setType(firstType);
					break;
				}
				// invalid types
				if (firstType.anyOf(Base.BOOLEAN, Base.IDENTIFIER, Base.VOID)
					|| secondType.anyOf(Base.BOOLEAN, Base.IDENTIFIER,
						Base.VOID)) {
					log.warn("Invalid types {} {} {}", firstType.toString(),
						node.getOperator().getReadable(),
						secondType.toString());
					return;
				}

				// Unknown
				if (firstType.anyOf(Base.UNKNOWN)
					|| secondType.anyOf(Base.UNKNOWN)) {
					node.setType(Type.unknownType());
					break;
				}

				// Same type

				// different types

				if (firstType.equals(secondType) || firstType.anyOf(Base.STRING)
					|| secondType.anyOf(Base.STRING)) {
					// String concatenation
					node.setType(firstType);
					break;
				}
				if (firstType.anyOf(Base.DOUBLE)
					&& secondType.anyOf(Base.CHAR, Base.INT)) {
					node.setType(firstType);
					break;
				}
				if (secondType.anyOf(Base.DOUBLE)
					&& firstType.anyOf(Base.CHAR, Base.INT)) {
					node.setType(secondType);
					break;
				}
				if (firstType.anyOf(Base.INT) && secondType.anyOf(Base.CHAR)) {
					node.setType(firstType);
					break;
				}
				if (secondType.anyOf(Base.INT) && firstType.anyOf(Base.CHAR)) {
					node.setType(secondType);
					break;
				}
				log.warn("Cannot automatically cast types {} and {}",
					firstType.toString(), secondType.toString());
				break;

			case DEC_PREFIX:
			case DEC_SUFFIX:
			case INC_PREFIX:
			case INC_SUFFIX:
				if (firstType.anyOf(Base.INT, Base.CHAR, Base.DOUBLE,
					Base.UNKNOWN)) {
					node.setType(firstType);
				}
				else {
					log.warn("Invalid type {}", firstType.toString());
				}
				break;
			case MOD:
				if (node.getChildren().size() < 2) {
					log.warn("Missing second child for node {}",
						this.toString());
					return;
				}
				if ((firstType.anyOf(Base.INT)
					&& secondType.anyOf(Base.INT, Base.CHAR))
					|| (firstType.anyOf(Base.DOUBLE)
						&& secondType.anyOf(Base.INT, Base.DOUBLE, Base.CHAR))
					|| (firstType.anyOf(Base.CHAR)
						&& secondType.anyOf(Base.CHAR))) {
					node.setType(firstType);
					break;
				}
				if (firstType.anyOf(Base.INT, Base.DOUBLE, Base.CHAR)
					&& secondType.anyOf(Base.DOUBLE)) {
					node.setType(secondType);
					break;
				}
				log.warn("Cannot automatically cast types {} and {}",
					firstType.toString(), secondType.toString());
				break;
			default:
				log.warn("Unknown operator {}", node.getOperator().toString());
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

		log.warn("Non-matching types {} and {}", ifTrue, ifFalse);
	}

	@Override
	public void visit(FieldAccess node) {
		// Reset the field's type, in case we have a variable with that name
		node.getChildren().get(1).setType(Type.unknownType());
	}

	@Override
	public void visit(Identifier node) {
		node.setType(variableMaps.peek().get(node.getName()));
	}

	@Override
	public void visit(TypeName node) {
		VariableTypeMap variables = variableMaps.peek();
		if (node.getChildren().size() == 1) {
			Identifier id = (Identifier) node.getChildren().get(0);
			if (variables.contains(id.getName())) {
				final Type idType = variables.get(id.getName());
				node.setType(idType);
				id.setType(idType);
				return;
			}
		}
		for (Node child : node.getChildren()) {
			child.setType(Type.unknownType());
		}
		node.setType(Type.unknownType());
	}

	@Override
	public void visit(VarDeclarationList node) {
		Type declaredType = node.getChildren().get(0).getType();

		for (int i = 1; i < node.getChildren().size(); ++i) {
			VarDeclaration decl = (VarDeclaration) node.getChildren().get(i);
			decl.setType(declaredType);
			Identifier id = (Identifier) decl.getChildren().get(0);
			id.setType(declaredType);
			variableMaps.peek().put(id.getName(), declaredType);
		}
	}
}
