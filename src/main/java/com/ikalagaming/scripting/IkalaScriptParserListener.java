package com.ikalagaming.scripting;

// Generated from IkalaScriptParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IkalaScriptParser}.
 */
public interface IkalaScriptParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#additiveExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(
		IkalaScriptParser.AdditiveExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#argumentList}.
	 *
	 * @param ctx the parse tree
	 */
	void enterArgumentList(IkalaScriptParser.ArgumentListContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#arrayType}.
	 *
	 * @param ctx the parse tree
	 */
	void enterArrayType(IkalaScriptParser.ArrayTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#assignment}.
	 *
	 * @param ctx the parse tree
	 */
	void enterAssignment(IkalaScriptParser.AssignmentContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#assignmentOperator}.
	 *
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(
		IkalaScriptParser.AssignmentOperatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#block}.
	 *
	 * @param ctx the parse tree
	 */
	void enterBlock(IkalaScriptParser.BlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#blockStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(IkalaScriptParser.BlockStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#blockStatements}.
	 *
	 * @param ctx the parse tree
	 */
	void enterBlockStatements(IkalaScriptParser.BlockStatementsContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#breakStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(IkalaScriptParser.BreakStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#castExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterCastExpression(IkalaScriptParser.CastExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#classOrInterfaceType}.
	 *
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceType(
		IkalaScriptParser.ClassOrInterfaceTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#compilationUnit}.
	 *
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(IkalaScriptParser.CompilationUnitContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#conditionalAndExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterConditionalAndExpression(
		IkalaScriptParser.ConditionalAndExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#conditionalExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(
		IkalaScriptParser.ConditionalExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#conditionalOrExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterConditionalOrExpression(
		IkalaScriptParser.ConditionalOrExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#continueStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(IkalaScriptParser.ContinueStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#dims}.
	 *
	 * @param ctx the parse tree
	 */
	void enterDims(IkalaScriptParser.DimsContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#doStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterDoStatement(IkalaScriptParser.DoStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#emptyStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(IkalaScriptParser.EmptyStatementContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#equalityExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(
		IkalaScriptParser.EqualityExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#exitStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterExitStatement(IkalaScriptParser.ExitStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#expression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterExpression(IkalaScriptParser.ExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#expressionStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(
		IkalaScriptParser.ExpressionStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#forInit}.
	 *
	 * @param ctx the parse tree
	 */
	void enterForInit(IkalaScriptParser.ForInitContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#forStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterForStatement(IkalaScriptParser.ForStatementContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#forStatementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void enterForStatementNoShortIf(
		IkalaScriptParser.ForStatementNoShortIfContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#gotoStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterGotoStatement(IkalaScriptParser.GotoStatementContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#ifThenElseStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterIfThenElseStatement(
		IkalaScriptParser.IfThenElseStatementContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#ifThenElseStatementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void enterIfThenElseStatementNoShortIf(
		IkalaScriptParser.IfThenElseStatementNoShortIfContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#ifThenStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterIfThenStatement(IkalaScriptParser.IfThenStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#label}.
	 *
	 * @param ctx the parse tree
	 */
	void enterLabel(IkalaScriptParser.LabelContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#labeledStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterLabeledStatement(IkalaScriptParser.LabeledStatementContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#labeledStatementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void enterLabeledStatementNoShortIf(
		IkalaScriptParser.LabeledStatementNoShortIfContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#leftHandSide}.
	 *
	 * @param ctx the parse tree
	 */
	void enterLeftHandSide(IkalaScriptParser.LeftHandSideContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#literal}.
	 *
	 * @param ctx the parse tree
	 */
	void enterLiteral(IkalaScriptParser.LiteralContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#localVariableDeclaration}.
	 *
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(
		IkalaScriptParser.LocalVariableDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#localVariableDeclarationStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclarationStatement(
		IkalaScriptParser.LocalVariableDeclarationStatementContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#methodInvocation}.
	 *
	 * @param ctx the parse tree
	 */
	void enterMethodInvocation(IkalaScriptParser.MethodInvocationContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#methodInvocation_extension}.
	 *
	 * @param ctx the parse tree
	 */
	void enterMethodInvocation_extension(
		IkalaScriptParser.MethodInvocation_extensionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#methodInvocation_LHS}.
	 *
	 * @param ctx the parse tree
	 */
	void enterMethodInvocation_LHS(
		IkalaScriptParser.MethodInvocation_LHSContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#multiplicativeExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(
		IkalaScriptParser.MultiplicativeExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#numericType}.
	 *
	 * @param ctx the parse tree
	 */
	void enterNumericType(IkalaScriptParser.NumericTypeContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#postDecrementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPostDecrementExpression(
		IkalaScriptParser.PostDecrementExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#postfixExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(IkalaScriptParser.PostfixExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#postIncrementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPostIncrementExpression(
		IkalaScriptParser.PostIncrementExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#preDecrementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPreDecrementExpression(
		IkalaScriptParser.PreDecrementExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#preIncrementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPreIncrementExpression(
		IkalaScriptParser.PreIncrementExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primary}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPrimary(IkalaScriptParser.PrimaryContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#primary_extension}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPrimary_extension(IkalaScriptParser.Primary_extensionContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primary_LHS}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPrimary_LHS(IkalaScriptParser.Primary_LHSContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#primary_LHS_access}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPrimary_LHS_access(
		IkalaScriptParser.Primary_LHS_accessContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primitiveType}.
	 *
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(IkalaScriptParser.PrimitiveTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#referenceType}.
	 *
	 * @param ctx the parse tree
	 */
	void enterReferenceType(IkalaScriptParser.ReferenceTypeContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#relationalExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(
		IkalaScriptParser.RelationalExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#statement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterStatement(IkalaScriptParser.StatementContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#statementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterStatementExpression(
		IkalaScriptParser.StatementExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#statementExpressionList}.
	 *
	 * @param ctx the parse tree
	 */
	void enterStatementExpressionList(
		IkalaScriptParser.StatementExpressionListContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#statementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void enterStatementNoShortIf(
		IkalaScriptParser.StatementNoShortIfContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#statementWithoutTrailingSubstatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterStatementWithoutTrailingSubstatement(
		IkalaScriptParser.StatementWithoutTrailingSubstatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#switchBlock}.
	 *
	 * @param ctx the parse tree
	 */
	void enterSwitchBlock(IkalaScriptParser.SwitchBlockContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#switchBlockStatementGroup}.
	 *
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(
		IkalaScriptParser.SwitchBlockStatementGroupContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#switchLabel}.
	 *
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(IkalaScriptParser.SwitchLabelContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#switchStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(IkalaScriptParser.SwitchStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#type}.
	 *
	 * @param ctx the parse tree
	 */
	void enterType(IkalaScriptParser.TypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#unaryExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(IkalaScriptParser.UnaryExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#unaryExpressionNotPlusMinus}.
	 *
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionNotPlusMinus(
		IkalaScriptParser.UnaryExpressionNotPlusMinusContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#variableDeclarator}.
	 *
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(
		IkalaScriptParser.VariableDeclaratorContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#variableDeclaratorId}.
	 *
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(
		IkalaScriptParser.VariableDeclaratorIdContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#variableDeclaratorList}.
	 *
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorList(
		IkalaScriptParser.VariableDeclaratorListContext ctx);

	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#whileStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(IkalaScriptParser.WhileStatementContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link IkalaScriptParser#whileStatementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void enterWhileStatementNoShortIf(
		IkalaScriptParser.WhileStatementNoShortIfContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#additiveExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(
		IkalaScriptParser.AdditiveExpressionContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#argumentList}.
	 *
	 * @param ctx the parse tree
	 */
	void exitArgumentList(IkalaScriptParser.ArgumentListContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#arrayType}.
	 *
	 * @param ctx the parse tree
	 */
	void exitArrayType(IkalaScriptParser.ArrayTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#assignment}.
	 *
	 * @param ctx the parse tree
	 */
	void exitAssignment(IkalaScriptParser.AssignmentContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#assignmentOperator}.
	 *
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(
		IkalaScriptParser.AssignmentOperatorContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#block}.
	 *
	 * @param ctx the parse tree
	 */
	void exitBlock(IkalaScriptParser.BlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#blockStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(IkalaScriptParser.BlockStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#blockStatements}.
	 *
	 * @param ctx the parse tree
	 */
	void exitBlockStatements(IkalaScriptParser.BlockStatementsContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#breakStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(IkalaScriptParser.BreakStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#castExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitCastExpression(IkalaScriptParser.CastExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#classOrInterfaceType}.
	 *
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceType(
		IkalaScriptParser.ClassOrInterfaceTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#compilationUnit}.
	 *
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(IkalaScriptParser.CompilationUnitContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#conditionalAndExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitConditionalAndExpression(
		IkalaScriptParser.ConditionalAndExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#conditionalExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(
		IkalaScriptParser.ConditionalExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#conditionalOrExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitConditionalOrExpression(
		IkalaScriptParser.ConditionalOrExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#continueStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(IkalaScriptParser.ContinueStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#dims}.
	 *
	 * @param ctx the parse tree
	 */
	void exitDims(IkalaScriptParser.DimsContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#doStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitDoStatement(IkalaScriptParser.DoStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#emptyStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(IkalaScriptParser.EmptyStatementContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#equalityExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(
		IkalaScriptParser.EqualityExpressionContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#exitStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitExitStatement(IkalaScriptParser.ExitStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#expression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitExpression(IkalaScriptParser.ExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#expressionStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(
		IkalaScriptParser.ExpressionStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#forInit}.
	 *
	 * @param ctx the parse tree
	 */
	void exitForInit(IkalaScriptParser.ForInitContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#forStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitForStatement(IkalaScriptParser.ForStatementContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#forStatementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void exitForStatementNoShortIf(
		IkalaScriptParser.ForStatementNoShortIfContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#gotoStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitGotoStatement(IkalaScriptParser.GotoStatementContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#ifThenElseStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitIfThenElseStatement(
		IkalaScriptParser.IfThenElseStatementContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#ifThenElseStatementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void exitIfThenElseStatementNoShortIf(
		IkalaScriptParser.IfThenElseStatementNoShortIfContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#ifThenStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitIfThenStatement(IkalaScriptParser.IfThenStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#label}.
	 *
	 * @param ctx the parse tree
	 */
	void exitLabel(IkalaScriptParser.LabelContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#labeledStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitLabeledStatement(IkalaScriptParser.LabeledStatementContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#labeledStatementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void exitLabeledStatementNoShortIf(
		IkalaScriptParser.LabeledStatementNoShortIfContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#leftHandSide}.
	 *
	 * @param ctx the parse tree
	 */
	void exitLeftHandSide(IkalaScriptParser.LeftHandSideContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#literal}.
	 *
	 * @param ctx the parse tree
	 */
	void exitLiteral(IkalaScriptParser.LiteralContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#localVariableDeclaration}.
	 *
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(
		IkalaScriptParser.LocalVariableDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#localVariableDeclarationStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclarationStatement(
		IkalaScriptParser.LocalVariableDeclarationStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#methodInvocation}.
	 *
	 * @param ctx the parse tree
	 */
	void exitMethodInvocation(IkalaScriptParser.MethodInvocationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#methodInvocation_extension}.
	 *
	 * @param ctx the parse tree
	 */
	void exitMethodInvocation_extension(
		IkalaScriptParser.MethodInvocation_extensionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#methodInvocation_LHS}.
	 *
	 * @param ctx the parse tree
	 */
	void exitMethodInvocation_LHS(
		IkalaScriptParser.MethodInvocation_LHSContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#multiplicativeExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(
		IkalaScriptParser.MultiplicativeExpressionContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#numericType}.
	 *
	 * @param ctx the parse tree
	 */
	void exitNumericType(IkalaScriptParser.NumericTypeContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#postDecrementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPostDecrementExpression(
		IkalaScriptParser.PostDecrementExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#postfixExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(IkalaScriptParser.PostfixExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#postIncrementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPostIncrementExpression(
		IkalaScriptParser.PostIncrementExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#preDecrementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPreDecrementExpression(
		IkalaScriptParser.PreDecrementExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#preIncrementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPreIncrementExpression(
		IkalaScriptParser.PreIncrementExpressionContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primary}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPrimary(IkalaScriptParser.PrimaryContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#primary_extension}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPrimary_extension(IkalaScriptParser.Primary_extensionContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primary_LHS}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPrimary_LHS(IkalaScriptParser.Primary_LHSContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#primary_LHS_access}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPrimary_LHS_access(
		IkalaScriptParser.Primary_LHS_accessContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primitiveType}.
	 *
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(IkalaScriptParser.PrimitiveTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#referenceType}.
	 *
	 * @param ctx the parse tree
	 */
	void exitReferenceType(IkalaScriptParser.ReferenceTypeContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#relationalExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(
		IkalaScriptParser.RelationalExpressionContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#statement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitStatement(IkalaScriptParser.StatementContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#statementExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitStatementExpression(
		IkalaScriptParser.StatementExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#statementExpressionList}.
	 *
	 * @param ctx the parse tree
	 */
	void exitStatementExpressionList(
		IkalaScriptParser.StatementExpressionListContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#statementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void exitStatementNoShortIf(
		IkalaScriptParser.StatementNoShortIfContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#statementWithoutTrailingSubstatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitStatementWithoutTrailingSubstatement(
		IkalaScriptParser.StatementWithoutTrailingSubstatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#switchBlock}.
	 *
	 * @param ctx the parse tree
	 */
	void exitSwitchBlock(IkalaScriptParser.SwitchBlockContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#switchBlockStatementGroup}.
	 *
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(
		IkalaScriptParser.SwitchBlockStatementGroupContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#switchLabel}.
	 *
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(IkalaScriptParser.SwitchLabelContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#switchStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(IkalaScriptParser.SwitchStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#type}.
	 *
	 * @param ctx the parse tree
	 */
	void exitType(IkalaScriptParser.TypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#unaryExpression}.
	 *
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(IkalaScriptParser.UnaryExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#unaryExpressionNotPlusMinus}.
	 *
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionNotPlusMinus(
		IkalaScriptParser.UnaryExpressionNotPlusMinusContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#variableDeclarator}.
	 *
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(
		IkalaScriptParser.VariableDeclaratorContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#variableDeclaratorId}.
	 *
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(
		IkalaScriptParser.VariableDeclaratorIdContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#variableDeclaratorList}.
	 *
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorList(
		IkalaScriptParser.VariableDeclaratorListContext ctx);

	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#whileStatement}.
	 *
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(IkalaScriptParser.WhileStatementContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link IkalaScriptParser#whileStatementNoShortIf}.
	 *
	 * @param ctx the parse tree
	 */
	void exitWhileStatementNoShortIf(
		IkalaScriptParser.WhileStatementNoShortIfContext ctx);
}