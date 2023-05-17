package com.ikalagaming.scripting.ast;

/**
 * Used to visit nodes of the abstract syntax tree and perform operations on
 * them.
 *
 * @author Ches Burks
 *
 */
public interface ASTVisitor {
	/**
	 * Visit the ArgumentList node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ArgumentList node) {}

	/**
	 * Visit the ArrayAccess node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ArrayAccess node) {}

	/**
	 * Visit the Block node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Block node) {}

	/**
	 * Visit the Break node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Break node) {}

	/**
	 * Visit the Call node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Call node) {}

	/**
	 * Visit the Cast node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Cast node) {}

	/**
	 * Visit the CompilationUnit node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(CompilationUnit node) {}

	/**
	 * Visit the ConstBool node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ConstBool node) {}

	/**
	 * Visit the ConstChar node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ConstChar node) {}

	/**
	 * Visit the ConstDouble node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ConstDouble node) {}

	/**
	 * Visit the ConstInt node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ConstInt node) {}

	/**
	 * Visit the ConstNull node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ConstNull node) {}

	/**
	 * Visit the ConstString node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ConstString node) {}

	/**
	 * Visit the Continue node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Continue node) {}

	/**
	 * Visit the DoWhile node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(DoWhile node) {}

	/**
	 * Visit the Exit node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Exit node) {}

	/**
	 * Visit the ExprArithmetic node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ExprArithmetic node) {}

	/**
	 * Visit the ExprAssign node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ExprAssign node) {}

	/**
	 * Visit the ExprEquality node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ExprEquality node) {}

	/**
	 * Visit the ExprLogic node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ExprLogic node) {}

	/**
	 * Visit the ExprRelation node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ExprRelation node) {}

	/**
	 * Visit the ExprTernary node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ExprTernary node) {}

	/**
	 * Visit the FieldAccess node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(FieldAccess node) {}

	/**
	 * Visit the ForLoop node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(ForLoop node) {}

	/**
	 * Visit the Goto node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Goto node) {}

	/**
	 * Visit the Identifier node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Identifier node) {}

	/**
	 * Visit the If node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(If node) {}

	/**
	 * Visit the Label node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(Label node) {}

	/**
	 * Visit the LabeledStatement node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(LabeledStatement node) {}

	/**
	 * Visit the StatementList node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(StatementList node) {}

	/**
	 * Visit the SwitchBlockGroup node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(SwitchBlockGroup node) {}

	/**
	 * Visit the SwitchLabel node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(SwitchLabel node) {}

	/**
	 * Visit the SwitchStatement node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(SwitchStatement node) {}

	/**
	 * Visit the TypeNode node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(TypeNode node) {}

	/**
	 * Visit the VarDeclaration node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(VarDeclaration node) {}

	/**
	 * Visit the VarDeclarationList node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(VarDeclarationList node) {}

	/**
	 * Visit the While node and perform an operation.
	 *
	 * @param node The node we are visiting.
	 */
	default void visit(While node) {}
}
