// Generated from IkalaScriptParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IkalaScriptParser}.
 */
public interface IkalaScriptParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(IkalaScriptParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(IkalaScriptParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(IkalaScriptParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(IkalaScriptParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#numericType}.
	 * @param ctx the parse tree
	 */
	void enterNumericType(IkalaScriptParser.NumericTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#numericType}.
	 * @param ctx the parse tree
	 */
	void exitNumericType(IkalaScriptParser.NumericTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#referenceType}.
	 * @param ctx the parse tree
	 */
	void enterReferenceType(IkalaScriptParser.ReferenceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#referenceType}.
	 * @param ctx the parse tree
	 */
	void exitReferenceType(IkalaScriptParser.ReferenceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceType(IkalaScriptParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceType(IkalaScriptParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(IkalaScriptParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(IkalaScriptParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#dims}.
	 * @param ctx the parse tree
	 */
	void enterDims(IkalaScriptParser.DimsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#dims}.
	 * @param ctx the parse tree
	 */
	void exitDims(IkalaScriptParser.DimsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#variableDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorList(IkalaScriptParser.VariableDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#variableDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorList(IkalaScriptParser.VariableDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(IkalaScriptParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(IkalaScriptParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(IkalaScriptParser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(IkalaScriptParser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(IkalaScriptParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(IkalaScriptParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(IkalaScriptParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(IkalaScriptParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(IkalaScriptParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(IkalaScriptParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#blockStatements}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatements(IkalaScriptParser.BlockStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#blockStatements}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatements(IkalaScriptParser.BlockStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(IkalaScriptParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(IkalaScriptParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(IkalaScriptParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(IkalaScriptParser.LocalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(IkalaScriptParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(IkalaScriptParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#statementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterStatementNoShortIf(IkalaScriptParser.StatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#statementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitStatementNoShortIf(IkalaScriptParser.StatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#statementWithoutTrailingSubstatement}.
	 * @param ctx the parse tree
	 */
	void enterStatementWithoutTrailingSubstatement(IkalaScriptParser.StatementWithoutTrailingSubstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#statementWithoutTrailingSubstatement}.
	 * @param ctx the parse tree
	 */
	void exitStatementWithoutTrailingSubstatement(IkalaScriptParser.StatementWithoutTrailingSubstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(IkalaScriptParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(IkalaScriptParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void enterLabeledStatement(IkalaScriptParser.LabeledStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void exitLabeledStatement(IkalaScriptParser.LabeledStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#labeledStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterLabeledStatementNoShortIf(IkalaScriptParser.LabeledStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#labeledStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitLabeledStatementNoShortIf(IkalaScriptParser.LabeledStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpression(IkalaScriptParser.StatementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpression(IkalaScriptParser.StatementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#ifThenStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfThenStatement(IkalaScriptParser.IfThenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#ifThenStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfThenStatement(IkalaScriptParser.IfThenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#ifThenElseStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElseStatement(IkalaScriptParser.IfThenElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#ifThenElseStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElseStatement(IkalaScriptParser.IfThenElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#ifThenElseStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElseStatementNoShortIf(IkalaScriptParser.IfThenElseStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#ifThenElseStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElseStatementNoShortIf(IkalaScriptParser.IfThenElseStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(IkalaScriptParser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(IkalaScriptParser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#switchBlock}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlock(IkalaScriptParser.SwitchBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#switchBlock}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlock(IkalaScriptParser.SwitchBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(IkalaScriptParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(IkalaScriptParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(IkalaScriptParser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(IkalaScriptParser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(IkalaScriptParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(IkalaScriptParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#whileStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatementNoShortIf(IkalaScriptParser.WhileStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#whileStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatementNoShortIf(IkalaScriptParser.WhileStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoStatement(IkalaScriptParser.DoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoStatement(IkalaScriptParser.DoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(IkalaScriptParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(IkalaScriptParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#forStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterForStatementNoShortIf(IkalaScriptParser.ForStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#forStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitForStatementNoShortIf(IkalaScriptParser.ForStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#basicForStatement}.
	 * @param ctx the parse tree
	 */
	void enterBasicForStatement(IkalaScriptParser.BasicForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#basicForStatement}.
	 * @param ctx the parse tree
	 */
	void exitBasicForStatement(IkalaScriptParser.BasicForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#basicForStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterBasicForStatementNoShortIf(IkalaScriptParser.BasicForStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#basicForStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitBasicForStatementNoShortIf(IkalaScriptParser.BasicForStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(IkalaScriptParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(IkalaScriptParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#statementExpressionList}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpressionList(IkalaScriptParser.StatementExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#statementExpressionList}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpressionList(IkalaScriptParser.StatementExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#enhancedForStatement}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForStatement(IkalaScriptParser.EnhancedForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#enhancedForStatement}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForStatement(IkalaScriptParser.EnhancedForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#enhancedForStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForStatementNoShortIf(IkalaScriptParser.EnhancedForStatementNoShortIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#enhancedForStatementNoShortIf}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForStatementNoShortIf(IkalaScriptParser.EnhancedForStatementNoShortIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(IkalaScriptParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(IkalaScriptParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(IkalaScriptParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(IkalaScriptParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#gotoStatement}.
	 * @param ctx the parse tree
	 */
	void enterGotoStatement(IkalaScriptParser.GotoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#gotoStatement}.
	 * @param ctx the parse tree
	 */
	void exitGotoStatement(IkalaScriptParser.GotoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#exitStatement}.
	 * @param ctx the parse tree
	 */
	void enterExitStatement(IkalaScriptParser.ExitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#exitStatement}.
	 * @param ctx the parse tree
	 */
	void exitExitStatement(IkalaScriptParser.ExitStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(IkalaScriptParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(IkalaScriptParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#arrayAccess_LHS_General}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess_LHS_General(IkalaScriptParser.ArrayAccess_LHS_GeneralContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#arrayAccess_LHS_General}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess_LHS_General(IkalaScriptParser.ArrayAccess_LHS_GeneralContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primary_extension}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_extension(IkalaScriptParser.Primary_extensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primary_extension}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_extension(IkalaScriptParser.Primary_extensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primary_extension_access}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_extension_access(IkalaScriptParser.Primary_extension_accessContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primary_extension_access}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_extension_access(IkalaScriptParser.Primary_extension_accessContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primary_LHS}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_LHS(IkalaScriptParser.Primary_LHSContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primary_LHS}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_LHS(IkalaScriptParser.Primary_LHSContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#primary_LHS_access}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_LHS_access(IkalaScriptParser.Primary_LHS_accessContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#primary_LHS_access}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_LHS_access(IkalaScriptParser.Primary_LHS_accessContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void enterFieldAccess(IkalaScriptParser.FieldAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void exitFieldAccess(IkalaScriptParser.FieldAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#fieldAccess_extension}.
	 * @param ctx the parse tree
	 */
	void enterFieldAccess_extension(IkalaScriptParser.FieldAccess_extensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#fieldAccess_extension}.
	 * @param ctx the parse tree
	 */
	void exitFieldAccess_extension(IkalaScriptParser.FieldAccess_extensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#arrayAccess}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess(IkalaScriptParser.ArrayAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#arrayAccess}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess(IkalaScriptParser.ArrayAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#arrayAccess_extension}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess_extension(IkalaScriptParser.ArrayAccess_extensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#arrayAccess_extension}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess_extension(IkalaScriptParser.ArrayAccess_extensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#arrayAccess_LHS}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess_LHS(IkalaScriptParser.ArrayAccess_LHSContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#arrayAccess_LHS}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess_LHS(IkalaScriptParser.ArrayAccess_LHSContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#methodInvocation}.
	 * @param ctx the parse tree
	 */
	void enterMethodInvocation(IkalaScriptParser.MethodInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#methodInvocation}.
	 * @param ctx the parse tree
	 */
	void exitMethodInvocation(IkalaScriptParser.MethodInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#methodInvocation_extension}.
	 * @param ctx the parse tree
	 */
	void enterMethodInvocation_extension(IkalaScriptParser.MethodInvocation_extensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#methodInvocation_extension}.
	 * @param ctx the parse tree
	 */
	void exitMethodInvocation_extension(IkalaScriptParser.MethodInvocation_extensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#methodInvocation_LHS}.
	 * @param ctx the parse tree
	 */
	void enterMethodInvocation_LHS(IkalaScriptParser.MethodInvocation_LHSContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#methodInvocation_LHS}.
	 * @param ctx the parse tree
	 */
	void exitMethodInvocation_LHS(IkalaScriptParser.MethodInvocation_LHSContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(IkalaScriptParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(IkalaScriptParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(IkalaScriptParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(IkalaScriptParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(IkalaScriptParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(IkalaScriptParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#leftHandSide}.
	 * @param ctx the parse tree
	 */
	void enterLeftHandSide(IkalaScriptParser.LeftHandSideContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#leftHandSide}.
	 * @param ctx the parse tree
	 */
	void exitLeftHandSide(IkalaScriptParser.LeftHandSideContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(IkalaScriptParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(IkalaScriptParser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(IkalaScriptParser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(IkalaScriptParser.ConditionalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalOrExpression(IkalaScriptParser.ConditionalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalOrExpression(IkalaScriptParser.ConditionalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalAndExpression(IkalaScriptParser.ConditionalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalAndExpression(IkalaScriptParser.ConditionalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(IkalaScriptParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(IkalaScriptParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(IkalaScriptParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(IkalaScriptParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(IkalaScriptParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(IkalaScriptParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(IkalaScriptParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(IkalaScriptParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(IkalaScriptParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(IkalaScriptParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#preIncrementExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreIncrementExpression(IkalaScriptParser.PreIncrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#preIncrementExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreIncrementExpression(IkalaScriptParser.PreIncrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#preDecrementExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreDecrementExpression(IkalaScriptParser.PreDecrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#preDecrementExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreDecrementExpression(IkalaScriptParser.PreDecrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionNotPlusMinus(IkalaScriptParser.UnaryExpressionNotPlusMinusContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionNotPlusMinus(IkalaScriptParser.UnaryExpressionNotPlusMinusContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(IkalaScriptParser.PostfixExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(IkalaScriptParser.PostfixExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#postIncrementExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostIncrementExpression(IkalaScriptParser.PostIncrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#postIncrementExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostIncrementExpression(IkalaScriptParser.PostIncrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#postDecrementExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostDecrementExpression(IkalaScriptParser.PostDecrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#postDecrementExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostDecrementExpression(IkalaScriptParser.PostDecrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IkalaScriptParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(IkalaScriptParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IkalaScriptParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(IkalaScriptParser.CastExpressionContext ctx);
}