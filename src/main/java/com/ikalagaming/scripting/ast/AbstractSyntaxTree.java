package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.IkalaScriptParser.AdditiveExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.ArgumentListContext;
import com.ikalagaming.scripting.IkalaScriptParser.ArrayAccessContext;
import com.ikalagaming.scripting.IkalaScriptParser.ArrayAccess_LHSContext;
import com.ikalagaming.scripting.IkalaScriptParser.ArrayAccess_LHS_GeneralContext;
import com.ikalagaming.scripting.IkalaScriptParser.ArrayTypeContext;
import com.ikalagaming.scripting.IkalaScriptParser.AssignmentContext;
import com.ikalagaming.scripting.IkalaScriptParser.BlockContext;
import com.ikalagaming.scripting.IkalaScriptParser.BlockStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.BreakStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.CastExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.ClassOrInterfaceTypeContext;
import com.ikalagaming.scripting.IkalaScriptParser.CompilationUnitContext;
import com.ikalagaming.scripting.IkalaScriptParser.ConditionalAndExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.ConditionalExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.ConditionalOrExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.ContinueStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.DoStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.EqualityExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.ExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.FieldAccessContext;
import com.ikalagaming.scripting.IkalaScriptParser.ForInitContext;
import com.ikalagaming.scripting.IkalaScriptParser.ForStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.ForStatementNoShortIfContext;
import com.ikalagaming.scripting.IkalaScriptParser.GotoStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.IfThenElseStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.IfThenElseStatementNoShortIfContext;
import com.ikalagaming.scripting.IkalaScriptParser.IfThenStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.LabelContext;
import com.ikalagaming.scripting.IkalaScriptParser.LabeledStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.LabeledStatementNoShortIfContext;
import com.ikalagaming.scripting.IkalaScriptParser.LeftHandSideContext;
import com.ikalagaming.scripting.IkalaScriptParser.LiteralContext;
import com.ikalagaming.scripting.IkalaScriptParser.LocalVariableDeclarationContext;
import com.ikalagaming.scripting.IkalaScriptParser.MethodInvocationContext;
import com.ikalagaming.scripting.IkalaScriptParser.MethodInvocation_LHSContext;
import com.ikalagaming.scripting.IkalaScriptParser.MultiplicativeExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.NumericTypeContext;
import com.ikalagaming.scripting.IkalaScriptParser.PostDecrementExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.PostIncrementExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.PostfixExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.PreDecrementExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.PreIncrementExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.PrimaryContext;
import com.ikalagaming.scripting.IkalaScriptParser.Primary_LHSContext;
import com.ikalagaming.scripting.IkalaScriptParser.Primary_LHS_accessContext;
import com.ikalagaming.scripting.IkalaScriptParser.Primary_extensionContext;
import com.ikalagaming.scripting.IkalaScriptParser.Primary_extension_accessContext;
import com.ikalagaming.scripting.IkalaScriptParser.PrimitiveTypeContext;
import com.ikalagaming.scripting.IkalaScriptParser.ReferenceTypeContext;
import com.ikalagaming.scripting.IkalaScriptParser.RelationalExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.StatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.StatementExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.StatementExpressionListContext;
import com.ikalagaming.scripting.IkalaScriptParser.StatementNoShortIfContext;
import com.ikalagaming.scripting.IkalaScriptParser.StatementWithoutTrailingSubstatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.SwitchBlockContext;
import com.ikalagaming.scripting.IkalaScriptParser.SwitchBlockStatementGroupContext;
import com.ikalagaming.scripting.IkalaScriptParser.SwitchLabelContext;
import com.ikalagaming.scripting.IkalaScriptParser.SwitchStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.TypeContext;
import com.ikalagaming.scripting.IkalaScriptParser.UnaryExpressionContext;
import com.ikalagaming.scripting.IkalaScriptParser.UnaryExpressionNotPlusMinusContext;
import com.ikalagaming.scripting.IkalaScriptParser.VariableDeclaratorContext;
import com.ikalagaming.scripting.IkalaScriptParser.VariableDeclaratorIdContext;
import com.ikalagaming.scripting.IkalaScriptParser.WhileStatementContext;
import com.ikalagaming.scripting.IkalaScriptParser.WhileStatementNoShortIfContext;
import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Utility class for converting the parser output to an abstract syntax tree
 * that we can manipulate.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class AbstractSyntaxTree {
	private static final String UNKNOWN_STATEMENT = "UNKNOWN_STATEMENT";
	private static final String UNKNOWN_UNARY_EXPRESSION =
		"UNKNOWN_UNARY_EXPRESSION";

	/**
	 * Fetch the base type of a primitive type.
	 *
	 * @param node The node to parse.
	 * @return The base type.
	 * @throws IllegalArgumentException If it is not a valid type.
	 */
	private static Base getBase(PrimitiveTypeContext node) {
		if (node.BOOLEAN() != null) {
			return Base.BOOLEAN;
		}
		if (node.STRING() != null) {
			return Base.STRING;
		}

		NumericTypeContext number = node.numericType();
		if (number == null) {
			final String error = SafeResourceLoader.getString(
				"UNKNOWN_NON_NUMERIC_TYPE", ScriptManager.getResourceBundle());
			AbstractSyntaxTree.log.warn(error, node.getText());
			throw new IllegalArgumentException(
				SafeResourceLoader.format(error, node.getText()));
		}

		if (number.INT() != null) {
			return Base.INT;
		}
		if (number.CHAR() != null) {
			return Base.CHAR;
		}
		if (number.DOUBLE() != null) {
			return Base.DOUBLE;
		}
		final String error = SafeResourceLoader.getString(
			"UNKNOWN_PRIMITIVE_TYPE", ScriptManager.getResourceBundle());

		AbstractSyntaxTree.log.warn(error, node.getText());
		throw new IllegalArgumentException(
			SafeResourceLoader.format(error, node.getText()));
	}

	/**
	 * Fetch the type of a reference type.
	 *
	 * @param node The node to parse.
	 * @return The type.
	 * @throws IllegalArgumentException If it is not a valid type.
	 */
	private static Type getType(ReferenceTypeContext node) {
		if (node.classOrInterfaceType() != null) {
			ClassOrInterfaceTypeContext identifier =
				node.classOrInterfaceType();
			return Type.identifier(identifier.getText());
		}
		if (node.Identifier() != null) {
			return Type.identifier(node.Identifier().getText());
		}
		if (node.arrayType() != null) {
			ArrayTypeContext arrayType = node.arrayType();
			final int dims = arrayType.dims().LBRACK().size();

			if (arrayType.primitiveType() != null) {
				return Type.primitiveArray(
					AbstractSyntaxTree.getBase(arrayType.primitiveType()),
					dims);
			}
			if (arrayType.classOrInterfaceType() != null) {
				ClassOrInterfaceTypeContext identifier =
					arrayType.classOrInterfaceType();
				return Type.identifierArray(identifier.getText(), dims);
			}
			if (arrayType.Identifier() != null) {
				return Type.identifierArray(arrayType.Identifier().getText(),
					dims);
			}
			AbstractSyntaxTree.log
				.warn(
					SafeResourceLoader.getString("UNKNOWN_ARRAY_TYPE",
						ScriptManager.getResourceBundle()),
					arrayType.getText());
		}
		final String error = SafeResourceLoader.getString(
			"UNKNOWN_REFERENCE_TYPE", ScriptManager.getResourceBundle());
		AbstractSyntaxTree.log.warn(error, node.getText());
		throw new IllegalArgumentException(
			SafeResourceLoader.format(error, node.getText()));
	}

	/**
	 * Calculate the type of a type declaration.
	 *
	 * @param node The context to parse.
	 * @return The corresponding type.
	 */
	private static Type getType(TypeContext node) {
		if (node.primitiveType() != null) {
			return Type
				.primitive(AbstractSyntaxTree.getBase(node.primitiveType()));
		}
		if (node.referenceType() != null) {
			return AbstractSyntaxTree.getType(node.referenceType());
		}

		AbstractSyntaxTree.log
			.warn(SafeResourceLoader.getString("UNKNOWN_TYPE_STATEMENT",
				ScriptManager.getResourceBundle()), node.getText());
		return null;
	}

	/**
	 * Convert an identifier node to an Identifier object.
	 *
	 * @param node The Identifier terminal node.
	 * @return An AST node for an identifier.
	 */
	private static Node identifierNode(TerminalNode node) {
		Identifier identifier = new Identifier();
		identifier.setName(node.getText());
		return identifier;
	}

	/**
	 * Process a primary expression extension.
	 *
	 * @param extension The context for the extension.
	 * @param currentRoot The current root node of the primary expression, which
	 *            is updated for each extension so that each node has an
	 *            expression on the left and a single operation on the right.
	 * @return The newly created node that will serve as the new root of the
	 *         expression.
	 */
	private static Node primaryExtension(Primary_extensionContext extension,
		Node currentRoot) {
		if (extension.fieldAccess_extension() != null) {
			FieldAccess newNode = new FieldAccess();
			newNode.setType(Type.unknownType());
			newNode.addChild(currentRoot);
			newNode.addChild(AbstractSyntaxTree.identifierNode(
				extension.fieldAccess_extension().Identifier()));
			return newNode;
		}
		if (extension.arrayAccess_extension() != null) {
			ArrayAccess newNode = new ArrayAccess();

			if (extension.arrayAccess_extension().expression() != null) {
				extension.arrayAccess_extension().expression().stream()
					.map(AbstractSyntaxTree::process)
					.forEach(newNode::addChild);
			}
			Primary_extension_accessContext arrayLHS =
				extension.arrayAccess_extension().primary_extension_access();

			if (arrayLHS.fieldAccess_extension() != null) {
				FieldAccess newNodeLHS = new FieldAccess();
				newNodeLHS.setType(Type.unknownType());
				// put the current root node on the far left of the tree
				newNodeLHS.addChild(currentRoot);
				newNodeLHS.addChild(AbstractSyntaxTree.identifierNode(
					arrayLHS.fieldAccess_extension().Identifier()));
				/*
				 * The root node is going to have one child, an array access,
				 * which is indexing into a field, which belongs to whatever the
				 * current leftNode is.
				 */
				newNode.addChild(newNodeLHS);
			}
			else if (arrayLHS.methodInvocation_extension() != null) {
				Call newNodeLHS = new Call();
				newNodeLHS.setType(Type.unknownType());
				newNodeLHS.addChild(currentRoot);
				newNodeLHS.setPrimary(true);

				newNodeLHS.addChild(AbstractSyntaxTree.identifierNode(
					arrayLHS.methodInvocation_extension().Identifier()));
				if (arrayLHS.methodInvocation_extension()
					.argumentList() != null) {
					newNodeLHS.addChild(AbstractSyntaxTree.process(
						arrayLHS.methodInvocation_extension().argumentList()));
				}
				/*
				 * The root node is going to have one child, an array access,
				 * which is indexing into the result of a method call, which is
				 * a method on whatever the leftNode is.
				 */
				newNode.addChild(newNodeLHS);
			}
			else {
				AbstractSyntaxTree.log.warn(
					SafeResourceLoader.getString(
						"UNKNOWN_PRIMARY_EXPRESSION_ACCESS",
						ScriptManager.getResourceBundle()),
					extension.getText());
			}
			return newNode;
		}
		if (extension.methodInvocation_extension() != null) {
			Call newNode = new Call();
			newNode.setType(Type.unknownType());
			newNode.addChild(currentRoot);
			newNode.setPrimary(true);

			newNode.addChild(AbstractSyntaxTree.identifierNode(
				extension.methodInvocation_extension().Identifier()));

			if (extension.methodInvocation_extension().argumentList() != null) {
				newNode.addChild(AbstractSyntaxTree.process(
					extension.methodInvocation_extension().argumentList()));
			}
			return newNode;
		}

		AbstractSyntaxTree.log
			.warn(SafeResourceLoader.getString("UNKNOWN_PRIMARY_EXTENSION",
				ScriptManager.getResourceBundle()), extension.getText());
		return null;
	}

	/**
	 * Process the left hand side of a primary expression into a node.
	 *
	 * @param lhs The left hand side context.
	 * @return The resulting node.
	 */
	private static Node primaryLHS(Primary_LHSContext lhs) {
		if (lhs.literal() != null) {
			return AbstractSyntaxTree.process(lhs.literal());
		}
		if (lhs.expression() != null) {
			return AbstractSyntaxTree.process(lhs.expression());
		}
		if (lhs.arrayAccess_LHS() != null) {
			ArrayAccess_LHSContext array = lhs.arrayAccess_LHS();
			Node leftNode = new ArrayAccess();

			if (array.Identifier() != null) {
				leftNode.addChild(
					AbstractSyntaxTree.identifierNode(array.Identifier()));
			}
			else if (array.primary_LHS_access() != null) {
				Primary_LHS_accessContext arrayLeft =
					array.primary_LHS_access();
				if (arrayLeft.literal() != null) {
					leftNode.addChild(
						AbstractSyntaxTree.process(arrayLeft.literal()));
				}
				else if (arrayLeft.expression() != null) {
					leftNode.addChild(
						AbstractSyntaxTree.process(arrayLeft.expression()));
				}
				else if (arrayLeft.methodInvocation_LHS() != null) {
					leftNode.addChild(AbstractSyntaxTree
						.process(arrayLeft.methodInvocation_LHS()));
				}
				else {
					// Should be impossible unless the grammar changes
					AbstractSyntaxTree.log.warn(
						SafeResourceLoader.getString(
							"UNKNOWN_PRIMARY_LEFT_SIDE_ARRAY",
							ScriptManager.getResourceBundle()),
						arrayLeft.getText());
				}
			}
			else {
				// Should be impossible unless the grammar changes
				AbstractSyntaxTree.log.warn(SafeResourceLoader.getString(
					"UNKNOWN_PRIMARY_LEFT_SIDE_ARRAY",
					ScriptManager.getResourceBundle()), array.getText());
			}
			array.expression().stream().map(AbstractSyntaxTree::process)
				.forEach(leftNode::addChild);
			return leftNode;
		}
		if (lhs.methodInvocation_LHS() != null) {
			return AbstractSyntaxTree.process(lhs.methodInvocation_LHS());
		}
		if (lhs.Identifier() != null) {
			return AbstractSyntaxTree.identifierNode(lhs.Identifier());
		}

		// Should be impossible unless the grammar changes
		AbstractSyntaxTree.log
			.warn(SafeResourceLoader.getString("UNKNOWN_PRIMARY_EXPRESSION",
				ScriptManager.getResourceBundle()), lhs.getText());

		return null;
	}

	/**
	 * Process an additive expression. We know the result will be arithmetic,
	 * but need to work out the actual type based on the type of the numbers
	 * involved.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(AdditiveExpressionContext node) {
		if (node.additiveExpression() != null) {
			ExprArithmetic result = new ExprArithmetic();
			if (node.ADD() != null) {
				result.setOperator(ExprArithmetic.Operator.ADD);
			}
			else if (node.SUB() != null) {
				result.setOperator(ExprArithmetic.Operator.SUB);
			}
			else {
				// Should be impossible unless the grammar changes
				AbstractSyntaxTree.log.warn(
					SafeResourceLoader.getString("UNKNOWN_ADDITIVE_OPERATOR",
						ScriptManager.getResourceBundle()),
					node.getText());
			}
			result.addChild(
				AbstractSyntaxTree.process(node.additiveExpression()));
			result.addChild(
				AbstractSyntaxTree.process(node.multiplicativeExpression()));
			return result;
		}
		return AbstractSyntaxTree.process(node.multiplicativeExpression());
	}

	private static Node process(ArgumentListContext node) {
		ArgumentList result = new ArgumentList();
		result.setType(Type.voidType());
		node.expression().stream().map(AbstractSyntaxTree::process)
			.forEach(result::addChild);
		return result;
	}

	/**
	 * Process an array access.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ArrayAccessContext node) {
		ArrayAccess result = new ArrayAccess();

		if (node.Identifier() != null) {
			result
				.addChild(AbstractSyntaxTree.identifierNode(node.Identifier()));
		}
		else if (node.arrayAccess_LHS_General() != null) {
			ArrayAccess_LHS_GeneralContext lhs = node.arrayAccess_LHS_General();
			if (lhs.literal() != null) {
				result.addChild(AbstractSyntaxTree.process(lhs.literal()));
			}
			else if (lhs.expression() != null) {
				result.addChild(AbstractSyntaxTree.process(lhs.expression()));
			}
			else if (lhs.fieldAccess() != null) {
				result.addChild(AbstractSyntaxTree.process(lhs.fieldAccess()));
			}
			else if (lhs.methodInvocation() != null) {
				result.addChild(
					AbstractSyntaxTree.process(lhs.methodInvocation()));
			}
			else {
				AbstractSyntaxTree.log.warn(
					SafeResourceLoader.getString("UNKNOWN_ARRAY_LEFT_SIDE",
						ScriptManager.getResourceBundle()),
					lhs.getText());
			}
		}

		node.expression().stream().map(AbstractSyntaxTree::process)
			.forEach(result::addChild);

		return result;
	}

	/**
	 * Process an assignment.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(AssignmentContext node) {
		ExprAssign result = new ExprAssign();

		LeftHandSideContext lhs = node.leftHandSide();
		if (lhs.Identifier() != null) {
			result
				.addChild(AbstractSyntaxTree.identifierNode(lhs.Identifier()));
		}
		else if (lhs.fieldAccess() != null) {
			result.addChild(AbstractSyntaxTree.process(lhs.fieldAccess()));
		}
		else if (lhs.arrayAccess() != null) {
			result.addChild(AbstractSyntaxTree.process(lhs.arrayAccess()));
		}
		else {
			AbstractSyntaxTree.log
				.warn(SafeResourceLoader.getString("UNKNOWN_ASSIGN_LEFT_SIDE",
					ScriptManager.getResourceBundle()), lhs.getText());
		}

		result.setOperator(
			ExprAssign.Operator.fromText(node.assignmentOperator().getText()));

		result.addChild(AbstractSyntaxTree.process(node.expression()));

		return result;
	}

	/**
	 * Process a block.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(BlockContext node) {
		Block result = new Block();
		result.setType(Type.voidType());
		if (node.blockStatements() != null) {
			node.blockStatements().blockStatement().stream()
				.map(AbstractSyntaxTree::process).forEach(result::addChild);
		}
		return result;
	}

	/**
	 * Process a block statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(BlockStatementContext node) {
		if (node.localVariableDeclarationStatement() != null) {
			return AbstractSyntaxTree
				.process(node.localVariableDeclarationStatement()
					.localVariableDeclaration());
		}
		if (node.statement() != null) {
			return AbstractSyntaxTree.process(node.statement());
		}
		if (node.label() != null) {
			return AbstractSyntaxTree.process(node.label());
		}

		AbstractSyntaxTree.log
			.warn(SafeResourceLoader.getString("UNKNOWN_BLOCK_STATEMENT",
				ScriptManager.getResourceBundle()), node.getText());

		return null;
	}

	/**
	 * Process a break statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(BreakStatementContext node/* NOSONAR */) {
		Break result = new Break();
		result.setType(Type.voidType());
		return result;
	}

	/**
	 * Process a cast expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(CastExpressionContext node) {
		Cast result = new Cast();
		if (node.primitiveType() != null) {
			// first case
			result.setType(Type
				.primitive(AbstractSyntaxTree.getBase(node.primitiveType())));
			result.addChild(AbstractSyntaxTree.process(node.unaryExpression()));
		}
		else if (node.unaryExpressionNotPlusMinus() != null) {
			// second case
			result.setType(AbstractSyntaxTree.getType(node.referenceType()));
			result.addChild(
				AbstractSyntaxTree.process(node.unaryExpressionNotPlusMinus()));
		}
		else {
			AbstractSyntaxTree.log.warn(SafeResourceLoader
				.getString("UNKNOWN_CAST", ScriptManager.getResourceBundle()),
				node.getText());
			return null;
		}
		return result;
	}

	/**
	 * Process a program into an abstract syntax tree.
	 *
	 * @param parserOutput The root node of the output from the parser.
	 * @return The root node of the abstract syntax tree.
	 */
	public static CompilationUnit process(CompilationUnitContext parserOutput) {
		CompilationUnit root = new CompilationUnit();
		root.setType(Type.voidType());

		if (parserOutput.blockStatement() != null) {
			final int count = parserOutput.blockStatement().size();
			for (int i = 0; i < count; ++i) {
				Node child =
					AbstractSyntaxTree.process(parserOutput.blockStatement(i));
				if (child == null) {
					root.setInvalid(true);
					// Might as well immediately bail
					return root;
				}
				root.addChild(child);

			}
		}
		return root;
	}

	/**
	 * Process a conditional and expression. This has to be comparing booleans,
	 * and thus is a boolean.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ConditionalAndExpressionContext node) {
		if (node.AND() != null) {
			ExprLogic result = new ExprLogic();
			result.setOperator(ExprLogic.Operator.AND);
			result.addChild(
				AbstractSyntaxTree.process(node.conditionalAndExpression()));
			result.addChild(
				AbstractSyntaxTree.process(node.equalityExpression()));
			result.setType(Type.primitive(Base.BOOLEAN));
			return result;
		}
		return AbstractSyntaxTree.process(node.equalityExpression());
	}

	/**
	 * Process a conditional expression. We don't know the type offhand for
	 * this.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ConditionalExpressionContext node) {
		if (node.QUESTION() != null) {
			ExprTernary result = new ExprTernary();

			result.addChild(
				AbstractSyntaxTree.process(node.conditionalOrExpression()));
			result.addChild(AbstractSyntaxTree.process(node.expression()));
			result.addChild(
				AbstractSyntaxTree.process(node.conditionalExpression()));
			return result;
		}
		return AbstractSyntaxTree.process(node.conditionalOrExpression());
	}

	/**
	 * Process a conditional or expression. This has to be comparing booleans,
	 * and thus is a boolean.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ConditionalOrExpressionContext node) {
		if (node.OR() != null) {
			ExprLogic result = new ExprLogic();
			result.setOperator(ExprLogic.Operator.OR);
			result.addChild(
				AbstractSyntaxTree.process(node.conditionalOrExpression()));
			result.addChild(
				AbstractSyntaxTree.process(node.conditionalAndExpression()));
			result.setType(Type.primitive(Base.BOOLEAN));
			return result;
		}
		return AbstractSyntaxTree.process(node.conditionalAndExpression());
	}

	/**
	 * Process a continue statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ContinueStatementContext node/* NOSONAR */) {
		Continue result = new Continue();
		result.setType(Type.voidType());
		return result;
	}

	/**
	 * Process a do while statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(DoStatementContext node) {
		DoWhile result = new DoWhile();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.statement()));
		result.addChild(AbstractSyntaxTree.process(node.expression()));
		return result;
	}

	/**
	 * Process a equality expression. The result is going to be a boolean,
	 * regardless of what we are comparing, as long as it's a valid comparison.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(EqualityExpressionContext node) {
		if (node.equalityExpression() != null) {
			ExprEquality result = new ExprEquality();
			if (node.EQUAL() != null) {
				result.setOperator(ExprEquality.Operator.EQUAL);
			}
			else if (node.NOTEQUAL() != null) {
				result.setOperator(ExprEquality.Operator.NOT_EQUAL);
			}
			else {
				// Should be impossible unless the grammar changes
				AbstractSyntaxTree.log.warn(
					SafeResourceLoader.getString("UNKNOWN_EQUALITY_OPERATOR",
						ScriptManager.getResourceBundle()),
					node.getText());
			}
			result.addChild(
				AbstractSyntaxTree.process(node.equalityExpression()));
			result.addChild(
				AbstractSyntaxTree.process(node.relationalExpression()));
			result.setType(Type.primitive(Base.BOOLEAN));
			return result;
		}
		return AbstractSyntaxTree.process(node.relationalExpression());
	}

	/**
	 * Process an expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ExpressionContext node) {
		if (node.assignment() != null) {
			return AbstractSyntaxTree.process(node.assignment());
		}
		if (node.conditionalExpression() != null) {
			return AbstractSyntaxTree.process(node.conditionalExpression());
		}
		AbstractSyntaxTree.log.warn(SafeResourceLoader
			.getString("UNKNOWN_EXPRESSION", ScriptManager.getResourceBundle()),
			node.getText());
		return null;
	}

	/**
	 * Process a field access.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(FieldAccessContext node) {
		FieldAccess result = new FieldAccess();
		result.setType(Type.unknownType());

		result.addChild(AbstractSyntaxTree.process(node.primary()));
		result.addChild(AbstractSyntaxTree.identifierNode(node.Identifier()));

		return result;
	}

	/**
	 * Process a for init.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ForInitContext node) {
		if (node.statementExpressionList() != null) {
			return AbstractSyntaxTree.process(node.statementExpressionList());
		}
		if (node.localVariableDeclaration() != null) {
			return AbstractSyntaxTree.process(node.localVariableDeclaration());
		}
		AbstractSyntaxTree.log.warn(SafeResourceLoader
			.getString("UNKNOWN_FOR_INIT", ScriptManager.getResourceBundle()),
			node.getText());
		return null;
	}

	/**
	 * Process a basic for loop.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ForStatementContext node) {
		ForLoop result = new ForLoop();
		result.setType(Type.voidType());
		if (node.forInit() != null) {
			result.addChild(AbstractSyntaxTree.process(node.forInit()));
			result.setInitializer(true);
		}
		if (node.expression() != null) {
			result.addChild(AbstractSyntaxTree.process(node.expression()));
			result.setCondition(true);
		}
		if (node.statementExpressionList() != null) {
			result.addChild(
				AbstractSyntaxTree.process(node.statementExpressionList()));
			result.setUpdate(true);
		}

		result.addChild(AbstractSyntaxTree.process(node.statement()));

		return result;
	}

	/**
	 * Process a basic for loop without a short if.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(ForStatementNoShortIfContext node) {
		ForLoop result = new ForLoop();
		result.setType(Type.voidType());
		if (node.forInit() != null) {
			result.addChild(AbstractSyntaxTree.process(node.forInit()));
			result.setInitializer(true);
		}
		if (node.expression() != null) {
			result.addChild(AbstractSyntaxTree.process(node.expression()));
			result.setCondition(true);
		}
		if (node.statementExpressionList() != null) {
			result.addChild(
				AbstractSyntaxTree.process(node.statementExpressionList()));
			result.setUpdate(true);
		}

		result.addChild(AbstractSyntaxTree.process(node.statementNoShortIf()));

		return result;
	}

	/**
	 * Process a goto statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(GotoStatementContext node) {
		Goto result = new Goto();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.identifierNode(node.Identifier()));
		return result;
	}

	/**
	 * Process an if then else statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(IfThenElseStatementContext node) {
		If result = new If();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.expression()));
		result.addChild(AbstractSyntaxTree.process(node.statementNoShortIf()));
		result.addChild(AbstractSyntaxTree.process(node.statement()));
		return result;
	}

	/**
	 * Process an if then else statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(IfThenElseStatementNoShortIfContext node) {
		If result = new If();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.expression()));
		result.addChild(AbstractSyntaxTree.process(node.statementNoShortIf(0)));
		result.addChild(AbstractSyntaxTree.process(node.statementNoShortIf(1)));
		return result;
	}

	/**
	 * Process an if then statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(IfThenStatementContext node) {
		If result = new If();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.expression()));
		result.addChild(AbstractSyntaxTree.process(node.statement()));
		return result;
	}

	/**
	 * Process a labeled statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(LabelContext node) {
		Label result = new Label();
		final String name = node.Identifier().getText();
		result.setType(Type.label(name));
		result.setName(name);
		return result;
	}

	/**
	 * Process a labeled statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(LabeledStatementContext node) {
		LabeledStatement result = new LabeledStatement();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.label()));
		result.addChild(AbstractSyntaxTree.process(node.statement()));
		return result;
	}

	/**
	 * Process a labeled statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(LabeledStatementNoShortIfContext node) {
		LabeledStatement result = new LabeledStatement();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.label()));
		result.addChild(AbstractSyntaxTree.process(node.statementNoShortIf()));
		return result;
	}

	/**
	 * Process a literal value.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(LiteralContext node) {
		if (node.IntegerLiteral() != null) {
			ConstInt result = new ConstInt();
			try {
				result.setValue(
					Integer.parseInt(node.IntegerLiteral().getText()));
			}
			catch (NumberFormatException e) {
				AbstractSyntaxTree.log
					.warn(SafeResourceLoader.getString("INVALID_INT",
						ScriptManager.getResourceBundle()), node.getText());
			}
			result.setType(Type.primitive(Base.INT));
			return result;
		}
		if (node.FloatingPointLiteral() != null) {
			ConstDouble result = new ConstDouble();
			try {
				result.setValue(
					Double.parseDouble(node.FloatingPointLiteral().getText()));
			}
			catch (NumberFormatException e) {
				AbstractSyntaxTree.log
					.warn(SafeResourceLoader.getString("INVALID_FLOAT",
						ScriptManager.getResourceBundle()), node.getText());
			}
			result.setType(Type.primitive(Base.DOUBLE));
			return result;
		}
		if (node.BooleanLiteral() != null) {
			ConstBool result = new ConstBool();
			result.setValue(
				Boolean.parseBoolean(node.BooleanLiteral().getText()));
			result.setType(Type.primitive(Base.BOOLEAN));
			return result;
		}
		if (node.CharacterLiteral() != null) {
			ConstChar result = new ConstChar();
			result.setValue(node.CharacterLiteral().getText().charAt(1));
			result.setType(Type.primitive(Base.CHAR));
			return result;
		}
		if (node.StringLiteral() != null) {
			ConstString result = new ConstString();
			result.setValue(node.StringLiteral().getText());
			result.setType(Type.primitive(Base.STRING));
			return result;
		}
		if (node.NullLiteral() != null) {
			return new ConstNull();
		}
		AbstractSyntaxTree.log.warn(SafeResourceLoader
			.getString("UNKNOWN_CONSTANT", ScriptManager.getResourceBundle()),
			node.getText());
		return null;
	}

	/**
	 * Process a local variable declaration.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(LocalVariableDeclarationContext node) {
		VarDeclarationList result = new VarDeclarationList();
		result.setType(Type.voidType());

		TypeNode type = new TypeNode();
		type.setType(AbstractSyntaxTree.getType(node.type()));
		if (node.FINAL() != null) {
			type.setFinal(true);
		}
		result.addChild(type);

		for (VariableDeclaratorContext varList : node.variableDeclaratorList()
			.variableDeclarator()) {
			VarDeclaration decl = new VarDeclaration();
			VariableDeclaratorIdContext id = varList.variableDeclaratorId();
			decl.addChild(AbstractSyntaxTree.identifierNode(id.Identifier()));
			if (id.dims() != null) {
				decl.setDimensions(id.dims().LBRACK().size());
			}
			if (varList.expression() != null) {
				ExpressionContext init = varList.expression();
				decl.addChild(AbstractSyntaxTree.process(init));
			}
			result.addChild(decl);
		}

		return result;
	}

	/**
	 * Process a method invocation.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(MethodInvocation_LHSContext node) {
		Call result = new Call();
		result.addChild(AbstractSyntaxTree.identifierNode(node.Identifier()));
		if (node.argumentList() != null) {
			result.addChild(AbstractSyntaxTree.process(node.argumentList()));
		}
		result.setType(Type.unknownType());
		return result;
	}

	/**
	 * Process a method invocation.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(MethodInvocationContext node) {
		Call result = new Call();
		if (node.primary() != null) {
			result.addChild(AbstractSyntaxTree.process(node.primary()));
			result.setPrimary(true);
		}

		result.addChild(AbstractSyntaxTree.identifierNode(node.Identifier()));

		if (node.argumentList() != null) {
			result.addChild(AbstractSyntaxTree.process(node.argumentList()));
		}
		result.setType(Type.unknownType());
		return result;
	}

	/**
	 * Process a multiplicative expression. We know the result will be
	 * arithmetic, but need to work out the actual type based on the type of the
	 * numbers involved.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(MultiplicativeExpressionContext node) {
		if (node.multiplicativeExpression() != null) {
			ExprArithmetic result = new ExprArithmetic();
			if (node.MUL() != null) {
				result.setOperator(ExprArithmetic.Operator.MUL);
			}
			else if (node.DIV() != null) {
				result.setOperator(ExprArithmetic.Operator.DIV);
			}
			else if (node.MOD() != null) {
				result.setOperator(ExprArithmetic.Operator.MOD);
			}
			else {
				// Should be impossible unless the grammar changes
				AbstractSyntaxTree.log.warn(SafeResourceLoader.getString(
					"UNKNOWN_MULTIPLICATIVE_OPERATOR",
					ScriptManager.getResourceBundle()), node.getText());
			}
			result.addChild(
				AbstractSyntaxTree.process(node.multiplicativeExpression()));
			result.addChild(AbstractSyntaxTree.process(node.unaryExpression()));
			return result;
		}
		return AbstractSyntaxTree.process(node.unaryExpression());
	}

	/**
	 * Process a post decrement expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(PostDecrementExpressionContext node) {
		ExprArithmetic result = new ExprArithmetic();
		result.addChild(AbstractSyntaxTree.process(node.postfixExpression()));
		result.setOperator(ExprArithmetic.Operator.DEC_SUFFIX);
		result.setUnaryCount(1);
		return result;
	}

	/**
	 * Process a postfix expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(PostfixExpressionContext node) {
		int incCount = 0;
		int decCount = 0;

		if (node.INC() != null) {
			incCount = node.INC().size();
		}
		if (node.DEC() != null) {
			decCount = node.DEC().size();
		}

		final int totalDelta = incCount - decCount;

		Node statement;
		if (node.primary() != null) {
			statement = AbstractSyntaxTree.process(node.primary());
		}
		else {
			statement = AbstractSyntaxTree.identifierNode(node.Identifier());
		}

		if (totalDelta == 0) {
			// We don't have increments or decrements, or they cancel out
			return statement;
		}

		ExprArithmetic result = new ExprArithmetic();
		result.addChild(statement);

		if (totalDelta < 0) {
			result.setOperator(ExprArithmetic.Operator.DEC_SUFFIX);
		}
		else {
			// > 0, since we checked 0 earlier
			result.setOperator(ExprArithmetic.Operator.INC_SUFFIX);
		}
		result.setUnaryCount(Math.abs(totalDelta));

		return result;
	}

	/**
	 * Process a post increment expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(PostIncrementExpressionContext node) {
		ExprArithmetic result = new ExprArithmetic();
		result.addChild(AbstractSyntaxTree.process(node.postfixExpression()));
		result.setOperator(ExprArithmetic.Operator.INC_SUFFIX);
		result.setUnaryCount(1);
		return result;
	}

	/**
	 * Process a pre decrement expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(PreDecrementExpressionContext node) {
		ExprArithmetic result = new ExprArithmetic();
		result.addChild(AbstractSyntaxTree.process(node.unaryExpression()));
		result.setOperator(ExprArithmetic.Operator.DEC_PREFIX);
		result.setUnaryCount(1);
		return result;
	}

	/**
	 * Process a pre increment expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(PreIncrementExpressionContext node) {
		ExprArithmetic result = new ExprArithmetic();
		result.addChild(AbstractSyntaxTree.process(node.unaryExpression()));
		result.setOperator(ExprArithmetic.Operator.INC_PREFIX);
		result.setUnaryCount(1);
		return result;
	}

	/**
	 * Process a primary expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(PrimaryContext node) {

		Node leftNode = AbstractSyntaxTree.primaryLHS(node.primary_LHS());

		if (node.primary_extension() == null) {
			return leftNode;
		}

		for (Primary_extensionContext extension : node.primary_extension()) {
			leftNode = AbstractSyntaxTree.primaryExtension(extension, leftNode);
		}

		return leftNode;
	}

	/**
	 * Process a relational expression. The result is going to be a boolean,
	 * regardless of what we are comparing, as long as it's a valid comparison.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(RelationalExpressionContext node) {
		ExprRelation result = new ExprRelation();
		if (node.relationalExpression() != null) {
			if (node.LT() != null) {
				result.setOperator(ExprRelation.Operator.LT);
			}
			else if (node.LTE() != null) {
				result.setOperator(ExprRelation.Operator.LTE);
			}
			else if (node.GT() != null) {
				result.setOperator(ExprRelation.Operator.GT);
			}
			else if (node.GTE() != null) {
				result.setOperator(ExprRelation.Operator.GTE);
			}
			else {
				// Should be impossible unless the grammar changes
				AbstractSyntaxTree.log.warn(
					SafeResourceLoader.getString("UNKNOWN_RELATIONAL_OPERATOR",
						ScriptManager.getResourceBundle()),
					node.getText());
			}
			result.addChild(
				AbstractSyntaxTree.process(node.relationalExpression()));
			result.addChild(
				AbstractSyntaxTree.process(node.additiveExpression()));
			result.setType(Type.primitive(Base.BOOLEAN));
			return result;
		}
		return AbstractSyntaxTree.process(node.additiveExpression());
	}

	/**
	 * Process a statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(StatementContext node) {
		if (node.statementWithoutTrailingSubstatement() != null) {
			return AbstractSyntaxTree
				.process(node.statementWithoutTrailingSubstatement());
		}
		if (node.labeledStatement() != null) {
			return AbstractSyntaxTree.process(node.labeledStatement());
		}
		if (node.ifThenStatement() != null) {
			return AbstractSyntaxTree.process(node.ifThenStatement());
		}
		if (node.ifThenElseStatement() != null) {
			return AbstractSyntaxTree.process(node.ifThenElseStatement());
		}
		if (node.whileStatement() != null) {
			return AbstractSyntaxTree.process(node.whileStatement());
		}
		if (node.forStatement() != null) {
			return AbstractSyntaxTree.process(node.forStatement());
		}

		AbstractSyntaxTree.log.warn(
			SafeResourceLoader.getString(AbstractSyntaxTree.UNKNOWN_STATEMENT,
				ScriptManager.getResourceBundle()),
			node.getText());
		return null;
	}

	/**
	 * Process a statement expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(StatementExpressionContext node) {
		if (node.assignment() != null) {
			return AbstractSyntaxTree.process(node.assignment());
		}
		if (node.preIncrementExpression() != null) {
			return AbstractSyntaxTree.process(node.preIncrementExpression());
		}
		if (node.preDecrementExpression() != null) {
			return AbstractSyntaxTree.process(node.preDecrementExpression());
		}
		if (node.postIncrementExpression() != null) {
			return AbstractSyntaxTree.process(node.postIncrementExpression());
		}
		if (node.postDecrementExpression() != null) {
			return AbstractSyntaxTree.process(node.postDecrementExpression());
		}
		if (node.methodInvocation() != null) {
			return AbstractSyntaxTree.process(node.methodInvocation());
		}

		AbstractSyntaxTree.log
			.warn(SafeResourceLoader.getString("UNKNOWN_STATEMENT_EXPRESSION",
				ScriptManager.getResourceBundle()), node.getText());
		return null;
	}

	/**
	 * Process a statement expression list.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(StatementExpressionListContext node) {
		StatementList result = new StatementList();
		result.setType(Type.voidType());
		node.statementExpression().stream().map(AbstractSyntaxTree::process)
			.forEach(result::addChild);
		return result;
	}

	/**
	 * Process a statement with no short if.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(StatementNoShortIfContext node) {
		if (node.statementWithoutTrailingSubstatement() != null) {
			return AbstractSyntaxTree
				.process(node.statementWithoutTrailingSubstatement());
		}
		if (node.labeledStatementNoShortIf() != null) {
			return AbstractSyntaxTree.process(node.labeledStatementNoShortIf());
		}
		if (node.ifThenElseStatementNoShortIf() != null) {
			return AbstractSyntaxTree
				.process(node.ifThenElseStatementNoShortIf());
		}
		if (node.whileStatementNoShortIf() != null) {
			return AbstractSyntaxTree.process(node.whileStatementNoShortIf());
		}
		if (node.forStatementNoShortIf() != null) {
			return AbstractSyntaxTree.process(node.forStatementNoShortIf());
		}

		AbstractSyntaxTree.log.warn(
			SafeResourceLoader.getString(AbstractSyntaxTree.UNKNOWN_STATEMENT,
				ScriptManager.getResourceBundle()),
			node.getText());
		return null;
	}

	/**
	 * Process a statement without trailing substatement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node
		process(StatementWithoutTrailingSubstatementContext node) {
		if (node.block() != null) {
			return AbstractSyntaxTree.process(node.block());
		}
		if (node.expressionStatement() != null) {
			return AbstractSyntaxTree
				.process(node.expressionStatement().statementExpression());
		}
		if (node.switchStatement() != null) {
			return AbstractSyntaxTree.process(node.switchStatement());
		}
		if (node.doStatement() != null) {
			return AbstractSyntaxTree.process(node.doStatement());
		}
		if (node.breakStatement() != null) {
			return AbstractSyntaxTree.process(node.breakStatement());
		}
		if (node.continueStatement() != null) {
			return AbstractSyntaxTree.process(node.continueStatement());
		}
		if (node.gotoStatement() != null) {
			return AbstractSyntaxTree.process(node.gotoStatement());
		}
		if (node.exitStatement() != null) {
			Exit result = new Exit();
			result.setType(Type.voidType());
			return result;
		}

		AbstractSyntaxTree.log.warn(
			SafeResourceLoader.getString(AbstractSyntaxTree.UNKNOWN_STATEMENT,
				ScriptManager.getResourceBundle()),
			node.getText());
		return null;
	}

	/**
	 * Process a switch block.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(SwitchBlockContext node) {
		Block result = new Block();
		result.setType(Type.voidType());
		if (node.switchBlockStatementGroup() != null) {
			node.switchBlockStatementGroup().stream()
				.map(AbstractSyntaxTree::process).forEach(result::addChild);
		}
		if (node.switchLabel() != null) {
			node.switchLabel().stream().map(AbstractSyntaxTree::process)
				.forEach(result::addChild);
		}
		return result;
	}

	/**
	 * Process a switch block statement group.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(SwitchBlockStatementGroupContext node) {
		SwitchBlockGroup result = new SwitchBlockGroup();
		result.setType(Type.voidType());
		node.switchLabel().stream().map(AbstractSyntaxTree::process)
			.forEach(result::addChild);
		node.blockStatements().blockStatement().stream()
			.map(AbstractSyntaxTree::process).forEach(result::addChild);
		return result;
	}

	/**
	 * Process a switch label.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(SwitchLabelContext node) {
		SwitchLabel result = new SwitchLabel();
		result.setType(Type.voidType());
		if (node.expression() != null) {
			result.addChild(AbstractSyntaxTree.process(node.expression()));
		}
		else {
			result.setDefault(true);
		}
		return result;
	}

	/**
	 * Process a switch statement.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(SwitchStatementContext node) {
		SwitchStatement result = new SwitchStatement();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.expression()));
		result.addChild(AbstractSyntaxTree.process(node.switchBlock()));
		return result;
	}

	/**
	 * Process a unary expression.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(UnaryExpressionContext node) {
		if (node.preIncrementExpression() != null) {
			return AbstractSyntaxTree.process(node.preIncrementExpression());
		}
		if (node.preDecrementExpression() != null) {
			return AbstractSyntaxTree.process(node.preDecrementExpression());
		}
		if (node.unaryExpression() != null) {
			if (node.ADD() != null) {
				// ignore unary plus prefix
				return AbstractSyntaxTree.process(node.unaryExpression());
			}
			ExprArithmetic result = new ExprArithmetic();
			if (node.SUB() != null) {
				result.setOperator(ExprArithmetic.Operator.SUB);
			}
			else {
				AbstractSyntaxTree.log.warn(SafeResourceLoader.getString(
					AbstractSyntaxTree.UNKNOWN_UNARY_EXPRESSION,
					ScriptManager.getResourceBundle()), node.getText());
			}
			result.addChild(AbstractSyntaxTree.process(node.unaryExpression()));
			return result;
		}
		if (node.unaryExpressionNotPlusMinus() != null) {
			return AbstractSyntaxTree
				.process(node.unaryExpressionNotPlusMinus());
		}

		AbstractSyntaxTree.log.warn(SafeResourceLoader.getString(
			AbstractSyntaxTree.UNKNOWN_UNARY_EXPRESSION,
			ScriptManager.getResourceBundle()), node.getText());

		return null;
	}

	/**
	 * Process a unary expression that is not plus or minus.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(UnaryExpressionNotPlusMinusContext node) {
		if (node.postfixExpression() != null) {
			return AbstractSyntaxTree.process(node.postfixExpression());
		}
		if (node.unaryExpression() != null) {
			return AbstractSyntaxTree.process(node.unaryExpression());
		}
		if (node.castExpression() != null) {
			return AbstractSyntaxTree.process(node.castExpression());
		}

		AbstractSyntaxTree.log.warn(SafeResourceLoader.getString(
			AbstractSyntaxTree.UNKNOWN_UNARY_EXPRESSION,
			ScriptManager.getResourceBundle()), node.getText());
		return null;
	}

	/**
	 * Process a while loop.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(WhileStatementContext node) {
		While result = new While();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.expression()));
		result.addChild(AbstractSyntaxTree.process(node.statement()));
		return result;
	}

	/**
	 * Process a while loop.
	 *
	 * @param node The context to parse.
	 * @return The parsed version of the node.
	 */
	private static Node process(WhileStatementNoShortIfContext node) {
		While result = new While();
		result.setType(Type.voidType());
		result.addChild(AbstractSyntaxTree.process(node.expression()));
		result.addChild(AbstractSyntaxTree.process(node.statementNoShortIf()));
		return result;
	}

	/**
	 * Private constructor so that this class is not instantiated.
	 */
	private AbstractSyntaxTree() {
		throw new UnsupportedOperationException(
			"This utility class should not be instantiated");
	}

}
