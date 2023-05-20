package com.ikalagaming.scripting;

// Generated from IkalaScriptParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast",
	"CheckReturnValue"})
public class IkalaScriptParser extends Parser {
	@SuppressWarnings("CheckReturnValue")
	public static class AdditiveExpressionContext extends ParserRuleContext {
		public AdditiveExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ADD() {
			return this.getToken(IkalaScriptParser.ADD, 0);
		}

		public AdditiveExpressionContext additiveExpression() {
			return this.getRuleContext(AdditiveExpressionContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterAdditiveExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitAdditiveExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_additiveExpression;
		}

		public MultiplicativeExpressionContext multiplicativeExpression() {
			return this.getRuleContext(MultiplicativeExpressionContext.class,
				0);
		}

		public TerminalNode SUB() {
			return this.getToken(IkalaScriptParser.SUB, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public ArgumentListContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public List<TerminalNode> COMMA() {
			return this.getTokens(IkalaScriptParser.COMMA);
		}

		public TerminalNode COMMA(int i) {
			return this.getToken(IkalaScriptParser.COMMA, i);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterArgumentList(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitArgumentList(this);
			}
		}

		public List<ExpressionContext> expression() {
			return this.getRuleContexts(ExpressionContext.class);
		}

		public ExpressionContext expression(int i) {
			return this.getRuleContext(ExpressionContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_argumentList;
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAccess_extensionContext extends ParserRuleContext {
		public ArrayAccess_extensionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterArrayAccess_extension(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitArrayAccess_extension(this);
			}
		}

		public List<ExpressionContext> expression() {
			return this.getRuleContexts(ExpressionContext.class);
		}

		public ExpressionContext expression(int i) {
			return this.getRuleContext(ExpressionContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_arrayAccess_extension;
		}

		public List<TerminalNode> LBRACK() {
			return this.getTokens(IkalaScriptParser.LBRACK);
		}

		public TerminalNode LBRACK(int i) {
			return this.getToken(IkalaScriptParser.LBRACK, i);
		}

		public Primary_extension_accessContext primary_extension_access() {
			return this.getRuleContext(Primary_extension_accessContext.class,
				0);
		}

		public List<TerminalNode> RBRACK() {
			return this.getTokens(IkalaScriptParser.RBRACK);
		}

		public TerminalNode RBRACK(int i) {
			return this.getToken(IkalaScriptParser.RBRACK, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAccess_LHS_GeneralContext
		extends ParserRuleContext {
		public ArrayAccess_LHS_GeneralContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterArrayAccess_LHS_General(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitArrayAccess_LHS_General(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_arrayAccess_LHS_General;
		}

		public LiteralContext literal() {
			return this.getRuleContext(LiteralContext.class, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public MethodInvocationContext methodInvocation() {
			return this.getRuleContext(MethodInvocationContext.class, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAccess_LHSContext extends ParserRuleContext {
		public ArrayAccess_LHSContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterArrayAccess_LHS(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitArrayAccess_LHS(this);
			}
		}

		public List<ExpressionContext> expression() {
			return this.getRuleContexts(ExpressionContext.class);
		}

		public ExpressionContext expression(int i) {
			return this.getRuleContext(ExpressionContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_arrayAccess_LHS;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public List<TerminalNode> LBRACK() {
			return this.getTokens(IkalaScriptParser.LBRACK);
		}

		public TerminalNode LBRACK(int i) {
			return this.getToken(IkalaScriptParser.LBRACK, i);
		}

		public Primary_LHS_accessContext primary_LHS_access() {
			return this.getRuleContext(Primary_LHS_accessContext.class, 0);
		}

		public List<TerminalNode> RBRACK() {
			return this.getTokens(IkalaScriptParser.RBRACK);
		}

		public TerminalNode RBRACK(int i) {
			return this.getToken(IkalaScriptParser.RBRACK, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAccessContext extends ParserRuleContext {
		public ArrayAccessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ArrayAccess_LHS_GeneralContext arrayAccess_LHS_General() {
			return this.getRuleContext(ArrayAccess_LHS_GeneralContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterArrayAccess(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitArrayAccess(this);
			}
		}

		public List<ExpressionContext> expression() {
			return this.getRuleContexts(ExpressionContext.class);
		}

		public ExpressionContext expression(int i) {
			return this.getRuleContext(ExpressionContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_arrayAccess;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public List<TerminalNode> LBRACK() {
			return this.getTokens(IkalaScriptParser.LBRACK);
		}

		public TerminalNode LBRACK(int i) {
			return this.getToken(IkalaScriptParser.LBRACK, i);
		}

		public List<TerminalNode> RBRACK() {
			return this.getTokens(IkalaScriptParser.RBRACK);
		}

		public TerminalNode RBRACK(int i) {
			return this.getToken(IkalaScriptParser.RBRACK, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayTypeContext extends ParserRuleContext {
		public ArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return this.getRuleContext(ClassOrInterfaceTypeContext.class, 0);
		}

		public DimsContext dims() {
			return this.getRuleContext(DimsContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterArrayType(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitArrayType(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_arrayType;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public PrimitiveTypeContext primitiveType() {
			return this.getRuleContext(PrimitiveTypeContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignmentContext extends ParserRuleContext {
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public AssignmentOperatorContext assignmentOperator() {
			return this.getRuleContext(AssignmentOperatorContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterAssignment(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitAssignment(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_assignment;
		}

		public LeftHandSideContext leftHandSide() {
			return this.getRuleContext(LeftHandSideContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignmentOperatorContext extends ParserRuleContext {
		public AssignmentOperatorContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ADD_ASSIGN() {
			return this.getToken(IkalaScriptParser.ADD_ASSIGN, 0);
		}

		public TerminalNode ASSIGN() {
			return this.getToken(IkalaScriptParser.ASSIGN, 0);
		}

		public TerminalNode DIV_ASSIGN() {
			return this.getToken(IkalaScriptParser.DIV_ASSIGN, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterAssignmentOperator(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitAssignmentOperator(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_assignmentOperator;
		}

		public TerminalNode MOD_ASSIGN() {
			return this.getToken(IkalaScriptParser.MOD_ASSIGN, 0);
		}

		public TerminalNode MUL_ASSIGN() {
			return this.getToken(IkalaScriptParser.MUL_ASSIGN, 0);
		}

		public TerminalNode SUB_ASSIGN() {
			return this.getToken(IkalaScriptParser.SUB_ASSIGN, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public BlockStatementsContext blockStatements() {
			return this.getRuleContext(BlockStatementsContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterBlock(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitBlock(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_block;
		}

		public TerminalNode LBRACE() {
			return this.getToken(IkalaScriptParser.LBRACE, 0);
		}

		public TerminalNode RBRACE() {
			return this.getToken(IkalaScriptParser.RBRACE, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementContext extends ParserRuleContext {
		public BlockStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterBlockStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitBlockStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_blockStatement;
		}

		public LabelContext label() {
			return this.getRuleContext(LabelContext.class, 0);
		}

		public LocalVariableDeclarationStatementContext
			localVariableDeclarationStatement() {
			return this.getRuleContext(
				LocalVariableDeclarationStatementContext.class, 0);
		}

		public StatementContext statement() {
			return this.getRuleContext(StatementContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementsContext extends ParserRuleContext {
		public BlockStatementsContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public List<BlockStatementContext> blockStatement() {
			return this.getRuleContexts(BlockStatementContext.class);
		}

		public BlockStatementContext blockStatement(int i) {
			return this.getRuleContext(BlockStatementContext.class, i);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterBlockStatements(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitBlockStatements(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_blockStatements;
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakStatementContext extends ParserRuleContext {
		public BreakStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode BREAK() {
			return this.getToken(IkalaScriptParser.BREAK, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterBreakStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitBreakStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_breakStatement;
		}

		public TerminalNode SEMICOLON() {
			return this.getToken(IkalaScriptParser.SEMICOLON, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CastExpressionContext extends ParserRuleContext {
		public CastExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterCastExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitCastExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_castExpression;
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public PrimitiveTypeContext primitiveType() {
			return this.getRuleContext(PrimitiveTypeContext.class, 0);
		}

		public ReferenceTypeContext referenceType() {
			return this.getRuleContext(ReferenceTypeContext.class, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public UnaryExpressionContext unaryExpression() {
			return this.getRuleContext(UnaryExpressionContext.class, 0);
		}

		public UnaryExpressionNotPlusMinusContext
			unaryExpressionNotPlusMinus() {
			return this.getRuleContext(UnaryExpressionNotPlusMinusContext.class,
				0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassOrInterfaceTypeContext extends ParserRuleContext {
		public ClassOrInterfaceTypeContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public List<TerminalNode> DOT() {
			return this.getTokens(IkalaScriptParser.DOT);
		}

		public TerminalNode DOT(int i) {
			return this.getToken(IkalaScriptParser.DOT, i);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterClassOrInterfaceType(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitClassOrInterfaceType(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_classOrInterfaceType;
		}

		public List<TerminalNode> Identifier() {
			return this.getTokens(IkalaScriptParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return this.getToken(IkalaScriptParser.Identifier, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompilationUnitContext extends ParserRuleContext {
		public CompilationUnitContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public List<BlockStatementContext> blockStatement() {
			return this.getRuleContexts(BlockStatementContext.class);
		}

		public BlockStatementContext blockStatement(int i) {
			return this.getRuleContext(BlockStatementContext.class, i);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterCompilationUnit(this);
			}
		}

		public TerminalNode EOF() {
			return this.getToken(Recognizer.EOF, 0);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitCompilationUnit(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_compilationUnit;
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalAndExpressionContext
		extends ParserRuleContext {
		public ConditionalAndExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode AND() {
			return this.getToken(IkalaScriptParser.AND, 0);
		}

		public ConditionalAndExpressionContext conditionalAndExpression() {
			return this.getRuleContext(ConditionalAndExpressionContext.class,
				0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterConditionalAndExpression(this);
			}
		}

		public EqualityExpressionContext equalityExpression() {
			return this.getRuleContext(EqualityExpressionContext.class, 0);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitConditionalAndExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_conditionalAndExpression;
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalExpressionContext extends ParserRuleContext {
		public ConditionalExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode COLON() {
			return this.getToken(IkalaScriptParser.COLON, 0);
		}

		public ConditionalExpressionContext conditionalExpression() {
			return this.getRuleContext(ConditionalExpressionContext.class, 0);
		}

		public ConditionalOrExpressionContext conditionalOrExpression() {
			return this.getRuleContext(ConditionalOrExpressionContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterConditionalExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitConditionalExpression(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_conditionalExpression;
		}

		public TerminalNode QUESTION() {
			return this.getToken(IkalaScriptParser.QUESTION, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalOrExpressionContext
		extends ParserRuleContext {
		public ConditionalOrExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public ConditionalAndExpressionContext conditionalAndExpression() {
			return this.getRuleContext(ConditionalAndExpressionContext.class,
				0);
		}

		public ConditionalOrExpressionContext conditionalOrExpression() {
			return this.getRuleContext(ConditionalOrExpressionContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterConditionalOrExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitConditionalOrExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_conditionalOrExpression;
		}

		public TerminalNode OR() {
			return this.getToken(IkalaScriptParser.OR, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends ParserRuleContext {
		public ContinueStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode CONTINUE() {
			return this.getToken(IkalaScriptParser.CONTINUE, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterContinueStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitContinueStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_continueStatement;
		}

		public TerminalNode SEMICOLON() {
			return this.getToken(IkalaScriptParser.SEMICOLON, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DimsContext extends ParserRuleContext {
		public DimsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterDims(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitDims(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_dims;
		}

		public List<TerminalNode> LBRACK() {
			return this.getTokens(IkalaScriptParser.LBRACK);
		}

		public TerminalNode LBRACK(int i) {
			return this.getToken(IkalaScriptParser.LBRACK, i);
		}

		public List<TerminalNode> RBRACK() {
			return this.getTokens(IkalaScriptParser.RBRACK);
		}

		public TerminalNode RBRACK(int i) {
			return this.getToken(IkalaScriptParser.RBRACK, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DoStatementContext extends ParserRuleContext {
		public DoStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DO() {
			return this.getToken(IkalaScriptParser.DO, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterDoStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitDoStatement(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_doStatement;
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public TerminalNode SEMICOLON() {
			return this.getToken(IkalaScriptParser.SEMICOLON, 0);
		}

		public StatementContext statement() {
			return this.getRuleContext(StatementContext.class, 0);
		}

		public TerminalNode WHILE() {
			return this.getToken(IkalaScriptParser.WHILE, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptyStatementContext extends ParserRuleContext {
		public EmptyStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterEmptyStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitEmptyStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_emptyStatement;
		}

		public TerminalNode SEMICOLON() {
			return this.getToken(IkalaScriptParser.SEMICOLON, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EqualityExpressionContext extends ParserRuleContext {
		public EqualityExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterEqualityExpression(this);
			}
		}

		public TerminalNode EQUAL() {
			return this.getToken(IkalaScriptParser.EQUAL, 0);
		}

		public EqualityExpressionContext equalityExpression() {
			return this.getRuleContext(EqualityExpressionContext.class, 0);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitEqualityExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_equalityExpression;
		}

		public TerminalNode NOTEQUAL() {
			return this.getToken(IkalaScriptParser.NOTEQUAL, 0);
		}

		public RelationalExpressionContext relationalExpression() {
			return this.getRuleContext(RelationalExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExitStatementContext extends ParserRuleContext {
		public ExitStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterExitStatement(this);
			}
		}

		public TerminalNode EXIT() {
			return this.getToken(IkalaScriptParser.EXIT, 0);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitExitStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_exitStatement;
		}

		public TerminalNode SEMICOLON() {
			return this.getToken(IkalaScriptParser.SEMICOLON, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public AssignmentContext assignment() {
			return this.getRuleContext(AssignmentContext.class, 0);
		}

		public ConditionalExpressionContext conditionalExpression() {
			return this.getRuleContext(ConditionalExpressionContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_expression;
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionStatementContext extends ParserRuleContext {
		public ExpressionStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterExpressionStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitExpressionStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_expressionStatement;
		}

		public TerminalNode SEMICOLON() {
			return this.getToken(IkalaScriptParser.SEMICOLON, 0);
		}

		public StatementExpressionContext statementExpression() {
			return this.getRuleContext(StatementExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForInitContext extends ParserRuleContext {
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterForInit(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitForInit(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_forInit;
		}

		public LocalVariableDeclarationContext localVariableDeclaration() {
			return this.getRuleContext(LocalVariableDeclarationContext.class,
				0);
		}

		public StatementExpressionListContext statementExpressionList() {
			return this.getRuleContext(StatementExpressionListContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementContext extends ParserRuleContext {
		public ForStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterForStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitForStatement(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		public TerminalNode FOR() {
			return this.getToken(IkalaScriptParser.FOR, 0);
		}

		public ForInitContext forInit() {
			return this.getRuleContext(ForInitContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_forStatement;
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public List<TerminalNode> SEMICOLON() {
			return this.getTokens(IkalaScriptParser.SEMICOLON);
		}

		public TerminalNode SEMICOLON(int i) {
			return this.getToken(IkalaScriptParser.SEMICOLON, i);
		}

		public StatementContext statement() {
			return this.getRuleContext(StatementContext.class, 0);
		}

		public StatementExpressionListContext statementExpressionList() {
			return this.getRuleContext(StatementExpressionListContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementNoShortIfContext extends ParserRuleContext {
		public ForStatementNoShortIfContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterForStatementNoShortIf(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitForStatementNoShortIf(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		public TerminalNode FOR() {
			return this.getToken(IkalaScriptParser.FOR, 0);
		}

		public ForInitContext forInit() {
			return this.getRuleContext(ForInitContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_forStatementNoShortIf;
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public List<TerminalNode> SEMICOLON() {
			return this.getTokens(IkalaScriptParser.SEMICOLON);
		}

		public TerminalNode SEMICOLON(int i) {
			return this.getToken(IkalaScriptParser.SEMICOLON, i);
		}

		public StatementExpressionListContext statementExpressionList() {
			return this.getRuleContext(StatementExpressionListContext.class, 0);
		}

		public StatementNoShortIfContext statementNoShortIf() {
			return this.getRuleContext(StatementNoShortIfContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GotoStatementContext extends ParserRuleContext {
		public GotoStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterGotoStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitGotoStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_gotoStatement;
		}

		public TerminalNode GOTO() {
			return this.getToken(IkalaScriptParser.GOTO, 0);
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public TerminalNode SEMICOLON() {
			return this.getToken(IkalaScriptParser.SEMICOLON, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfThenElseStatementContext extends ParserRuleContext {
		public IfThenElseStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ELSE() {
			return this.getToken(IkalaScriptParser.ELSE, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterIfThenElseStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitIfThenElseStatement(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_ifThenElseStatement;
		}

		public TerminalNode IF() {
			return this.getToken(IkalaScriptParser.IF, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public StatementContext statement() {
			return this.getRuleContext(StatementContext.class, 0);
		}

		public StatementNoShortIfContext statementNoShortIf() {
			return this.getRuleContext(StatementNoShortIfContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfThenElseStatementNoShortIfContext
		extends ParserRuleContext {
		public IfThenElseStatementNoShortIfContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ELSE() {
			return this.getToken(IkalaScriptParser.ELSE, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterIfThenElseStatementNoShortIf(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitIfThenElseStatementNoShortIf(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_ifThenElseStatementNoShortIf;
		}

		public TerminalNode IF() {
			return this.getToken(IkalaScriptParser.IF, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public List<StatementNoShortIfContext> statementNoShortIf() {
			return this.getRuleContexts(StatementNoShortIfContext.class);
		}

		public StatementNoShortIfContext statementNoShortIf(int i) {
			return this.getRuleContext(StatementNoShortIfContext.class, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfThenStatementContext extends ParserRuleContext {
		public IfThenStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterIfThenStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitIfThenStatement(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_ifThenStatement;
		}

		public TerminalNode IF() {
			return this.getToken(IkalaScriptParser.IF, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public StatementContext statement() {
			return this.getRuleContext(StatementContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabelContext extends ParserRuleContext {
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode COLON() {
			return this.getToken(IkalaScriptParser.COLON, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterLabel(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitLabel(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_label;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabeledStatementContext extends ParserRuleContext {
		public LabeledStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterLabeledStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitLabeledStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_labeledStatement;
		}

		public LabelContext label() {
			return this.getRuleContext(LabelContext.class, 0);
		}

		public StatementContext statement() {
			return this.getRuleContext(StatementContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabeledStatementNoShortIfContext
		extends ParserRuleContext {
		public LabeledStatementNoShortIfContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterLabeledStatementNoShortIf(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitLabeledStatementNoShortIf(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_labeledStatementNoShortIf;
		}

		public LabelContext label() {
			return this.getRuleContext(LabelContext.class, 0);
		}

		public StatementNoShortIfContext statementNoShortIf() {
			return this.getRuleContext(StatementNoShortIfContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LeftHandSideContext extends ParserRuleContext {
		public LeftHandSideContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public ArrayAccessContext arrayAccess() {
			return this.getRuleContext(ArrayAccessContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterLeftHandSide(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitLeftHandSide(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_leftHandSide;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode BooleanLiteral() {
			return this.getToken(IkalaScriptParser.BooleanLiteral, 0);
		}

		public TerminalNode CharacterLiteral() {
			return this.getToken(IkalaScriptParser.CharacterLiteral, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterLiteral(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitLiteral(this);
			}
		}

		public TerminalNode FloatingPointLiteral() {
			return this.getToken(IkalaScriptParser.FloatingPointLiteral, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_literal;
		}

		public TerminalNode IntegerLiteral() {
			return this.getToken(IkalaScriptParser.IntegerLiteral, 0);
		}

		public TerminalNode NullLiteral() {
			return this.getToken(IkalaScriptParser.NullLiteral, 0);
		}

		public TerminalNode StringLiteral() {
			return this.getToken(IkalaScriptParser.StringLiteral, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LocalVariableDeclarationContext
		extends ParserRuleContext {
		public LocalVariableDeclarationContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterLocalVariableDeclaration(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitLocalVariableDeclaration(this);
			}
		}

		public TerminalNode FINAL() {
			return this.getToken(IkalaScriptParser.FINAL, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_localVariableDeclaration;
		}

		public TypeContext type() {
			return this.getRuleContext(TypeContext.class, 0);
		}

		public VariableDeclaratorListContext variableDeclaratorList() {
			return this.getRuleContext(VariableDeclaratorListContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LocalVariableDeclarationStatementContext
		extends ParserRuleContext {
		public LocalVariableDeclarationStatementContext(
			ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterLocalVariableDeclarationStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitLocalVariableDeclarationStatement(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_localVariableDeclarationStatement;
		}

		public LocalVariableDeclarationContext localVariableDeclaration() {
			return this.getRuleContext(LocalVariableDeclarationContext.class,
				0);
		}

		public TerminalNode SEMICOLON() {
			return this.getToken(IkalaScriptParser.SEMICOLON, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MethodInvocation_extensionContext
		extends ParserRuleContext {
		public MethodInvocation_extensionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public ArgumentListContext argumentList() {
			return this.getRuleContext(ArgumentListContext.class, 0);
		}

		public TerminalNode DOT() {
			return this.getToken(IkalaScriptParser.DOT, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterMethodInvocation_extension(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitMethodInvocation_extension(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_methodInvocation_extension;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MethodInvocation_LHSContext extends ParserRuleContext {
		public MethodInvocation_LHSContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public ArgumentListContext argumentList() {
			return this.getRuleContext(ArgumentListContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterMethodInvocation_LHS(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitMethodInvocation_LHS(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_methodInvocation_LHS;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MethodInvocationContext extends ParserRuleContext {
		public MethodInvocationContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public ArgumentListContext argumentList() {
			return this.getRuleContext(ArgumentListContext.class, 0);
		}

		public TerminalNode DOT() {
			return this.getToken(IkalaScriptParser.DOT, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterMethodInvocation(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitMethodInvocation(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_methodInvocation;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public PrimaryContext primary() {
			return this.getRuleContext(PrimaryContext.class, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicativeExpressionContext
		extends ParserRuleContext {
		public MultiplicativeExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DIV() {
			return this.getToken(IkalaScriptParser.DIV, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterMultiplicativeExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitMultiplicativeExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_multiplicativeExpression;
		}

		public TerminalNode MOD() {
			return this.getToken(IkalaScriptParser.MOD, 0);
		}

		public TerminalNode MUL() {
			return this.getToken(IkalaScriptParser.MUL, 0);
		}

		public MultiplicativeExpressionContext multiplicativeExpression() {
			return this.getRuleContext(MultiplicativeExpressionContext.class,
				0);
		}

		public UnaryExpressionContext unaryExpression() {
			return this.getRuleContext(UnaryExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NumericTypeContext extends ParserRuleContext {
		public NumericTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode CHAR() {
			return this.getToken(IkalaScriptParser.CHAR, 0);
		}

		public TerminalNode DOUBLE() {
			return this.getToken(IkalaScriptParser.DOUBLE, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterNumericType(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitNumericType(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_numericType;
		}

		public TerminalNode INT() {
			return this.getToken(IkalaScriptParser.INT, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostDecrementExpressionContext
		extends ParserRuleContext {
		public PostDecrementExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DEC() {
			return this.getToken(IkalaScriptParser.DEC, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterPostDecrementExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitPostDecrementExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_postDecrementExpression;
		}

		public PostfixExpressionContext postfixExpression() {
			return this.getRuleContext(PostfixExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostfixExpressionContext extends ParserRuleContext {
		public PostfixExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public List<TerminalNode> DEC() {
			return this.getTokens(IkalaScriptParser.DEC);
		}

		public TerminalNode DEC(int i) {
			return this.getToken(IkalaScriptParser.DEC, i);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterPostfixExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitPostfixExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_postfixExpression;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public List<TerminalNode> INC() {
			return this.getTokens(IkalaScriptParser.INC);
		}

		public TerminalNode INC(int i) {
			return this.getToken(IkalaScriptParser.INC, i);
		}

		public PrimaryContext primary() {
			return this.getRuleContext(PrimaryContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostIncrementExpressionContext
		extends ParserRuleContext {
		public PostIncrementExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterPostIncrementExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitPostIncrementExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_postIncrementExpression;
		}

		public TerminalNode INC() {
			return this.getToken(IkalaScriptParser.INC, 0);
		}

		public PostfixExpressionContext postfixExpression() {
			return this.getRuleContext(PostfixExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PreDecrementExpressionContext
		extends ParserRuleContext {
		public PreDecrementExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DEC() {
			return this.getToken(IkalaScriptParser.DEC, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterPreDecrementExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitPreDecrementExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_preDecrementExpression;
		}

		public UnaryExpressionContext unaryExpression() {
			return this.getRuleContext(UnaryExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PreIncrementExpressionContext
		extends ParserRuleContext {
		public PreIncrementExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterPreIncrementExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitPreIncrementExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_preIncrementExpression;
		}

		public TerminalNode INC() {
			return this.getToken(IkalaScriptParser.INC, 0);
		}

		public UnaryExpressionContext unaryExpression() {
			return this.getRuleContext(UnaryExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Primary_extension_accessContext
		extends ParserRuleContext {
		public Primary_extension_accessContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterPrimary_extension_access(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitPrimary_extension_access(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_primary_extension_access;
		}

		public MethodInvocation_extensionContext methodInvocation_extension() {
			return this.getRuleContext(MethodInvocation_extensionContext.class,
				0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Primary_extensionContext extends ParserRuleContext {
		public Primary_extensionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public ArrayAccess_extensionContext arrayAccess_extension() {
			return this.getRuleContext(ArrayAccess_extensionContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterPrimary_extension(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitPrimary_extension(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_primary_extension;
		}

		public MethodInvocation_extensionContext methodInvocation_extension() {
			return this.getRuleContext(MethodInvocation_extensionContext.class,
				0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Primary_LHS_accessContext extends ParserRuleContext {
		public Primary_LHS_accessContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterPrimary_LHS_access(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitPrimary_LHS_access(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_primary_LHS_access;
		}

		public LiteralContext literal() {
			return this.getRuleContext(LiteralContext.class, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public MethodInvocation_LHSContext methodInvocation_LHS() {
			return this.getRuleContext(MethodInvocation_LHSContext.class, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Primary_LHSContext extends ParserRuleContext {
		public Primary_LHSContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ArrayAccess_LHSContext arrayAccess_LHS() {
			return this.getRuleContext(ArrayAccess_LHSContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterPrimary_LHS(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitPrimary_LHS(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_primary_LHS;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}

		public LiteralContext literal() {
			return this.getRuleContext(LiteralContext.class, 0);
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public MethodInvocation_LHSContext methodInvocation_LHS() {
			return this.getRuleContext(MethodInvocation_LHSContext.class, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryContext extends ParserRuleContext {
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterPrimary(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitPrimary(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_primary;
		}

		public List<Primary_extensionContext> primary_extension() {
			return this.getRuleContexts(Primary_extensionContext.class);
		}

		public Primary_extensionContext primary_extension(int i) {
			return this.getRuleContext(Primary_extensionContext.class, i);
		}

		public Primary_LHSContext primary_LHS() {
			return this.getRuleContext(Primary_LHSContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode BOOLEAN() {
			return this.getToken(IkalaScriptParser.BOOLEAN, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterPrimitiveType(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitPrimitiveType(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_primitiveType;
		}

		public NumericTypeContext numericType() {
			return this.getRuleContext(NumericTypeContext.class, 0);
		}

		public TerminalNode STRING() {
			return this.getToken(IkalaScriptParser.STRING, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReferenceTypeContext extends ParserRuleContext {
		public ReferenceTypeContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public ArrayTypeContext arrayType() {
			return this.getRuleContext(ArrayTypeContext.class, 0);
		}

		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return this.getRuleContext(ClassOrInterfaceTypeContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterReferenceType(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitReferenceType(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_referenceType;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationalExpressionContext extends ParserRuleContext {
		public RelationalExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public AdditiveExpressionContext additiveExpression() {
			return this.getRuleContext(AdditiveExpressionContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterRelationalExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitRelationalExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_relationalExpression;
		}

		public TerminalNode GT() {
			return this.getToken(IkalaScriptParser.GT, 0);
		}

		public TerminalNode GTE() {
			return this.getToken(IkalaScriptParser.GTE, 0);
		}

		public TerminalNode LT() {
			return this.getToken(IkalaScriptParser.LT, 0);
		}

		public TerminalNode LTE() {
			return this.getToken(IkalaScriptParser.LTE, 0);
		}

		public RelationalExpressionContext relationalExpression() {
			return this.getRuleContext(RelationalExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitStatement(this);
			}
		}

		public ForStatementContext forStatement() {
			return this.getRuleContext(ForStatementContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_statement;
		}

		public IfThenElseStatementContext ifThenElseStatement() {
			return this.getRuleContext(IfThenElseStatementContext.class, 0);
		}

		public IfThenStatementContext ifThenStatement() {
			return this.getRuleContext(IfThenStatementContext.class, 0);
		}

		public LabeledStatementContext labeledStatement() {
			return this.getRuleContext(LabeledStatementContext.class, 0);
		}

		public StatementWithoutTrailingSubstatementContext
			statementWithoutTrailingSubstatement() {
			return this.getRuleContext(
				StatementWithoutTrailingSubstatementContext.class, 0);
		}

		public WhileStatementContext whileStatement() {
			return this.getRuleContext(WhileStatementContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementExpressionContext extends ParserRuleContext {
		public StatementExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public AssignmentContext assignment() {
			return this.getRuleContext(AssignmentContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterStatementExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitStatementExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_statementExpression;
		}

		public MethodInvocationContext methodInvocation() {
			return this.getRuleContext(MethodInvocationContext.class, 0);
		}

		public PostDecrementExpressionContext postDecrementExpression() {
			return this.getRuleContext(PostDecrementExpressionContext.class, 0);
		}

		public PostIncrementExpressionContext postIncrementExpression() {
			return this.getRuleContext(PostIncrementExpressionContext.class, 0);
		}

		public PreDecrementExpressionContext preDecrementExpression() {
			return this.getRuleContext(PreDecrementExpressionContext.class, 0);
		}

		public PreIncrementExpressionContext preIncrementExpression() {
			return this.getRuleContext(PreIncrementExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementExpressionListContext
		extends ParserRuleContext {
		public StatementExpressionListContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public List<TerminalNode> COMMA() {
			return this.getTokens(IkalaScriptParser.COMMA);
		}

		public TerminalNode COMMA(int i) {
			return this.getToken(IkalaScriptParser.COMMA, i);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterStatementExpressionList(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitStatementExpressionList(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_statementExpressionList;
		}

		public List<StatementExpressionContext> statementExpression() {
			return this.getRuleContexts(StatementExpressionContext.class);
		}

		public StatementExpressionContext statementExpression(int i) {
			return this.getRuleContext(StatementExpressionContext.class, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementNoShortIfContext extends ParserRuleContext {
		public StatementNoShortIfContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterStatementNoShortIf(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitStatementNoShortIf(this);
			}
		}

		public ForStatementNoShortIfContext forStatementNoShortIf() {
			return this.getRuleContext(ForStatementNoShortIfContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_statementNoShortIf;
		}

		public IfThenElseStatementNoShortIfContext
			ifThenElseStatementNoShortIf() {
			return this
				.getRuleContext(IfThenElseStatementNoShortIfContext.class, 0);
		}

		public LabeledStatementNoShortIfContext labeledStatementNoShortIf() {
			return this.getRuleContext(LabeledStatementNoShortIfContext.class,
				0);
		}

		public StatementWithoutTrailingSubstatementContext
			statementWithoutTrailingSubstatement() {
			return this.getRuleContext(
				StatementWithoutTrailingSubstatementContext.class, 0);
		}

		public WhileStatementNoShortIfContext whileStatementNoShortIf() {
			return this.getRuleContext(WhileStatementNoShortIfContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementWithoutTrailingSubstatementContext
		extends ParserRuleContext {
		public StatementWithoutTrailingSubstatementContext(
			ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public BlockContext block() {
			return this.getRuleContext(BlockContext.class, 0);
		}

		public BreakStatementContext breakStatement() {
			return this.getRuleContext(BreakStatementContext.class, 0);
		}

		public ContinueStatementContext continueStatement() {
			return this.getRuleContext(ContinueStatementContext.class, 0);
		}

		public DoStatementContext doStatement() {
			return this.getRuleContext(DoStatementContext.class, 0);
		}

		public EmptyStatementContext emptyStatement() {
			return this.getRuleContext(EmptyStatementContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterStatementWithoutTrailingSubstatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitStatementWithoutTrailingSubstatement(this);
			}
		}

		public ExitStatementContext exitStatement() {
			return this.getRuleContext(ExitStatementContext.class, 0);
		}

		public ExpressionStatementContext expressionStatement() {
			return this.getRuleContext(ExpressionStatementContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_statementWithoutTrailingSubstatement;
		}

		public GotoStatementContext gotoStatement() {
			return this.getRuleContext(GotoStatementContext.class, 0);
		}

		public SwitchStatementContext switchStatement() {
			return this.getRuleContext(SwitchStatementContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchBlockContext extends ParserRuleContext {
		public SwitchBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterSwitchBlock(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitSwitchBlock(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_switchBlock;
		}

		public TerminalNode LBRACE() {
			return this.getToken(IkalaScriptParser.LBRACE, 0);
		}

		public TerminalNode RBRACE() {
			return this.getToken(IkalaScriptParser.RBRACE, 0);
		}

		public List<SwitchBlockStatementGroupContext>
			switchBlockStatementGroup() {
			return this.getRuleContexts(SwitchBlockStatementGroupContext.class);
		}

		public SwitchBlockStatementGroupContext
			switchBlockStatementGroup(int i) {
			return this.getRuleContext(SwitchBlockStatementGroupContext.class,
				i);
		}

		public List<SwitchLabelContext> switchLabel() {
			return this.getRuleContexts(SwitchLabelContext.class);
		}

		public SwitchLabelContext switchLabel(int i) {
			return this.getRuleContext(SwitchLabelContext.class, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchBlockStatementGroupContext
		extends ParserRuleContext {
		public SwitchBlockStatementGroupContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public BlockStatementsContext blockStatements() {
			return this.getRuleContext(BlockStatementsContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterSwitchBlockStatementGroup(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitSwitchBlockStatementGroup(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_switchBlockStatementGroup;
		}

		public List<SwitchLabelContext> switchLabel() {
			return this.getRuleContexts(SwitchLabelContext.class);
		}

		public SwitchLabelContext switchLabel(int i) {
			return this.getRuleContext(SwitchLabelContext.class, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchLabelContext extends ParserRuleContext {
		public SwitchLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode CASE() {
			return this.getToken(IkalaScriptParser.CASE, 0);
		}

		public TerminalNode COLON() {
			return this.getToken(IkalaScriptParser.COLON, 0);
		}

		public TerminalNode DEFAULT() {
			return this.getToken(IkalaScriptParser.DEFAULT, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterSwitchLabel(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitSwitchLabel(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_switchLabel;
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchStatementContext extends ParserRuleContext {
		public SwitchStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterSwitchStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitSwitchStatement(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_switchStatement;
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public TerminalNode SWITCH() {
			return this.getToken(IkalaScriptParser.SWITCH, 0);
		}

		public SwitchBlockContext switchBlock() {
			return this.getRuleContext(SwitchBlockContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).enterType(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitType(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_type;
		}

		public PrimitiveTypeContext primitiveType() {
			return this.getRuleContext(PrimitiveTypeContext.class, 0);
		}

		public ReferenceTypeContext referenceType() {
			return this.getRuleContext(ReferenceTypeContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExpressionContext extends ParserRuleContext {
		public UnaryExpressionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ADD() {
			return this.getToken(IkalaScriptParser.ADD, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterUnaryExpression(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitUnaryExpression(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_unaryExpression;
		}

		public PreDecrementExpressionContext preDecrementExpression() {
			return this.getRuleContext(PreDecrementExpressionContext.class, 0);
		}

		public PreIncrementExpressionContext preIncrementExpression() {
			return this.getRuleContext(PreIncrementExpressionContext.class, 0);
		}

		public TerminalNode SUB() {
			return this.getToken(IkalaScriptParser.SUB, 0);
		}

		public UnaryExpressionContext unaryExpression() {
			return this.getRuleContext(UnaryExpressionContext.class, 0);
		}

		public UnaryExpressionNotPlusMinusContext
			unaryExpressionNotPlusMinus() {
			return this.getRuleContext(UnaryExpressionNotPlusMinusContext.class,
				0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExpressionNotPlusMinusContext
		extends ParserRuleContext {
		public UnaryExpressionNotPlusMinusContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public CastExpressionContext castExpression() {
			return this.getRuleContext(CastExpressionContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterUnaryExpressionNotPlusMinus(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitUnaryExpressionNotPlusMinus(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_unaryExpressionNotPlusMinus;
		}

		public TerminalNode NOT() {
			return this.getToken(IkalaScriptParser.NOT, 0);
		}

		public PostfixExpressionContext postfixExpression() {
			return this.getRuleContext(PostfixExpressionContext.class, 0);
		}

		public UnaryExpressionContext unaryExpression() {
			return this.getRuleContext(UnaryExpressionContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclaratorContext extends ParserRuleContext {
		public VariableDeclaratorContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ASSIGN() {
			return this.getToken(IkalaScriptParser.ASSIGN, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterVariableDeclarator(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitVariableDeclarator(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_variableDeclarator;
		}

		public VariableDeclaratorIdContext variableDeclaratorId() {
			return this.getRuleContext(VariableDeclaratorIdContext.class, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclaratorIdContext extends ParserRuleContext {
		public VariableDeclaratorIdContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public DimsContext dims() {
			return this.getRuleContext(DimsContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterVariableDeclaratorId(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitVariableDeclaratorId(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_variableDeclaratorId;
		}

		public TerminalNode Identifier() {
			return this.getToken(IkalaScriptParser.Identifier, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclaratorListContext
		extends ParserRuleContext {
		public VariableDeclaratorListContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		public List<TerminalNode> COMMA() {
			return this.getTokens(IkalaScriptParser.COMMA);
		}

		public TerminalNode COMMA(int i) {
			return this.getToken(IkalaScriptParser.COMMA, i);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterVariableDeclaratorList(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitVariableDeclaratorList(this);
			}
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_variableDeclaratorList;
		}

		public List<VariableDeclaratorContext> variableDeclarator() {
			return this.getRuleContexts(VariableDeclaratorContext.class);
		}

		public VariableDeclaratorContext variableDeclarator(int i) {
			return this.getRuleContext(VariableDeclaratorContext.class, i);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
		public WhileStatementContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterWhileStatement(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener).exitWhileStatement(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_whileStatement;
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public StatementContext statement() {
			return this.getRuleContext(StatementContext.class, 0);
		}

		public TerminalNode WHILE() {
			return this.getToken(IkalaScriptParser.WHILE, 0);
		}
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementNoShortIfContext
		extends ParserRuleContext {
		public WhileStatementNoShortIfContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.enterWhileStatementNoShortIf(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof IkalaScriptParserListener) {
				((IkalaScriptParserListener) listener)
					.exitWhileStatementNoShortIf(this);
			}
		}

		public ExpressionContext expression() {
			return this.getRuleContext(ExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return IkalaScriptParser.RULE_whileStatementNoShortIf;
		}

		public TerminalNode LPAREN() {
			return this.getToken(IkalaScriptParser.LPAREN, 0);
		}

		public TerminalNode RPAREN() {
			return this.getToken(IkalaScriptParser.RPAREN, 0);
		}

		public StatementNoShortIfContext statementNoShortIf() {
			return this.getRuleContext(StatementNoShortIfContext.class, 0);
		}

		public TerminalNode WHILE() {
			return this.getToken(IkalaScriptParser.WHILE, 0);
		}
	}

	static {
		RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;

	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();

	public static final int BOOLEAN = 1, BREAK = 2, CASE = 3, CHAR = 4,
		CONTINUE = 5, DEFAULT = 6, DO = 7, DOUBLE = 8, ELSE = 9, EXIT = 10,
		FINAL = 11, FOR = 12, GOTO = 13, IF = 14, INT = 15, STRING = 16,
		SWITCH = 17, VOID = 18, WHILE = 19, IntegerLiteral = 20,
		FloatingPointLiteral = 21, BooleanLiteral = 22, CharacterLiteral = 23,
		StringLiteral = 24, NullLiteral = 25, Identifier = 26, LPAREN = 27,
		RPAREN = 28, LBRACE = 29, RBRACE = 30, LBRACK = 31, RBRACK = 32,
		SEMICOLON = 33, COMMA = 34, DOT = 35, ASSIGN = 36, GT = 37, LT = 38,
		NOT = 39, QUESTION = 40, COLON = 41, EQUAL = 42, LTE = 43, GTE = 44,
		NOTEQUAL = 45, AND = 46, OR = 47, INC = 48, DEC = 49, ADD = 50,
		SUB = 51, MUL = 52, DIV = 53, MOD = 54, ADD_ASSIGN = 55,
		SUB_ASSIGN = 56, MUL_ASSIGN = 57, DIV_ASSIGN = 58, MOD_ASSIGN = 59,
		WS = 60, COMMENT = 61, LINE_COMMENT = 62;

	public static final int RULE_literal = 0, RULE_primitiveType = 1,
		RULE_numericType = 2, RULE_referenceType = 3,
		RULE_classOrInterfaceType = 4, RULE_arrayType = 5, RULE_dims = 6,
		RULE_variableDeclaratorList = 7, RULE_variableDeclarator = 8,
		RULE_variableDeclaratorId = 9, RULE_type = 10,
		RULE_compilationUnit = 11, RULE_block = 12, RULE_blockStatements = 13,
		RULE_blockStatement = 14, RULE_localVariableDeclarationStatement = 15,
		RULE_localVariableDeclaration = 16, RULE_statement = 17,
		RULE_statementNoShortIf = 18,
		RULE_statementWithoutTrailingSubstatement = 19, RULE_label = 20,
		RULE_labeledStatement = 21, RULE_labeledStatementNoShortIf = 22,
		RULE_emptyStatement = 23, RULE_expressionStatement = 24,
		RULE_statementExpression = 25, RULE_ifThenStatement = 26,
		RULE_ifThenElseStatement = 27, RULE_ifThenElseStatementNoShortIf = 28,
		RULE_switchStatement = 29, RULE_switchBlock = 30,
		RULE_switchBlockStatementGroup = 31, RULE_switchLabel = 32,
		RULE_whileStatement = 33, RULE_whileStatementNoShortIf = 34,
		RULE_doStatement = 35, RULE_forStatement = 36,
		RULE_forStatementNoShortIf = 37, RULE_forInit = 38,
		RULE_statementExpressionList = 39, RULE_breakStatement = 40,
		RULE_continueStatement = 41, RULE_gotoStatement = 42,
		RULE_exitStatement = 43, RULE_primary = 44,
		RULE_arrayAccess_LHS_General = 45, RULE_primary_extension = 46,
		RULE_primary_extension_access = 47, RULE_primary_LHS = 48,
		RULE_primary_LHS_access = 49, RULE_arrayAccess = 50,
		RULE_arrayAccess_extension = 51, RULE_arrayAccess_LHS = 52,
		RULE_methodInvocation = 53, RULE_methodInvocation_extension = 54,
		RULE_methodInvocation_LHS = 55, RULE_argumentList = 56,
		RULE_expression = 57, RULE_assignment = 58, RULE_leftHandSide = 59,
		RULE_assignmentOperator = 60, RULE_conditionalExpression = 61,
		RULE_conditionalOrExpression = 62, RULE_conditionalAndExpression = 63,
		RULE_equalityExpression = 64, RULE_relationalExpression = 65,
		RULE_additiveExpression = 66, RULE_multiplicativeExpression = 67,
		RULE_unaryExpression = 68, RULE_preIncrementExpression = 69,
		RULE_preDecrementExpression = 70, RULE_unaryExpressionNotPlusMinus = 71,
		RULE_postfixExpression = 72, RULE_postIncrementExpression = 73,
		RULE_postDecrementExpression = 74, RULE_castExpression = 75;

	public static final String[] ruleNames = IkalaScriptParser.makeRuleNames();

	private static final String[] _LITERAL_NAMES =
		IkalaScriptParser.makeLiteralNames();

	private static final String[] _SYMBOLIC_NAMES =
		IkalaScriptParser.makeSymbolicNames();

	public static final Vocabulary VOCABULARY = new VocabularyImpl(
		IkalaScriptParser._LITERAL_NAMES, IkalaScriptParser._SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;

	static {
		tokenNames = new String[IkalaScriptParser._SYMBOLIC_NAMES.length];
		for (int i = 0; i < IkalaScriptParser.tokenNames.length; i++) {
			IkalaScriptParser.tokenNames[i] =
				IkalaScriptParser.VOCABULARY.getLiteralName(i);
			if (IkalaScriptParser.tokenNames[i] == null) {
				IkalaScriptParser.tokenNames[i] =
					IkalaScriptParser.VOCABULARY.getSymbolicName(i);
			}

			if (IkalaScriptParser.tokenNames[i] == null) {
				IkalaScriptParser.tokenNames[i] = "<INVALID>";
			}
		}
	}

	public static final String _serializedATN =
		"\u0004\u0001>\u02ce\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"
			+ "\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"
			+ "\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"
			+ "\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"
			+ "\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"
			+ "\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"
			+ "\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"
			+ "\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"
			+ "\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"
			+ "\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"
			+ "\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"
			+ "#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"
			+ "(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"
			+ "-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0002"
			+ "2\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u00076\u0002"
			+ "7\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007;\u0002"
			+ "<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007@\u0002"
			+ "A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007E\u0002"
			+ "F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007J\u0002"
			+ "K\u0007K\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0003"
			+ "\u0001\u009e\b\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"
			+ "\u0003\u0003\u0003\u00a5\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0005"
			+ "\u0004\u00aa\b\u0004\n\u0004\f\u0004\u00ad\t\u0004\u0001\u0005\u0001\u0005"
			+ "\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"
			+ "\u0003\u0005\u00b7\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"
			+ "\u0005\u0006\u00bd\b\u0006\n\u0006\f\u0006\u00c0\t\u0006\u0001\u0007\u0001"
			+ "\u0007\u0001\u0007\u0005\u0007\u00c5\b\u0007\n\u0007\f\u0007\u00c8\t\u0007"
			+ "\u0001\b\u0001\b\u0001\b\u0003\b\u00cd\b\b\u0001\t\u0001\t\u0003\t\u00d1"
			+ "\b\t\u0001\n\u0001\n\u0003\n\u00d5\b\n\u0001\u000b\u0005\u000b\u00d8\b"
			+ "\u000b\n\u000b\f\u000b\u00db\t\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001"
			+ "\f\u0003\f\u00e1\b\f\u0001\f\u0001\f\u0001\r\u0004\r\u00e6\b\r\u000b\r"
			+ "\f\r\u00e7\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00ed\b\u000e"
			+ "\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0003\u0010\u00f3\b\u0010"
			+ "\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011"
			+ "\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00fe\b\u0011\u0001\u0012"
			+ "\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0105\b\u0012"
			+ "\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"
			+ "\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0110\b\u0013\u0001\u0014"
			+ "\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016"
			+ "\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018"
			+ "\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"
			+ "\u0001\u0019\u0003\u0019\u0126\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a"
			+ "\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b"
			+ "\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c"
			+ "\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"
			+ "\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"
			+ "\u0001\u001d\u0001\u001e\u0001\u001e\u0005\u001e\u0146\b\u001e\n\u001e"
			+ "\f\u001e\u0149\t\u001e\u0001\u001e\u0005\u001e\u014c\b\u001e\n\u001e\f"
			+ "\u001e\u014f\t\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0004\u001f\u0154"
			+ "\b\u001f\u000b\u001f\f\u001f\u0155\u0001\u001f\u0001\u001f\u0001 \u0001"
			+ " \u0001 \u0001 \u0001 \u0001 \u0003 \u0160\b \u0001!\u0001!\u0001!\u0001"
			+ "!\u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001"
			+ "#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001$\u0001$\u0001"
			+ "$\u0003$\u0179\b$\u0001$\u0001$\u0003$\u017d\b$\u0001$\u0001$\u0003$\u0181"
			+ "\b$\u0001$\u0001$\u0001$\u0001%\u0001%\u0001%\u0003%\u0189\b%\u0001%\u0001"
			+ "%\u0003%\u018d\b%\u0001%\u0001%\u0003%\u0191\b%\u0001%\u0001%\u0001%\u0001"
			+ "&\u0001&\u0003&\u0198\b&\u0001\'\u0001\'\u0001\'\u0005\'\u019d\b\'\n\'"
			+ "\f\'\u01a0\t\'\u0001(\u0001(\u0001(\u0001)\u0001)\u0001)\u0001*\u0001"
			+ "*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001,\u0001,\u0005,\u01b1\b,\n,"
			+ "\f,\u01b4\t,\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0003-\u01bc\b"
			+ "-\u0001.\u0001.\u0003.\u01c0\b.\u0001/\u0001/\u00010\u00010\u00010\u0001"
			+ "0\u00010\u00010\u00010\u00010\u00030\u01cc\b0\u00011\u00011\u00011\u0001"
			+ "1\u00011\u00011\u00031\u01d4\b1\u00012\u00012\u00012\u00012\u00012\u0004"
			+ "2\u01db\b2\u000b2\f2\u01dc\u00012\u00012\u00012\u00012\u00012\u00042\u01e4"
			+ "\b2\u000b2\f2\u01e5\u00032\u01e8\b2\u00013\u00013\u00013\u00013\u0001"
			+ "3\u00043\u01ef\b3\u000b3\f3\u01f0\u00014\u00014\u00014\u00014\u00014\u0004"
			+ "4\u01f8\b4\u000b4\f4\u01f9\u00014\u00014\u00014\u00014\u00014\u00044\u0201"
			+ "\b4\u000b4\f4\u0202\u00034\u0205\b4\u00015\u00015\u00015\u00035\u020a"
			+ "\b5\u00015\u00015\u00015\u00015\u00015\u00015\u00035\u0212\b5\u00015\u0001"
			+ "5\u00035\u0216\b5\u00016\u00016\u00016\u00016\u00036\u021c\b6\u00016\u0001"
			+ "6\u00017\u00017\u00017\u00037\u0223\b7\u00017\u00017\u00018\u00018\u0001"
			+ "8\u00058\u022a\b8\n8\f8\u022d\t8\u00019\u00019\u00039\u0231\b9\u0001:"
			+ "\u0001:\u0001:\u0001:\u0001;\u0001;\u0003;\u0239\b;\u0001<\u0001<\u0001"
			+ "=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0003=\u0244\b=\u0001>\u0001"
			+ ">\u0001>\u0001>\u0001>\u0001>\u0005>\u024c\b>\n>\f>\u024f\t>\u0001?\u0001"
			+ "?\u0001?\u0001?\u0001?\u0001?\u0005?\u0257\b?\n?\f?\u025a\t?\u0001@\u0001"
			+ "@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0001@\u0005@\u0265\b@\n@"
			+ "\f@\u0268\t@\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001"
			+ "A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0005A\u0279\bA\nA\fA\u027c"
			+ "\tA\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0005"
			+ "B\u0287\bB\nB\fB\u028a\tB\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"
			+ "C\u0001C\u0001C\u0001C\u0001C\u0001C\u0005C\u0298\bC\nC\fC\u029b\tC\u0001"
			+ "D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0003D\u02a4\bD\u0001E\u0001"
			+ "E\u0001E\u0001F\u0001F\u0001F\u0001G\u0001G\u0001G\u0001G\u0003G\u02b0"
			+ "\bG\u0001H\u0001H\u0003H\u02b4\bH\u0001H\u0005H\u02b7\bH\nH\fH\u02ba\t"
			+ "H\u0001I\u0001I\u0001I\u0001J\u0001J\u0001J\u0001K\u0001K\u0001K\u0001"
			+ "K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0003K\u02cc\bK\u0001K\u0000"
			+ "\u0006|~\u0080\u0082\u0084\u0086L\u0000\u0002\u0004\u0006\b\n\f\u000e"
			+ "\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"
			+ "HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"
			+ "\u008e\u0090\u0092\u0094\u0096\u0000\u0004\u0001\u0000\u0014\u0019\u0003"
			+ "\u0000\u0004\u0004\b\b\u000f\u000f\u0002\u0000$$7;\u0001\u000001\u02e5"
			+ "\u0000\u0098\u0001\u0000\u0000\u0000\u0002\u009d\u0001\u0000\u0000\u0000"
			+ "\u0004\u009f\u0001\u0000\u0000\u0000\u0006\u00a4\u0001\u0000\u0000\u0000"
			+ "\b\u00a6\u0001\u0000\u0000\u0000\n\u00b6\u0001\u0000\u0000\u0000\f\u00b8"
			+ "\u0001\u0000\u0000\u0000\u000e\u00c1\u0001\u0000\u0000\u0000\u0010\u00c9"
			+ "\u0001\u0000\u0000\u0000\u0012\u00ce\u0001\u0000\u0000\u0000\u0014\u00d4"
			+ "\u0001\u0000\u0000\u0000\u0016\u00d9\u0001\u0000\u0000\u0000\u0018\u00de"
			+ "\u0001\u0000\u0000\u0000\u001a\u00e5\u0001\u0000\u0000\u0000\u001c\u00ec"
			+ "\u0001\u0000\u0000\u0000\u001e\u00ee\u0001\u0000\u0000\u0000 \u00f2\u0001"
			+ "\u0000\u0000\u0000\"\u00fd\u0001\u0000\u0000\u0000$\u0104\u0001\u0000"
			+ "\u0000\u0000&\u010f\u0001\u0000\u0000\u0000(\u0111\u0001\u0000\u0000\u0000"
			+ "*\u0114\u0001\u0000\u0000\u0000,\u0117\u0001\u0000\u0000\u0000.\u011a"
			+ "\u0001\u0000\u0000\u00000\u011c\u0001\u0000\u0000\u00002\u0125\u0001\u0000"
			+ "\u0000\u00004\u0127\u0001\u0000\u0000\u00006\u012d\u0001\u0000\u0000\u0000"
			+ "8\u0135\u0001\u0000\u0000\u0000:\u013d\u0001\u0000\u0000\u0000<\u0143"
			+ "\u0001\u0000\u0000\u0000>\u0153\u0001\u0000\u0000\u0000@\u015f\u0001\u0000"
			+ "\u0000\u0000B\u0161\u0001\u0000\u0000\u0000D\u0167\u0001\u0000\u0000\u0000"
			+ "F\u016d\u0001\u0000\u0000\u0000H\u0175\u0001\u0000\u0000\u0000J\u0185"
			+ "\u0001\u0000\u0000\u0000L\u0197\u0001\u0000\u0000\u0000N\u0199\u0001\u0000"
			+ "\u0000\u0000P\u01a1\u0001\u0000\u0000\u0000R\u01a4\u0001\u0000\u0000\u0000"
			+ "T\u01a7\u0001\u0000\u0000\u0000V\u01ab\u0001\u0000\u0000\u0000X\u01ae"
			+ "\u0001\u0000\u0000\u0000Z\u01bb\u0001\u0000\u0000\u0000\\\u01bf\u0001"
			+ "\u0000\u0000\u0000^\u01c1\u0001\u0000\u0000\u0000`\u01cb\u0001\u0000\u0000"
			+ "\u0000b\u01d3\u0001\u0000\u0000\u0000d\u01e7\u0001\u0000\u0000\u0000f"
			+ "\u01e9\u0001\u0000\u0000\u0000h\u0204\u0001\u0000\u0000\u0000j\u0215\u0001"
			+ "\u0000\u0000\u0000l\u0217\u0001\u0000\u0000\u0000n\u021f\u0001\u0000\u0000"
			+ "\u0000p\u0226\u0001\u0000\u0000\u0000r\u0230\u0001\u0000\u0000\u0000t"
			+ "\u0232\u0001\u0000\u0000\u0000v\u0238\u0001\u0000\u0000\u0000x\u023a\u0001"
			+ "\u0000\u0000\u0000z\u0243\u0001\u0000\u0000\u0000|\u0245\u0001\u0000\u0000"
			+ "\u0000~\u0250\u0001\u0000\u0000\u0000\u0080\u025b\u0001\u0000\u0000\u0000"
			+ "\u0082\u0269\u0001\u0000\u0000\u0000\u0084\u027d\u0001\u0000\u0000\u0000"
			+ "\u0086\u028b\u0001\u0000\u0000\u0000\u0088\u02a3\u0001\u0000\u0000\u0000"
			+ "\u008a\u02a5\u0001\u0000\u0000\u0000\u008c\u02a8\u0001\u0000\u0000\u0000"
			+ "\u008e\u02af\u0001\u0000\u0000\u0000\u0090\u02b3\u0001\u0000\u0000\u0000"
			+ "\u0092\u02bb\u0001\u0000\u0000\u0000\u0094\u02be\u0001\u0000\u0000\u0000"
			+ "\u0096\u02cb\u0001\u0000\u0000\u0000\u0098\u0099\u0007\u0000\u0000\u0000"
			+ "\u0099\u0001\u0001\u0000\u0000\u0000\u009a\u009e\u0003\u0004\u0002\u0000"
			+ "\u009b\u009e\u0005\u0001\u0000\u0000\u009c\u009e\u0005\u0010\u0000\u0000"
			+ "\u009d\u009a\u0001\u0000\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000"
			+ "\u009d\u009c\u0001\u0000\u0000\u0000\u009e\u0003\u0001\u0000\u0000\u0000"
			+ "\u009f\u00a0\u0007\u0001\u0000\u0000\u00a0\u0005\u0001\u0000\u0000\u0000"
			+ "\u00a1\u00a5\u0003\b\u0004\u0000\u00a2\u00a5\u0005\u001a\u0000\u0000\u00a3"
			+ "\u00a5\u0003\n\u0005\u0000\u00a4\u00a1\u0001\u0000\u0000\u0000\u00a4\u00a2"
			+ "\u0001\u0000\u0000\u0000\u00a4\u00a3\u0001\u0000\u0000\u0000\u00a5\u0007"
			+ "\u0001\u0000\u0000\u0000\u00a6\u00ab\u0005\u001a\u0000\u0000\u00a7\u00a8"
			+ "\u0005#\u0000\u0000\u00a8\u00aa\u0005\u001a\u0000\u0000\u00a9\u00a7\u0001"
			+ "\u0000\u0000\u0000\u00aa\u00ad\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001"
			+ "\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\t\u0001\u0000"
			+ "\u0000\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ae\u00af\u0003\u0002"
			+ "\u0001\u0000\u00af\u00b0\u0003\f\u0006\u0000\u00b0\u00b7\u0001\u0000\u0000"
			+ "\u0000\u00b1\u00b2\u0003\b\u0004\u0000\u00b2\u00b3\u0003\f\u0006\u0000"
			+ "\u00b3\u00b7\u0001\u0000\u0000\u0000\u00b4\u00b5\u0005\u001a\u0000\u0000"
			+ "\u00b5\u00b7\u0003\f\u0006\u0000\u00b6\u00ae\u0001\u0000\u0000\u0000\u00b6"
			+ "\u00b1\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b7"
			+ "\u000b\u0001\u0000\u0000\u0000\u00b8\u00b9\u0005\u001f\u0000\u0000\u00b9"
			+ "\u00be\u0005 \u0000\u0000\u00ba\u00bb\u0005\u001f\u0000\u0000\u00bb\u00bd"
			+ "\u0005 \u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bd\u00c0\u0001"
			+ "\u0000\u0000\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00be\u00bf\u0001"
			+ "\u0000\u0000\u0000\u00bf\r\u0001\u0000\u0000\u0000\u00c0\u00be\u0001\u0000"
			+ "\u0000\u0000\u00c1\u00c6\u0003\u0010\b\u0000\u00c2\u00c3\u0005\"\u0000"
			+ "\u0000\u00c3\u00c5\u0003\u0010\b\u0000\u00c4\u00c2\u0001\u0000\u0000\u0000"
			+ "\u00c5\u00c8\u0001\u0000\u0000\u0000\u00c6\u00c4\u0001\u0000\u0000\u0000"
			+ "\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u000f\u0001\u0000\u0000\u0000"
			+ "\u00c8\u00c6\u0001\u0000\u0000\u0000\u00c9\u00cc\u0003\u0012\t\u0000\u00ca"
			+ "\u00cb\u0005$\u0000\u0000\u00cb\u00cd\u0003r9\u0000\u00cc\u00ca\u0001"
			+ "\u0000\u0000\u0000\u00cc\u00cd\u0001\u0000\u0000\u0000\u00cd\u0011\u0001"
			+ "\u0000\u0000\u0000\u00ce\u00d0\u0005\u001a\u0000\u0000\u00cf\u00d1\u0003"
			+ "\f\u0006\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d0\u00d1\u0001\u0000"
			+ "\u0000\u0000\u00d1\u0013\u0001\u0000\u0000\u0000\u00d2\u00d5\u0003\u0002"
			+ "\u0001\u0000\u00d3\u00d5\u0003\u0006\u0003\u0000\u00d4\u00d2\u0001\u0000"
			+ "\u0000\u0000\u00d4\u00d3\u0001\u0000\u0000\u0000\u00d5\u0015\u0001\u0000"
			+ "\u0000\u0000\u00d6\u00d8\u0003\u001c\u000e\u0000\u00d7\u00d6\u0001\u0000"
			+ "\u0000\u0000\u00d8\u00db\u0001\u0000\u0000\u0000\u00d9\u00d7\u0001\u0000"
			+ "\u0000\u0000\u00d9\u00da\u0001\u0000\u0000\u0000\u00da\u00dc\u0001\u0000"
			+ "\u0000\u0000\u00db\u00d9\u0001\u0000\u0000\u0000\u00dc\u00dd\u0005\u0000"
			+ "\u0000\u0001\u00dd\u0017\u0001\u0000\u0000\u0000\u00de\u00e0\u0005\u001d"
			+ "\u0000\u0000\u00df\u00e1\u0003\u001a\r\u0000\u00e0\u00df\u0001\u0000\u0000"
			+ "\u0000\u00e0\u00e1\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000\u0000"
			+ "\u0000\u00e2\u00e3\u0005\u001e\u0000\u0000\u00e3\u0019\u0001\u0000\u0000"
			+ "\u0000\u00e4\u00e6\u0003\u001c\u000e\u0000\u00e5\u00e4\u0001\u0000\u0000"
			+ "\u0000\u00e6\u00e7\u0001\u0000\u0000\u0000\u00e7\u00e5\u0001\u0000\u0000"
			+ "\u0000\u00e7\u00e8\u0001\u0000\u0000\u0000\u00e8\u001b\u0001\u0000\u0000"
			+ "\u0000\u00e9\u00ed\u0003\u001e\u000f\u0000\u00ea\u00ed\u0003\"\u0011\u0000"
			+ "\u00eb\u00ed\u0003(\u0014\u0000\u00ec\u00e9\u0001\u0000\u0000\u0000\u00ec"
			+ "\u00ea\u0001\u0000\u0000\u0000\u00ec\u00eb\u0001\u0000\u0000\u0000\u00ed"
			+ "\u001d\u0001\u0000\u0000\u0000\u00ee\u00ef\u0003 \u0010\u0000\u00ef\u00f0"
			+ "\u0005!\u0000\u0000\u00f0\u001f\u0001\u0000\u0000\u0000\u00f1\u00f3\u0005"
			+ "\u000b\u0000\u0000\u00f2\u00f1\u0001\u0000\u0000\u0000\u00f2\u00f3\u0001"
			+ "\u0000\u0000\u0000\u00f3\u00f4\u0001\u0000\u0000\u0000\u00f4\u00f5\u0003"
			+ "\u0014\n\u0000\u00f5\u00f6\u0003\u000e\u0007\u0000\u00f6!\u0001\u0000"
			+ "\u0000\u0000\u00f7\u00fe\u0003&\u0013\u0000\u00f8\u00fe\u0003*\u0015\u0000"
			+ "\u00f9\u00fe\u00034\u001a\u0000\u00fa\u00fe\u00036\u001b\u0000\u00fb\u00fe"
			+ "\u0003B!\u0000\u00fc\u00fe\u0003H$\u0000\u00fd\u00f7\u0001\u0000\u0000"
			+ "\u0000\u00fd\u00f8\u0001\u0000\u0000\u0000\u00fd\u00f9\u0001\u0000\u0000"
			+ "\u0000\u00fd\u00fa\u0001\u0000\u0000\u0000\u00fd\u00fb\u0001\u0000\u0000"
			+ "\u0000\u00fd\u00fc\u0001\u0000\u0000\u0000\u00fe#\u0001\u0000\u0000\u0000"
			+ "\u00ff\u0105\u0003&\u0013\u0000\u0100\u0105\u0003,\u0016\u0000\u0101\u0105"
			+ "\u00038\u001c\u0000\u0102\u0105\u0003D\"\u0000\u0103\u0105\u0003J%\u0000"
			+ "\u0104\u00ff\u0001\u0000\u0000\u0000\u0104\u0100\u0001\u0000\u0000\u0000"
			+ "\u0104\u0101\u0001\u0000\u0000\u0000\u0104\u0102\u0001\u0000\u0000\u0000"
			+ "\u0104\u0103\u0001\u0000\u0000\u0000\u0105%\u0001\u0000\u0000\u0000\u0106"
			+ "\u0110\u0003\u0018\f\u0000\u0107\u0110\u0003.\u0017\u0000\u0108\u0110"
			+ "\u00030\u0018\u0000\u0109\u0110\u0003:\u001d\u0000\u010a\u0110\u0003F"
			+ "#\u0000\u010b\u0110\u0003P(\u0000\u010c\u0110\u0003R)\u0000\u010d\u0110"
			+ "\u0003T*\u0000\u010e\u0110\u0003V+\u0000\u010f\u0106\u0001\u0000\u0000"
			+ "\u0000\u010f\u0107\u0001\u0000\u0000\u0000\u010f\u0108\u0001\u0000\u0000"
			+ "\u0000\u010f\u0109\u0001\u0000\u0000\u0000\u010f\u010a\u0001\u0000\u0000"
			+ "\u0000\u010f\u010b\u0001\u0000\u0000\u0000\u010f\u010c\u0001\u0000\u0000"
			+ "\u0000\u010f\u010d\u0001\u0000\u0000\u0000\u010f\u010e\u0001\u0000\u0000"
			+ "\u0000\u0110\'\u0001\u0000\u0000\u0000\u0111\u0112\u0005\u001a\u0000\u0000"
			+ "\u0112\u0113\u0005)\u0000\u0000\u0113)\u0001\u0000\u0000\u0000\u0114\u0115"
			+ "\u0003(\u0014\u0000\u0115\u0116\u0003\"\u0011\u0000\u0116+\u0001\u0000"
			+ "\u0000\u0000\u0117\u0118\u0003(\u0014\u0000\u0118\u0119\u0003$\u0012\u0000"
			+ "\u0119-\u0001\u0000\u0000\u0000\u011a\u011b\u0005!\u0000\u0000\u011b/"
			+ "\u0001\u0000\u0000\u0000\u011c\u011d\u00032\u0019\u0000\u011d\u011e\u0005"
			+ "!\u0000\u0000\u011e1\u0001\u0000\u0000\u0000\u011f\u0126\u0003t:\u0000"
			+ "\u0120\u0126\u0003\u008aE\u0000\u0121\u0126\u0003\u008cF\u0000\u0122\u0126"
			+ "\u0003\u0092I\u0000\u0123\u0126\u0003\u0094J\u0000\u0124\u0126\u0003j"
			+ "5\u0000\u0125\u011f\u0001\u0000\u0000\u0000\u0125\u0120\u0001\u0000\u0000"
			+ "\u0000\u0125\u0121\u0001\u0000\u0000\u0000\u0125\u0122\u0001\u0000\u0000"
			+ "\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0125\u0124\u0001\u0000\u0000"
			+ "\u0000\u01263\u0001\u0000\u0000\u0000\u0127\u0128\u0005\u000e\u0000\u0000"
			+ "\u0128\u0129\u0005\u001b\u0000\u0000\u0129\u012a\u0003r9\u0000\u012a\u012b"
			+ "\u0005\u001c\u0000\u0000\u012b\u012c\u0003\"\u0011\u0000\u012c5\u0001"
			+ "\u0000\u0000\u0000\u012d\u012e\u0005\u000e\u0000\u0000\u012e\u012f\u0005"
			+ "\u001b\u0000\u0000\u012f\u0130\u0003r9\u0000\u0130\u0131\u0005\u001c\u0000"
			+ "\u0000\u0131\u0132\u0003$\u0012\u0000\u0132\u0133\u0005\t\u0000\u0000"
			+ "\u0133\u0134\u0003\"\u0011\u0000\u01347\u0001\u0000\u0000\u0000\u0135"
			+ "\u0136\u0005\u000e\u0000\u0000\u0136\u0137\u0005\u001b\u0000\u0000\u0137"
			+ "\u0138\u0003r9\u0000\u0138\u0139\u0005\u001c\u0000\u0000\u0139\u013a\u0003"
			+ "$\u0012\u0000\u013a\u013b\u0005\t\u0000\u0000\u013b\u013c\u0003$\u0012"
			+ "\u0000\u013c9\u0001\u0000\u0000\u0000\u013d\u013e\u0005\u0011\u0000\u0000"
			+ "\u013e\u013f\u0005\u001b\u0000\u0000\u013f\u0140\u0003r9\u0000\u0140\u0141"
			+ "\u0005\u001c\u0000\u0000\u0141\u0142\u0003<\u001e\u0000\u0142;\u0001\u0000"
			+ "\u0000\u0000\u0143\u0147\u0005\u001d\u0000\u0000\u0144\u0146\u0003>\u001f"
			+ "\u0000\u0145\u0144\u0001\u0000\u0000\u0000\u0146\u0149\u0001\u0000\u0000"
			+ "\u0000\u0147\u0145\u0001\u0000\u0000\u0000\u0147\u0148\u0001\u0000\u0000"
			+ "\u0000\u0148\u014d\u0001\u0000\u0000\u0000\u0149\u0147\u0001\u0000\u0000"
			+ "\u0000\u014a\u014c\u0003@ \u0000\u014b\u014a\u0001\u0000\u0000\u0000\u014c"
			+ "\u014f\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000\u014d"
			+ "\u014e\u0001\u0000\u0000\u0000\u014e\u0150\u0001\u0000\u0000\u0000\u014f"
			+ "\u014d\u0001\u0000\u0000\u0000\u0150\u0151\u0005\u001e\u0000\u0000\u0151"
			+ "=\u0001\u0000\u0000\u0000\u0152\u0154\u0003@ \u0000\u0153\u0152\u0001"
			+ "\u0000\u0000\u0000\u0154\u0155\u0001\u0000\u0000\u0000\u0155\u0153\u0001"
			+ "\u0000\u0000\u0000\u0155\u0156\u0001\u0000\u0000\u0000\u0156\u0157\u0001"
			+ "\u0000\u0000\u0000\u0157\u0158\u0003\u001a\r\u0000\u0158?\u0001\u0000"
			+ "\u0000\u0000\u0159\u015a\u0005\u0003\u0000\u0000\u015a\u015b\u0003r9\u0000"
			+ "\u015b\u015c\u0005)\u0000\u0000\u015c\u0160\u0001\u0000\u0000\u0000\u015d"
			+ "\u015e\u0005\u0006\u0000\u0000\u015e\u0160\u0005)\u0000\u0000\u015f\u0159"
			+ "\u0001\u0000\u0000\u0000\u015f\u015d\u0001\u0000\u0000\u0000\u0160A\u0001"
			+ "\u0000\u0000\u0000\u0161\u0162\u0005\u0013\u0000\u0000\u0162\u0163\u0005"
			+ "\u001b\u0000\u0000\u0163\u0164\u0003r9\u0000\u0164\u0165\u0005\u001c\u0000"
			+ "\u0000\u0165\u0166\u0003\"\u0011\u0000\u0166C\u0001\u0000\u0000\u0000"
			+ "\u0167\u0168\u0005\u0013\u0000\u0000\u0168\u0169\u0005\u001b\u0000\u0000"
			+ "\u0169\u016a\u0003r9\u0000\u016a\u016b\u0005\u001c\u0000\u0000\u016b\u016c"
			+ "\u0003$\u0012\u0000\u016cE\u0001\u0000\u0000\u0000\u016d\u016e\u0005\u0007"
			+ "\u0000\u0000\u016e\u016f\u0003\"\u0011\u0000\u016f\u0170\u0005\u0013\u0000"
			+ "\u0000\u0170\u0171\u0005\u001b\u0000\u0000\u0171\u0172\u0003r9\u0000\u0172"
			+ "\u0173\u0005\u001c\u0000\u0000\u0173\u0174\u0005!\u0000\u0000\u0174G\u0001"
			+ "\u0000\u0000\u0000\u0175\u0176\u0005\f\u0000\u0000\u0176\u0178\u0005\u001b"
			+ "\u0000\u0000\u0177\u0179\u0003L&\u0000\u0178\u0177\u0001\u0000\u0000\u0000"
			+ "\u0178\u0179\u0001\u0000\u0000\u0000\u0179\u017a\u0001\u0000\u0000\u0000"
			+ "\u017a\u017c\u0005!\u0000\u0000\u017b\u017d\u0003r9\u0000\u017c\u017b"
			+ "\u0001\u0000\u0000\u0000\u017c\u017d\u0001\u0000\u0000\u0000\u017d\u017e"
			+ "\u0001\u0000\u0000\u0000\u017e\u0180\u0005!\u0000\u0000\u017f\u0181\u0003"
			+ "N\'\u0000\u0180\u017f\u0001\u0000\u0000\u0000\u0180\u0181\u0001\u0000"
			+ "\u0000\u0000\u0181\u0182\u0001\u0000\u0000\u0000\u0182\u0183\u0005\u001c"
			+ "\u0000\u0000\u0183\u0184\u0003\"\u0011\u0000\u0184I\u0001\u0000\u0000"
			+ "\u0000\u0185\u0186\u0005\f\u0000\u0000\u0186\u0188\u0005\u001b\u0000\u0000"
			+ "\u0187\u0189\u0003L&\u0000\u0188\u0187\u0001\u0000\u0000\u0000\u0188\u0189"
			+ "\u0001\u0000\u0000\u0000\u0189\u018a\u0001\u0000\u0000\u0000\u018a\u018c"
			+ "\u0005!\u0000\u0000\u018b\u018d\u0003r9\u0000\u018c\u018b\u0001\u0000"
			+ "\u0000\u0000\u018c\u018d\u0001\u0000\u0000\u0000\u018d\u018e\u0001\u0000"
			+ "\u0000\u0000\u018e\u0190\u0005!\u0000\u0000\u018f\u0191\u0003N\'\u0000"
			+ "\u0190\u018f\u0001\u0000\u0000\u0000\u0190\u0191\u0001\u0000\u0000\u0000"
			+ "\u0191\u0192\u0001\u0000\u0000\u0000\u0192\u0193\u0005\u001c\u0000\u0000"
			+ "\u0193\u0194\u0003$\u0012\u0000\u0194K\u0001\u0000\u0000\u0000\u0195\u0198"
			+ "\u0003N\'\u0000\u0196\u0198\u0003 \u0010\u0000\u0197\u0195\u0001\u0000"
			+ "\u0000\u0000\u0197\u0196\u0001\u0000\u0000\u0000\u0198M\u0001\u0000\u0000"
			+ "\u0000\u0199\u019e\u00032\u0019\u0000\u019a\u019b\u0005\"\u0000\u0000"
			+ "\u019b\u019d\u00032\u0019\u0000\u019c\u019a\u0001\u0000\u0000\u0000\u019d"
			+ "\u01a0\u0001\u0000\u0000\u0000\u019e\u019c\u0001\u0000\u0000\u0000\u019e"
			+ "\u019f\u0001\u0000\u0000\u0000\u019fO\u0001\u0000\u0000\u0000\u01a0\u019e"
			+ "\u0001\u0000\u0000\u0000\u01a1\u01a2\u0005\u0002\u0000\u0000\u01a2\u01a3"
			+ "\u0005!\u0000\u0000\u01a3Q\u0001\u0000\u0000\u0000\u01a4\u01a5\u0005\u0005"
			+ "\u0000\u0000\u01a5\u01a6\u0005!\u0000\u0000\u01a6S\u0001\u0000\u0000\u0000"
			+ "\u01a7\u01a8\u0005\r\u0000\u0000\u01a8\u01a9\u0005\u001a\u0000\u0000\u01a9"
			+ "\u01aa\u0005!\u0000\u0000\u01aaU\u0001\u0000\u0000\u0000\u01ab\u01ac\u0005"
			+ "\n\u0000\u0000\u01ac\u01ad\u0005!\u0000\u0000\u01adW\u0001\u0000\u0000"
			+ "\u0000\u01ae\u01b2\u0003`0\u0000\u01af\u01b1\u0003\\.\u0000\u01b0\u01af"
			+ "\u0001\u0000\u0000\u0000\u01b1\u01b4\u0001\u0000\u0000\u0000\u01b2\u01b0"
			+ "\u0001\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3Y\u0001"
			+ "\u0000\u0000\u0000\u01b4\u01b2\u0001\u0000\u0000\u0000\u01b5\u01bc\u0003"
			+ "\u0000\u0000\u0000\u01b6\u01b7\u0005\u001b\u0000\u0000\u01b7\u01b8\u0003"
			+ "r9\u0000\u01b8\u01b9\u0005\u001c\u0000\u0000\u01b9\u01bc\u0001\u0000\u0000"
			+ "\u0000\u01ba\u01bc\u0003j5\u0000\u01bb\u01b5\u0001\u0000\u0000\u0000\u01bb"
			+ "\u01b6\u0001\u0000\u0000\u0000\u01bb\u01ba\u0001\u0000\u0000\u0000\u01bc"
			+ "[\u0001\u0000\u0000\u0000\u01bd\u01c0\u0003f3\u0000\u01be\u01c0\u0003"
			+ "l6\u0000\u01bf\u01bd\u0001\u0000\u0000\u0000\u01bf\u01be\u0001\u0000\u0000"
			+ "\u0000\u01c0]\u0001\u0000\u0000\u0000\u01c1\u01c2\u0003l6\u0000\u01c2"
			+ "_\u0001\u0000\u0000\u0000\u01c3\u01cc\u0003\u0000\u0000\u0000\u01c4\u01c5"
			+ "\u0005\u001b\u0000\u0000\u01c5\u01c6\u0003r9\u0000\u01c6\u01c7\u0005\u001c"
			+ "\u0000\u0000\u01c7\u01cc\u0001\u0000\u0000\u0000\u01c8\u01cc\u0003h4\u0000"
			+ "\u01c9\u01cc\u0003n7\u0000\u01ca\u01cc\u0005\u001a\u0000\u0000\u01cb\u01c3"
			+ "\u0001\u0000\u0000\u0000\u01cb\u01c4\u0001\u0000\u0000\u0000\u01cb\u01c8"
			+ "\u0001\u0000\u0000\u0000\u01cb\u01c9\u0001\u0000\u0000\u0000\u01cb\u01ca"
			+ "\u0001\u0000\u0000\u0000\u01cca\u0001\u0000\u0000\u0000\u01cd\u01d4\u0003"
			+ "\u0000\u0000\u0000\u01ce\u01cf\u0005\u001b\u0000\u0000\u01cf\u01d0\u0003"
			+ "r9\u0000\u01d0\u01d1\u0005\u001c\u0000\u0000\u01d1\u01d4\u0001\u0000\u0000"
			+ "\u0000\u01d2\u01d4\u0003n7\u0000\u01d3\u01cd\u0001\u0000\u0000\u0000\u01d3"
			+ "\u01ce\u0001\u0000\u0000\u0000\u01d3\u01d2\u0001\u0000\u0000\u0000\u01d4"
			+ "c\u0001\u0000\u0000\u0000\u01d5\u01da\u0005\u001a\u0000\u0000\u01d6\u01d7"
			+ "\u0005\u001f\u0000\u0000\u01d7\u01d8\u0003r9\u0000\u01d8\u01d9\u0005 "
			+ "\u0000\u0000\u01d9\u01db\u0001\u0000\u0000\u0000\u01da\u01d6\u0001\u0000"
			+ "\u0000\u0000\u01db\u01dc\u0001\u0000\u0000\u0000\u01dc\u01da\u0001\u0000"
			+ "\u0000\u0000\u01dc\u01dd\u0001\u0000\u0000\u0000\u01dd\u01e8\u0001\u0000"
			+ "\u0000\u0000\u01de\u01e3\u0003Z-\u0000\u01df\u01e0\u0005\u001f\u0000\u0000"
			+ "\u01e0\u01e1\u0003r9\u0000\u01e1\u01e2\u0005 \u0000\u0000\u01e2\u01e4"
			+ "\u0001\u0000\u0000\u0000\u01e3\u01df\u0001\u0000\u0000\u0000\u01e4\u01e5"
			+ "\u0001\u0000\u0000\u0000\u01e5\u01e3\u0001\u0000\u0000\u0000\u01e5\u01e6"
			+ "\u0001\u0000\u0000\u0000\u01e6\u01e8\u0001\u0000\u0000\u0000\u01e7\u01d5"
			+ "\u0001\u0000\u0000\u0000\u01e7\u01de\u0001\u0000\u0000\u0000\u01e8e\u0001"
			+ "\u0000\u0000\u0000\u01e9\u01ee\u0003^/\u0000\u01ea\u01eb\u0005\u001f\u0000"
			+ "\u0000\u01eb\u01ec\u0003r9\u0000\u01ec\u01ed\u0005 \u0000\u0000\u01ed"
			+ "\u01ef\u0001\u0000\u0000\u0000\u01ee\u01ea\u0001\u0000\u0000\u0000\u01ef"
			+ "\u01f0\u0001\u0000\u0000\u0000\u01f0\u01ee\u0001\u0000\u0000\u0000\u01f0"
			+ "\u01f1\u0001\u0000\u0000\u0000\u01f1g\u0001\u0000\u0000\u0000\u01f2\u01f7"
			+ "\u0005\u001a\u0000\u0000\u01f3\u01f4\u0005\u001f\u0000\u0000\u01f4\u01f5"
			+ "\u0003r9\u0000\u01f5\u01f6\u0005 \u0000\u0000\u01f6\u01f8\u0001\u0000"
			+ "\u0000\u0000\u01f7\u01f3\u0001\u0000\u0000\u0000\u01f8\u01f9\u0001\u0000"
			+ "\u0000\u0000\u01f9\u01f7\u0001\u0000\u0000\u0000\u01f9\u01fa\u0001\u0000"
			+ "\u0000\u0000\u01fa\u0205\u0001\u0000\u0000\u0000\u01fb\u0200\u0003b1\u0000"
			+ "\u01fc\u01fd\u0005\u001f\u0000\u0000\u01fd\u01fe\u0003r9\u0000\u01fe\u01ff"
			+ "\u0005 \u0000\u0000\u01ff\u0201\u0001\u0000\u0000\u0000\u0200\u01fc\u0001"
			+ "\u0000\u0000\u0000\u0201\u0202\u0001\u0000\u0000\u0000\u0202\u0200\u0001"
			+ "\u0000\u0000\u0000\u0202\u0203\u0001\u0000\u0000\u0000\u0203\u0205\u0001"
			+ "\u0000\u0000\u0000\u0204\u01f2\u0001\u0000\u0000\u0000\u0204\u01fb\u0001"
			+ "\u0000\u0000\u0000\u0205i\u0001\u0000\u0000\u0000\u0206\u0207\u0005\u001a"
			+ "\u0000\u0000\u0207\u0209\u0005\u001b\u0000\u0000\u0208\u020a\u0003p8\u0000"
			+ "\u0209\u0208\u0001\u0000\u0000\u0000\u0209\u020a\u0001\u0000\u0000\u0000"
			+ "\u020a\u020b\u0001\u0000\u0000\u0000\u020b\u0216\u0005\u001c\u0000\u0000"
			+ "\u020c\u020d\u0003X,\u0000\u020d\u020e\u0005#\u0000\u0000\u020e\u020f"
			+ "\u0005\u001a\u0000\u0000\u020f\u0211\u0005\u001b\u0000\u0000\u0210\u0212"
			+ "\u0003p8\u0000\u0211\u0210\u0001\u0000\u0000\u0000\u0211\u0212\u0001\u0000"
			+ "\u0000\u0000\u0212\u0213\u0001\u0000\u0000\u0000\u0213\u0214\u0005\u001c"
			+ "\u0000\u0000\u0214\u0216\u0001\u0000\u0000\u0000\u0215\u0206\u0001\u0000"
			+ "\u0000\u0000\u0215\u020c\u0001\u0000\u0000\u0000\u0216k\u0001\u0000\u0000"
			+ "\u0000\u0217\u0218\u0005#\u0000\u0000\u0218\u0219\u0005\u001a\u0000\u0000"
			+ "\u0219\u021b\u0005\u001b\u0000\u0000\u021a\u021c\u0003p8\u0000\u021b\u021a"
			+ "\u0001\u0000\u0000\u0000\u021b\u021c\u0001\u0000\u0000\u0000\u021c\u021d"
			+ "\u0001\u0000\u0000\u0000\u021d\u021e\u0005\u001c\u0000\u0000\u021em\u0001"
			+ "\u0000\u0000\u0000\u021f\u0220\u0005\u001a\u0000\u0000\u0220\u0222\u0005"
			+ "\u001b\u0000\u0000\u0221\u0223\u0003p8\u0000\u0222\u0221\u0001\u0000\u0000"
			+ "\u0000\u0222\u0223\u0001\u0000\u0000\u0000\u0223\u0224\u0001\u0000\u0000"
			+ "\u0000\u0224\u0225\u0005\u001c\u0000\u0000\u0225o\u0001\u0000\u0000\u0000"
			+ "\u0226\u022b\u0003r9\u0000\u0227\u0228\u0005\"\u0000\u0000\u0228\u022a"
			+ "\u0003r9\u0000\u0229\u0227\u0001\u0000\u0000\u0000\u022a\u022d\u0001\u0000"
			+ "\u0000\u0000\u022b\u0229\u0001\u0000\u0000\u0000\u022b\u022c\u0001\u0000"
			+ "\u0000\u0000\u022cq\u0001\u0000\u0000\u0000\u022d\u022b\u0001\u0000\u0000"
			+ "\u0000\u022e\u0231\u0003z=\u0000\u022f\u0231\u0003t:\u0000\u0230\u022e"
			+ "\u0001\u0000\u0000\u0000\u0230\u022f\u0001\u0000\u0000\u0000\u0231s\u0001"
			+ "\u0000\u0000\u0000\u0232\u0233\u0003v;\u0000\u0233\u0234\u0003x<\u0000"
			+ "\u0234\u0235\u0003r9\u0000\u0235u\u0001\u0000\u0000\u0000\u0236\u0239"
			+ "\u0005\u001a\u0000\u0000\u0237\u0239\u0003d2\u0000\u0238\u0236\u0001\u0000"
			+ "\u0000\u0000\u0238\u0237\u0001\u0000\u0000\u0000\u0239w\u0001\u0000\u0000"
			+ "\u0000\u023a\u023b\u0007\u0002\u0000\u0000\u023by\u0001\u0000\u0000\u0000"
			+ "\u023c\u0244\u0003|>\u0000\u023d\u023e\u0003|>\u0000\u023e\u023f\u0005"
			+ "(\u0000\u0000\u023f\u0240\u0003r9\u0000\u0240\u0241\u0005)\u0000\u0000"
			+ "\u0241\u0242\u0003z=\u0000\u0242\u0244\u0001\u0000\u0000\u0000\u0243\u023c"
			+ "\u0001\u0000\u0000\u0000\u0243\u023d\u0001\u0000\u0000\u0000\u0244{\u0001"
			+ "\u0000\u0000\u0000\u0245\u0246\u0006>\uffff\uffff\u0000\u0246\u0247\u0003"
			+ "~?\u0000\u0247\u024d\u0001\u0000\u0000\u0000\u0248\u0249\n\u0001\u0000"
			+ "\u0000\u0249\u024a\u0005/\u0000\u0000\u024a\u024c\u0003~?\u0000\u024b"
			+ "\u0248\u0001\u0000\u0000\u0000\u024c\u024f\u0001\u0000\u0000\u0000\u024d"
			+ "\u024b\u0001\u0000\u0000\u0000\u024d\u024e\u0001\u0000\u0000\u0000\u024e"
			+ "}\u0001\u0000\u0000\u0000\u024f\u024d\u0001\u0000\u0000\u0000\u0250\u0251"
			+ "\u0006?\uffff\uffff\u0000\u0251\u0252\u0003\u0080@\u0000\u0252\u0258\u0001"
			+ "\u0000\u0000\u0000\u0253\u0254\n\u0001\u0000\u0000\u0254\u0255\u0005."
			+ "\u0000\u0000\u0255\u0257\u0003\u0080@\u0000\u0256\u0253\u0001\u0000\u0000"
			+ "\u0000\u0257\u025a\u0001\u0000\u0000\u0000\u0258\u0256\u0001\u0000\u0000"
			+ "\u0000\u0258\u0259\u0001\u0000\u0000\u0000\u0259\u007f\u0001\u0000\u0000"
			+ "\u0000\u025a\u0258\u0001\u0000\u0000\u0000\u025b\u025c\u0006@\uffff\uffff"
			+ "\u0000\u025c\u025d\u0003\u0082A\u0000\u025d\u0266\u0001\u0000\u0000\u0000"
			+ "\u025e\u025f\n\u0002\u0000\u0000\u025f\u0260\u0005*\u0000\u0000\u0260"
			+ "\u0265\u0003\u0082A\u0000\u0261\u0262\n\u0001\u0000\u0000\u0262\u0263"
			+ "\u0005-\u0000\u0000\u0263\u0265\u0003\u0082A\u0000\u0264\u025e\u0001\u0000"
			+ "\u0000\u0000\u0264\u0261\u0001\u0000\u0000\u0000\u0265\u0268\u0001\u0000"
			+ "\u0000\u0000\u0266\u0264\u0001\u0000\u0000\u0000\u0266\u0267\u0001\u0000"
			+ "\u0000\u0000\u0267\u0081\u0001\u0000\u0000\u0000\u0268\u0266\u0001\u0000"
			+ "\u0000\u0000\u0269\u026a\u0006A\uffff\uffff\u0000\u026a\u026b\u0003\u0084"
			+ "B\u0000\u026b\u027a\u0001\u0000\u0000\u0000\u026c\u026d\n\u0004\u0000"
			+ "\u0000\u026d\u026e\u0005&\u0000\u0000\u026e\u0279\u0003\u0084B\u0000\u026f"
			+ "\u0270\n\u0003\u0000\u0000\u0270\u0271\u0005%\u0000\u0000\u0271\u0279"
			+ "\u0003\u0084B\u0000\u0272\u0273\n\u0002\u0000\u0000\u0273\u0274\u0005"
			+ "+\u0000\u0000\u0274\u0279\u0003\u0084B\u0000\u0275\u0276\n\u0001\u0000"
			+ "\u0000\u0276\u0277\u0005,\u0000\u0000\u0277\u0279\u0003\u0084B\u0000\u0278"
			+ "\u026c\u0001\u0000\u0000\u0000\u0278\u026f\u0001\u0000\u0000\u0000\u0278"
			+ "\u0272\u0001\u0000\u0000\u0000\u0278\u0275\u0001\u0000\u0000\u0000\u0279"
			+ "\u027c\u0001\u0000\u0000\u0000\u027a\u0278\u0001\u0000\u0000\u0000\u027a"
			+ "\u027b\u0001\u0000\u0000\u0000\u027b\u0083\u0001\u0000\u0000\u0000\u027c"
			+ "\u027a\u0001\u0000\u0000\u0000\u027d\u027e\u0006B\uffff\uffff\u0000\u027e"
			+ "\u027f\u0003\u0086C\u0000\u027f\u0288\u0001\u0000\u0000\u0000\u0280\u0281"
			+ "\n\u0002\u0000\u0000\u0281\u0282\u00052\u0000\u0000\u0282\u0287\u0003"
			+ "\u0086C\u0000\u0283\u0284\n\u0001\u0000\u0000\u0284\u0285\u00053\u0000"
			+ "\u0000\u0285\u0287\u0003\u0086C\u0000\u0286\u0280\u0001\u0000\u0000\u0000"
			+ "\u0286\u0283\u0001\u0000\u0000\u0000\u0287\u028a\u0001\u0000\u0000\u0000"
			+ "\u0288\u0286\u0001\u0000\u0000\u0000\u0288\u0289\u0001\u0000\u0000\u0000"
			+ "\u0289\u0085\u0001\u0000\u0000\u0000\u028a\u0288\u0001\u0000\u0000\u0000"
			+ "\u028b\u028c\u0006C\uffff\uffff\u0000\u028c\u028d\u0003\u0088D\u0000\u028d"
			+ "\u0299\u0001\u0000\u0000\u0000\u028e\u028f\n\u0003\u0000\u0000\u028f\u0290"
			+ "\u00054\u0000\u0000\u0290\u0298\u0003\u0088D\u0000\u0291\u0292\n\u0002"
			+ "\u0000\u0000\u0292\u0293\u00055\u0000\u0000\u0293\u0298\u0003\u0088D\u0000"
			+ "\u0294\u0295\n\u0001\u0000\u0000\u0295\u0296\u00056\u0000\u0000\u0296"
			+ "\u0298\u0003\u0088D\u0000\u0297\u028e\u0001\u0000\u0000\u0000\u0297\u0291"
			+ "\u0001\u0000\u0000\u0000\u0297\u0294\u0001\u0000\u0000\u0000\u0298\u029b"
			+ "\u0001\u0000\u0000\u0000\u0299\u0297\u0001\u0000\u0000\u0000\u0299\u029a"
			+ "\u0001\u0000\u0000\u0000\u029a\u0087\u0001\u0000\u0000\u0000\u029b\u0299"
			+ "\u0001\u0000\u0000\u0000\u029c\u02a4\u0003\u008aE\u0000\u029d\u02a4\u0003"
			+ "\u008cF\u0000\u029e\u029f\u00052\u0000\u0000\u029f\u02a4\u0003\u0088D"
			+ "\u0000\u02a0\u02a1\u00053\u0000\u0000\u02a1\u02a4\u0003\u0088D\u0000\u02a2"
			+ "\u02a4\u0003\u008eG\u0000\u02a3\u029c\u0001\u0000\u0000\u0000\u02a3\u029d"
			+ "\u0001\u0000\u0000\u0000\u02a3\u029e\u0001\u0000\u0000\u0000\u02a3\u02a0"
			+ "\u0001\u0000\u0000\u0000\u02a3\u02a2\u0001\u0000\u0000\u0000\u02a4\u0089"
			+ "\u0001\u0000\u0000\u0000\u02a5\u02a6\u00050\u0000\u0000\u02a6\u02a7\u0003"
			+ "\u0088D\u0000\u02a7\u008b\u0001\u0000\u0000\u0000\u02a8\u02a9\u00051\u0000"
			+ "\u0000\u02a9\u02aa\u0003\u0088D\u0000\u02aa\u008d\u0001\u0000\u0000\u0000"
			+ "\u02ab\u02b0\u0003\u0090H\u0000\u02ac\u02ad\u0005\'\u0000\u0000\u02ad"
			+ "\u02b0\u0003\u0088D\u0000\u02ae\u02b0\u0003\u0096K\u0000\u02af\u02ab\u0001"
			+ "\u0000\u0000\u0000\u02af\u02ac\u0001\u0000\u0000\u0000\u02af\u02ae\u0001"
			+ "\u0000\u0000\u0000\u02b0\u008f\u0001\u0000\u0000\u0000\u02b1\u02b4\u0003"
			+ "X,\u0000\u02b2\u02b4\u0005\u001a\u0000\u0000\u02b3\u02b1\u0001\u0000\u0000"
			+ "\u0000\u02b3\u02b2\u0001\u0000\u0000\u0000\u02b4\u02b8\u0001\u0000\u0000"
			+ "\u0000\u02b5\u02b7\u0007\u0003\u0000\u0000\u02b6\u02b5\u0001\u0000\u0000"
			+ "\u0000\u02b7\u02ba\u0001\u0000\u0000\u0000\u02b8\u02b6\u0001\u0000\u0000"
			+ "\u0000\u02b8\u02b9\u0001\u0000\u0000\u0000\u02b9\u0091\u0001\u0000\u0000"
			+ "\u0000\u02ba\u02b8\u0001\u0000\u0000\u0000\u02bb\u02bc\u0003\u0090H\u0000"
			+ "\u02bc\u02bd\u00050\u0000\u0000\u02bd\u0093\u0001\u0000\u0000\u0000\u02be"
			+ "\u02bf\u0003\u0090H\u0000\u02bf\u02c0\u00051\u0000\u0000\u02c0\u0095\u0001"
			+ "\u0000\u0000\u0000\u02c1\u02c2\u0005\u001b\u0000\u0000\u02c2\u02c3\u0003"
			+ "\u0002\u0001\u0000\u02c3\u02c4\u0005\u001c\u0000\u0000\u02c4\u02c5\u0003"
			+ "\u0088D\u0000\u02c5\u02cc\u0001\u0000\u0000\u0000\u02c6\u02c7\u0005\u001b"
			+ "\u0000\u0000\u02c7\u02c8\u0003\u0006\u0003\u0000\u02c8\u02c9\u0005\u001c"
			+ "\u0000\u0000\u02c9\u02ca\u0003\u008eG\u0000\u02ca\u02cc\u0001\u0000\u0000"
			+ "\u0000\u02cb\u02c1\u0001\u0000\u0000\u0000\u02cb\u02c6\u0001\u0000\u0000"
			+ "\u0000\u02cc\u0097\u0001\u0000\u0000\u0000B\u009d\u00a4\u00ab\u00b6\u00be"
			+ "\u00c6\u00cc\u00d0\u00d4\u00d9\u00e0\u00e7\u00ec\u00f2\u00fd\u0104\u010f"
			+ "\u0125\u0147\u014d\u0155\u015f\u0178\u017c\u0180\u0188\u018c\u0190\u0197"
			+ "\u019e\u01b2\u01bb\u01bf\u01cb\u01d3\u01dc\u01e5\u01e7\u01f0\u01f9\u0202"
			+ "\u0204\u0209\u0211\u0215\u021b\u0222\u022b\u0230\u0238\u0243\u024d\u0258"
			+ "\u0264\u0266\u0278\u027a\u0286\u0288\u0297\u0299\u02a3\u02af\u02b3\u02b8"
			+ "\u02cb";

	public static final ATN _ATN = new ATNDeserializer()
		.deserialize(IkalaScriptParser._serializedATN.toCharArray());

	static {
		_decisionToDFA = new DFA[IkalaScriptParser._ATN.getNumberOfDecisions()];
		for (int i = 0; i < IkalaScriptParser._ATN.getNumberOfDecisions();
			i++) {
			IkalaScriptParser._decisionToDFA[i] =
				new DFA(IkalaScriptParser._ATN.getDecisionState(i), i);
		}
	}

	private static String[] makeLiteralNames() {
		return new String[] {null, "'boolean'", "'break'", "'case'", "'char'",
			"'continue'", "'default'", "'do'", "'double'", "'else'", "'exit'",
			"'final'", "'for'", "'goto'", "'if'", "'int'", "'string'",
			"'switch'", "'void'", "'while'", null, null, null, null, null,
			"'null'", null, "'('", "')'", "'{'", "'}'", "'['", "']'", "';'",
			"','", "'.'", "'='", "'>'", "'<'", "'!'", "'?'", "':'", "'=='",
			"'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'",
			"'-'", "'*'", "'/'", "'%'", "'+='", "'-='", "'*='", "'/='", "'%='"};
	}

	private static String[] makeRuleNames() {
		return new String[] {"literal", "primitiveType", "numericType",
			"referenceType", "classOrInterfaceType", "arrayType", "dims",
			"variableDeclaratorList", "variableDeclarator",
			"variableDeclaratorId", "type", "compilationUnit", "block",
			"blockStatements", "blockStatement",
			"localVariableDeclarationStatement", "localVariableDeclaration",
			"statement", "statementNoShortIf",
			"statementWithoutTrailingSubstatement", "label", "labeledStatement",
			"labeledStatementNoShortIf", "emptyStatement",
			"expressionStatement", "statementExpression", "ifThenStatement",
			"ifThenElseStatement", "ifThenElseStatementNoShortIf",
			"switchStatement", "switchBlock", "switchBlockStatementGroup",
			"switchLabel", "whileStatement", "whileStatementNoShortIf",
			"doStatement", "forStatement", "forStatementNoShortIf", "forInit",
			"statementExpressionList", "breakStatement", "continueStatement",
			"gotoStatement", "exitStatement", "primary",
			"arrayAccess_LHS_General", "primary_extension",
			"primary_extension_access", "primary_LHS", "primary_LHS_access",
			"arrayAccess", "arrayAccess_extension", "arrayAccess_LHS",
			"methodInvocation", "methodInvocation_extension",
			"methodInvocation_LHS", "argumentList", "expression", "assignment",
			"leftHandSide", "assignmentOperator", "conditionalExpression",
			"conditionalOrExpression", "conditionalAndExpression",
			"equalityExpression", "relationalExpression", "additiveExpression",
			"multiplicativeExpression", "unaryExpression",
			"preIncrementExpression", "preDecrementExpression",
			"unaryExpressionNotPlusMinus", "postfixExpression",
			"postIncrementExpression", "postDecrementExpression",
			"castExpression"};
	}

	private static String[] makeSymbolicNames() {
		return new String[] {null, "BOOLEAN", "BREAK", "CASE", "CHAR",
			"CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", "EXIT", "FINAL",
			"FOR", "GOTO", "IF", "INT", "STRING", "SWITCH", "VOID", "WHILE",
			"IntegerLiteral", "FloatingPointLiteral", "BooleanLiteral",
			"CharacterLiteral", "StringLiteral", "NullLiteral", "Identifier",
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK",
			"SEMICOLON", "COMMA", "DOT", "ASSIGN", "GT", "LT", "NOT",
			"QUESTION", "COLON", "EQUAL", "LTE", "GTE", "NOTEQUAL", "AND", "OR",
			"INC", "DEC", "ADD", "SUB", "MUL", "DIV", "MOD", "ADD_ASSIGN",
			"SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "MOD_ASSIGN", "WS",
			"COMMENT", "LINE_COMMENT"};
	}

	public IkalaScriptParser(TokenStream input) {
		super(input);
		this._interp = new ParserATNSimulator(this, IkalaScriptParser._ATN,
			IkalaScriptParser._decisionToDFA,
			IkalaScriptParser._sharedContextCache);
	}

	public final AdditiveExpressionContext additiveExpression()
		throws RecognitionException {
		return this.additiveExpression(0);
	}

	private AdditiveExpressionContext additiveExpression(int _p)
		throws RecognitionException {
		ParserRuleContext _parentctx = this._ctx;
		int _parentState = this.getState();
		AdditiveExpressionContext _localctx =
			new AdditiveExpressionContext(this._ctx, _parentState);
		AdditiveExpressionContext _prevctx = _localctx;
		int _startState = 132;
		this.enterRecursionRule(_localctx, 132,
			IkalaScriptParser.RULE_additiveExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(638);
					this.multiplicativeExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(648);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 58,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							this.setState(646);
							this._errHandler.sync(this);
							switch (this.getInterpreter()
								.adaptivePredict(this._input, 57, this._ctx)) {
								case 1: {
									_localctx = new AdditiveExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_additiveExpression);
									this.setState(640);
									if (!(this.precpred(this._ctx, 2))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 2)");
									}
									this.setState(641);
									this.match(IkalaScriptParser.ADD);
									this.setState(642);
									this.multiplicativeExpression(0);
								}
									break;
								case 2: {
									_localctx = new AdditiveExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_additiveExpression);
									this.setState(643);
									if (!(this.precpred(this._ctx, 1))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 1)");
									}
									this.setState(644);
									this.match(IkalaScriptParser.SUB);
									this.setState(645);
									this.multiplicativeExpression(0);
								}
									break;
							}
						}
					}
					this.setState(650);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						58, this._ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	private boolean additiveExpression_sempred(
		AdditiveExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
			case 8:
				return this.precpred(this._ctx, 2);
			case 9:
				return this.precpred(this._ctx, 1);
		}
		return true;
	}

	public final ArgumentListContext argumentList()
		throws RecognitionException {
		ArgumentListContext _localctx =
			new ArgumentListContext(this._ctx, this.getState());
		this.enterRule(_localctx, 112, IkalaScriptParser.RULE_argumentList);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(550);
				this.expression();
				this.setState(555);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.COMMA) {
					{
						{
							this.setState(551);
							this.match(IkalaScriptParser.COMMA);
							this.setState(552);
							this.expression();
						}
					}
					this.setState(557);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ArrayAccessContext arrayAccess() throws RecognitionException {
		ArrayAccessContext _localctx =
			new ArrayAccessContext(this._ctx, this.getState());
		this.enterRule(_localctx, 100, IkalaScriptParser.RULE_arrayAccess);
		int _la;
		try {
			this.setState(487);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 37,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(469);
					this.match(IkalaScriptParser.Identifier);
					this.setState(474);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					do {
						{
							{
								this.setState(470);
								this.match(IkalaScriptParser.LBRACK);
								this.setState(471);
								this.expression();
								this.setState(472);
								this.match(IkalaScriptParser.RBRACK);
							}
						}
						this.setState(476);
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					while (_la == IkalaScriptParser.LBRACK);
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(478);
					this.arrayAccess_LHS_General();
					this.setState(483);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					do {
						{
							{
								this.setState(479);
								this.match(IkalaScriptParser.LBRACK);
								this.setState(480);
								this.expression();
								this.setState(481);
								this.match(IkalaScriptParser.RBRACK);
							}
						}
						this.setState(485);
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					while (_la == IkalaScriptParser.LBRACK);
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ArrayAccess_extensionContext arrayAccess_extension()
		throws RecognitionException {
		ArrayAccess_extensionContext _localctx =
			new ArrayAccess_extensionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 102,
			IkalaScriptParser.RULE_arrayAccess_extension);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(489);
				this.primary_extension_access();
				this.setState(494);
				this._errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								this.setState(490);
								this.match(IkalaScriptParser.LBRACK);
								this.setState(491);
								this.expression();
								this.setState(492);
								this.match(IkalaScriptParser.RBRACK);
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
					}
					this.setState(496);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						38, this._ctx);
				}
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ArrayAccess_LHSContext arrayAccess_LHS()
		throws RecognitionException {
		ArrayAccess_LHSContext _localctx =
			new ArrayAccess_LHSContext(this._ctx, this.getState());
		this.enterRule(_localctx, 104, IkalaScriptParser.RULE_arrayAccess_LHS);
		try {
			int _alt;
			this.setState(516);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 41,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(498);
					this.match(IkalaScriptParser.Identifier);
					this.setState(503);
					this._errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
							case 1: {
								{
									this.setState(499);
									this.match(IkalaScriptParser.LBRACK);
									this.setState(500);
									this.expression();
									this.setState(501);
									this.match(IkalaScriptParser.RBRACK);
								}
							}
								break;
							default:
								throw new NoViableAltException(this);
						}
						this.setState(505);
						this._errHandler.sync(this);
						_alt = this.getInterpreter()
							.adaptivePredict(this._input, 39, this._ctx);
					}
					while (_alt != 2
						&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(507);
					this.primary_LHS_access();
					this.setState(512);
					this._errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
							case 1: {
								{
									this.setState(508);
									this.match(IkalaScriptParser.LBRACK);
									this.setState(509);
									this.expression();
									this.setState(510);
									this.match(IkalaScriptParser.RBRACK);
								}
							}
								break;
							default:
								throw new NoViableAltException(this);
						}
						this.setState(514);
						this._errHandler.sync(this);
						_alt = this.getInterpreter()
							.adaptivePredict(this._input, 40, this._ctx);
					}
					while (_alt != 2
						&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ArrayAccess_LHS_GeneralContext arrayAccess_LHS_General()
		throws RecognitionException {
		ArrayAccess_LHS_GeneralContext _localctx =
			new ArrayAccess_LHS_GeneralContext(this._ctx, this.getState());
		this.enterRule(_localctx, 90,
			IkalaScriptParser.RULE_arrayAccess_LHS_General);
		try {
			this.setState(443);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 31,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(437);
					this.literal();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(438);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(439);
					this.expression();
					this.setState(440);
					this.match(IkalaScriptParser.RPAREN);
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(442);
					this.methodInvocation();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx =
			new ArrayTypeContext(this._ctx, this.getState());
		this.enterRule(_localctx, 10, IkalaScriptParser.RULE_arrayType);
		try {
			this.setState(182);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 3,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(174);
					this.primitiveType();
					this.setState(175);
					this.dims();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(177);
					this.classOrInterfaceType();
					this.setState(178);
					this.dims();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(180);
					this.match(IkalaScriptParser.Identifier);
					this.setState(181);
					this.dims();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx =
			new AssignmentContext(this._ctx, this.getState());
		this.enterRule(_localctx, 116, IkalaScriptParser.RULE_assignment);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(562);
				this.leftHandSide();
				this.setState(563);
				this.assignmentOperator();
				this.setState(564);
				this.expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final AssignmentOperatorContext assignmentOperator()
		throws RecognitionException {
		AssignmentOperatorContext _localctx =
			new AssignmentOperatorContext(this._ctx, this.getState());
		this.enterRule(_localctx, 120,
			IkalaScriptParser.RULE_assignmentOperator);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(570);
				_la = this._input.LA(1);
				if (!((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 1116892776307359744L) != 0))) {
					this._errHandler.recoverInline(this);
				}
				else {
					if (this._input.LA(1) == Token.EOF) {
						this.matchedEOF = true;
					}
					this._errHandler.reportMatch(this);
					this.consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(this._ctx, this.getState());
		this.enterRule(_localctx, 24, IkalaScriptParser.RULE_block);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(222);
				this.match(IkalaScriptParser.LBRACE);
				this.setState(224);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844434325110198L) != 0)) {
					{
						this.setState(223);
						this.blockStatements();
					}
				}

				this.setState(226);
				this.match(IkalaScriptParser.RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final BlockStatementContext blockStatement()
		throws RecognitionException {
		BlockStatementContext _localctx =
			new BlockStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 28, IkalaScriptParser.RULE_blockStatement);
		try {
			this.setState(236);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 12,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(233);
					this.localVariableDeclarationStatement();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(234);
					this.statement();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(235);
					this.label();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final BlockStatementsContext blockStatements()
		throws RecognitionException {
		BlockStatementsContext _localctx =
			new BlockStatementsContext(this._ctx, this.getState());
		this.enterRule(_localctx, 26, IkalaScriptParser.RULE_blockStatements);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(229);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
						{
							this.setState(228);
							this.blockStatement();
						}
					}
					this.setState(231);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				while ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844434325110198L) != 0));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final BreakStatementContext breakStatement()
		throws RecognitionException {
		BreakStatementContext _localctx =
			new BreakStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 80, IkalaScriptParser.RULE_breakStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(417);
				this.match(IkalaScriptParser.BREAK);
				this.setState(418);
				this.match(IkalaScriptParser.SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final CastExpressionContext castExpression()
		throws RecognitionException {
		CastExpressionContext _localctx =
			new CastExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 150, IkalaScriptParser.RULE_castExpression);
		try {
			this.setState(715);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 65,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(705);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(706);
					this.primitiveType();
					this.setState(707);
					this.match(IkalaScriptParser.RPAREN);
					this.setState(708);
					this.unaryExpression();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(710);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(711);
					this.referenceType();
					this.setState(712);
					this.match(IkalaScriptParser.RPAREN);
					this.setState(713);
					this.unaryExpressionNotPlusMinus();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ClassOrInterfaceTypeContext classOrInterfaceType()
		throws RecognitionException {
		ClassOrInterfaceTypeContext _localctx =
			new ClassOrInterfaceTypeContext(this._ctx, this.getState());
		this.enterRule(_localctx, 8,
			IkalaScriptParser.RULE_classOrInterfaceType);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(166);
				this.match(IkalaScriptParser.Identifier);
				this.setState(171);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.DOT) {
					{
						{
							this.setState(167);
							this.match(IkalaScriptParser.DOT);
							this.setState(168);
							this.match(IkalaScriptParser.Identifier);
						}
					}
					this.setState(173);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final CompilationUnitContext compilationUnit()
		throws RecognitionException {
		CompilationUnitContext _localctx =
			new CompilationUnitContext(this._ctx, this.getState());
		this.enterRule(_localctx, 22, IkalaScriptParser.RULE_compilationUnit);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(217);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844434325110198L) != 0)) {
					{
						{
							this.setState(214);
							this.blockStatement();
						}
					}
					this.setState(219);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.setState(220);
				this.match(Recognizer.EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ConditionalAndExpressionContext conditionalAndExpression()
		throws RecognitionException {
		return this.conditionalAndExpression(0);
	}

	private ConditionalAndExpressionContext conditionalAndExpression(int _p)
		throws RecognitionException {
		ParserRuleContext _parentctx = this._ctx;
		int _parentState = this.getState();
		ConditionalAndExpressionContext _localctx =
			new ConditionalAndExpressionContext(this._ctx, _parentState);
		ConditionalAndExpressionContext _prevctx = _localctx;
		int _startState = 126;
		this.enterRecursionRule(_localctx, 126,
			IkalaScriptParser.RULE_conditionalAndExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(593);
					this.equalityExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(600);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 52,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							{
								_localctx = new ConditionalAndExpressionContext(
									_parentctx, _parentState);
								this.pushNewRecursionContext(_localctx,
									_startState,
									IkalaScriptParser.RULE_conditionalAndExpression);
								this.setState(595);
								if (!(this.precpred(this._ctx, 1))) {
									throw new FailedPredicateException(this,
										"precpred(_ctx, 1)");
								}
								this.setState(596);
								this.match(IkalaScriptParser.AND);
								this.setState(597);
								this.equalityExpression(0);
							}
						}
					}
					this.setState(602);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						52, this._ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	private boolean conditionalAndExpression_sempred(
		ConditionalAndExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
			case 1:
				return this.precpred(this._ctx, 1);
		}
		return true;
	}

	public final ConditionalExpressionContext conditionalExpression()
		throws RecognitionException {
		ConditionalExpressionContext _localctx =
			new ConditionalExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 122,
			IkalaScriptParser.RULE_conditionalExpression);
		try {
			this.setState(579);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 50,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(572);
					this.conditionalOrExpression(0);
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(573);
					this.conditionalOrExpression(0);
					this.setState(574);
					this.match(IkalaScriptParser.QUESTION);
					this.setState(575);
					this.expression();
					this.setState(576);
					this.match(IkalaScriptParser.COLON);
					this.setState(577);
					this.conditionalExpression();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ConditionalOrExpressionContext conditionalOrExpression()
		throws RecognitionException {
		return this.conditionalOrExpression(0);
	}

	private ConditionalOrExpressionContext conditionalOrExpression(int _p)
		throws RecognitionException {
		ParserRuleContext _parentctx = this._ctx;
		int _parentState = this.getState();
		ConditionalOrExpressionContext _localctx =
			new ConditionalOrExpressionContext(this._ctx, _parentState);
		ConditionalOrExpressionContext _prevctx = _localctx;
		int _startState = 124;
		this.enterRecursionRule(_localctx, 124,
			IkalaScriptParser.RULE_conditionalOrExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(582);
					this.conditionalAndExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(589);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 51,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							{
								_localctx = new ConditionalOrExpressionContext(
									_parentctx, _parentState);
								this.pushNewRecursionContext(_localctx,
									_startState,
									IkalaScriptParser.RULE_conditionalOrExpression);
								this.setState(584);
								if (!(this.precpred(this._ctx, 1))) {
									throw new FailedPredicateException(this,
										"precpred(_ctx, 1)");
								}
								this.setState(585);
								this.match(IkalaScriptParser.OR);
								this.setState(586);
								this.conditionalAndExpression(0);
							}
						}
					}
					this.setState(591);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						51, this._ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	private boolean conditionalOrExpression_sempred(
		ConditionalOrExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
			case 0:
				return this.precpred(this._ctx, 1);
		}
		return true;
	}

	public final ContinueStatementContext continueStatement()
		throws RecognitionException {
		ContinueStatementContext _localctx =
			new ContinueStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 82, IkalaScriptParser.RULE_continueStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(420);
				this.match(IkalaScriptParser.CONTINUE);
				this.setState(421);
				this.match(IkalaScriptParser.SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final DimsContext dims() throws RecognitionException {
		DimsContext _localctx = new DimsContext(this._ctx, this.getState());
		this.enterRule(_localctx, 12, IkalaScriptParser.RULE_dims);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(184);
				this.match(IkalaScriptParser.LBRACK);
				this.setState(185);
				this.match(IkalaScriptParser.RBRACK);
				this.setState(190);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.LBRACK) {
					{
						{
							this.setState(186);
							this.match(IkalaScriptParser.LBRACK);
							this.setState(187);
							this.match(IkalaScriptParser.RBRACK);
						}
					}
					this.setState(192);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final DoStatementContext doStatement() throws RecognitionException {
		DoStatementContext _localctx =
			new DoStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 70, IkalaScriptParser.RULE_doStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(365);
				this.match(IkalaScriptParser.DO);
				this.setState(366);
				this.statement();
				this.setState(367);
				this.match(IkalaScriptParser.WHILE);
				this.setState(368);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(369);
				this.expression();
				this.setState(370);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(371);
				this.match(IkalaScriptParser.SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final EmptyStatementContext emptyStatement()
		throws RecognitionException {
		EmptyStatementContext _localctx =
			new EmptyStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 46, IkalaScriptParser.RULE_emptyStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(282);
				this.match(IkalaScriptParser.SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final EqualityExpressionContext equalityExpression()
		throws RecognitionException {
		return this.equalityExpression(0);
	}

	private EqualityExpressionContext equalityExpression(int _p)
		throws RecognitionException {
		ParserRuleContext _parentctx = this._ctx;
		int _parentState = this.getState();
		EqualityExpressionContext _localctx =
			new EqualityExpressionContext(this._ctx, _parentState);
		EqualityExpressionContext _prevctx = _localctx;
		int _startState = 128;
		this.enterRecursionRule(_localctx, 128,
			IkalaScriptParser.RULE_equalityExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(604);
					this.relationalExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(614);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 54,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							this.setState(612);
							this._errHandler.sync(this);
							switch (this.getInterpreter()
								.adaptivePredict(this._input, 53, this._ctx)) {
								case 1: {
									_localctx = new EqualityExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_equalityExpression);
									this.setState(606);
									if (!(this.precpred(this._ctx, 2))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 2)");
									}
									this.setState(607);
									this.match(IkalaScriptParser.EQUAL);
									this.setState(608);
									this.relationalExpression(0);
								}
									break;
								case 2: {
									_localctx = new EqualityExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_equalityExpression);
									this.setState(609);
									if (!(this.precpred(this._ctx, 1))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 1)");
									}
									this.setState(610);
									this.match(IkalaScriptParser.NOTEQUAL);
									this.setState(611);
									this.relationalExpression(0);
								}
									break;
							}
						}
					}
					this.setState(616);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						54, this._ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	private boolean equalityExpression_sempred(
		EqualityExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
			case 2:
				return this.precpred(this._ctx, 2);
			case 3:
				return this.precpred(this._ctx, 1);
		}
		return true;
	}

	public final ExitStatementContext exitStatement()
		throws RecognitionException {
		ExitStatementContext _localctx =
			new ExitStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 86, IkalaScriptParser.RULE_exitStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(427);
				this.match(IkalaScriptParser.EXIT);
				this.setState(428);
				this.match(IkalaScriptParser.SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx =
			new ExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 114, IkalaScriptParser.RULE_expression);
		try {
			this.setState(560);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 48,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(558);
					this.conditionalExpression();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(559);
					this.assignment();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ExpressionStatementContext expressionStatement()
		throws RecognitionException {
		ExpressionStatementContext _localctx =
			new ExpressionStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 48,
			IkalaScriptParser.RULE_expressionStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(284);
				this.statementExpression();
				this.setState(285);
				this.match(IkalaScriptParser.SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx =
			new ForInitContext(this._ctx, this.getState());
		this.enterRule(_localctx, 76, IkalaScriptParser.RULE_forInit);
		try {
			this.setState(407);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 28,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(405);
					this.statementExpressionList();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(406);
					this.localVariableDeclaration();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ForStatementContext forStatement()
		throws RecognitionException {
		ForStatementContext _localctx =
			new ForStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 72, IkalaScriptParser.RULE_forStatement);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(373);
				this.match(IkalaScriptParser.FOR);
				this.setState(374);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(376);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844425197619474L) != 0)) {
					{
						this.setState(375);
						this.forInit();
					}
				}

				this.setState(378);
				this.match(IkalaScriptParser.SEMICOLON);
				this.setState(380);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 4222674673860608L) != 0)) {
					{
						this.setState(379);
						this.expression();
					}
				}

				this.setState(382);
				this.match(IkalaScriptParser.SEMICOLON);
				this.setState(384);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844425197518848L) != 0)) {
					{
						this.setState(383);
						this.statementExpressionList();
					}
				}

				this.setState(386);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(387);
				this.statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ForStatementNoShortIfContext forStatementNoShortIf()
		throws RecognitionException {
		ForStatementNoShortIfContext _localctx =
			new ForStatementNoShortIfContext(this._ctx, this.getState());
		this.enterRule(_localctx, 74,
			IkalaScriptParser.RULE_forStatementNoShortIf);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(389);
				this.match(IkalaScriptParser.FOR);
				this.setState(390);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(392);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844425197619474L) != 0)) {
					{
						this.setState(391);
						this.forInit();
					}
				}

				this.setState(394);
				this.match(IkalaScriptParser.SEMICOLON);
				this.setState(396);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 4222674673860608L) != 0)) {
					{
						this.setState(395);
						this.expression();
					}
				}

				this.setState(398);
				this.match(IkalaScriptParser.SEMICOLON);
				this.setState(400);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844425197518848L) != 0)) {
					{
						this.setState(399);
						this.statementExpressionList();
					}
				}

				this.setState(402);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(403);
				this.statementNoShortIf();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	@Override
	public ATN getATN() {
		return IkalaScriptParser._ATN;
	}

	@Override
	public String getGrammarFileName() {
		return "IkalaScriptParser.g4";
	}

	@Override
	public String[] getRuleNames() {
		return IkalaScriptParser.ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return IkalaScriptParser._serializedATN;
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return IkalaScriptParser.tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return IkalaScriptParser.VOCABULARY;
	}

	public final GotoStatementContext gotoStatement()
		throws RecognitionException {
		GotoStatementContext _localctx =
			new GotoStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 84, IkalaScriptParser.RULE_gotoStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(423);
				this.match(IkalaScriptParser.GOTO);
				this.setState(424);
				this.match(IkalaScriptParser.Identifier);
				this.setState(425);
				this.match(IkalaScriptParser.SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final IfThenElseStatementContext ifThenElseStatement()
		throws RecognitionException {
		IfThenElseStatementContext _localctx =
			new IfThenElseStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 54,
			IkalaScriptParser.RULE_ifThenElseStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(301);
				this.match(IkalaScriptParser.IF);
				this.setState(302);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(303);
				this.expression();
				this.setState(304);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(305);
				this.statementNoShortIf();
				this.setState(306);
				this.match(IkalaScriptParser.ELSE);
				this.setState(307);
				this.statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final IfThenElseStatementNoShortIfContext
		ifThenElseStatementNoShortIf() throws RecognitionException {
		IfThenElseStatementNoShortIfContext _localctx =
			new IfThenElseStatementNoShortIfContext(this._ctx, this.getState());
		this.enterRule(_localctx, 56,
			IkalaScriptParser.RULE_ifThenElseStatementNoShortIf);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(309);
				this.match(IkalaScriptParser.IF);
				this.setState(310);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(311);
				this.expression();
				this.setState(312);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(313);
				this.statementNoShortIf();
				this.setState(314);
				this.match(IkalaScriptParser.ELSE);
				this.setState(315);
				this.statementNoShortIf();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final IfThenStatementContext ifThenStatement()
		throws RecognitionException {
		IfThenStatementContext _localctx =
			new IfThenStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 52, IkalaScriptParser.RULE_ifThenStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(295);
				this.match(IkalaScriptParser.IF);
				this.setState(296);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(297);
				this.expression();
				this.setState(298);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(299);
				this.statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(this._ctx, this.getState());
		this.enterRule(_localctx, 40, IkalaScriptParser.RULE_label);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(273);
				this.match(IkalaScriptParser.Identifier);
				this.setState(274);
				this.match(IkalaScriptParser.COLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final LabeledStatementContext labeledStatement()
		throws RecognitionException {
		LabeledStatementContext _localctx =
			new LabeledStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 42, IkalaScriptParser.RULE_labeledStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(276);
				this.label();
				this.setState(277);
				this.statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final LabeledStatementNoShortIfContext labeledStatementNoShortIf()
		throws RecognitionException {
		LabeledStatementNoShortIfContext _localctx =
			new LabeledStatementNoShortIfContext(this._ctx, this.getState());
		this.enterRule(_localctx, 44,
			IkalaScriptParser.RULE_labeledStatementNoShortIf);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(279);
				this.label();
				this.setState(280);
				this.statementNoShortIf();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final LeftHandSideContext leftHandSide()
		throws RecognitionException {
		LeftHandSideContext _localctx =
			new LeftHandSideContext(this._ctx, this.getState());
		this.enterRule(_localctx, 118, IkalaScriptParser.RULE_leftHandSide);
		try {
			this.setState(568);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 49,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(566);
					this.match(IkalaScriptParser.Identifier);
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(567);
					this.arrayAccess();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx =
			new LiteralContext(this._ctx, this.getState());
		this.enterRule(_localctx, 0, IkalaScriptParser.RULE_literal);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(152);
				_la = this._input.LA(1);
				if (!((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 66060288L) != 0))) {
					this._errHandler.recoverInline(this);
				}
				else {
					if (this._input.LA(1) == Token.EOF) {
						this.matchedEOF = true;
					}
					this._errHandler.reportMatch(this);
					this.consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final LocalVariableDeclarationContext localVariableDeclaration()
		throws RecognitionException {
		LocalVariableDeclarationContext _localctx =
			new LocalVariableDeclarationContext(this._ctx, this.getState());
		this.enterRule(_localctx, 32,
			IkalaScriptParser.RULE_localVariableDeclaration);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(242);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la == IkalaScriptParser.FINAL) {
					{
						this.setState(241);
						this.match(IkalaScriptParser.FINAL);
					}
				}

				this.setState(244);
				this.type();
				this.setState(245);
				this.variableDeclaratorList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final LocalVariableDeclarationStatementContext
		localVariableDeclarationStatement() throws RecognitionException {
		LocalVariableDeclarationStatementContext _localctx =
			new LocalVariableDeclarationStatementContext(this._ctx,
				this.getState());
		this.enterRule(_localctx, 30,
			IkalaScriptParser.RULE_localVariableDeclarationStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(238);
				this.localVariableDeclaration();
				this.setState(239);
				this.match(IkalaScriptParser.SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final MethodInvocationContext methodInvocation()
		throws RecognitionException {
		MethodInvocationContext _localctx =
			new MethodInvocationContext(this._ctx, this.getState());
		this.enterRule(_localctx, 106, IkalaScriptParser.RULE_methodInvocation);
		int _la;
		try {
			this.setState(533);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 44,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(518);
					this.match(IkalaScriptParser.Identifier);
					this.setState(519);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(521);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if ((((_la) & ~0x3f) == 0
						&& ((1L << _la) & 4222674673860608L) != 0)) {
						{
							this.setState(520);
							this.argumentList();
						}
					}

					this.setState(523);
					this.match(IkalaScriptParser.RPAREN);
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(524);
					this.primary();
					this.setState(525);
					this.match(IkalaScriptParser.DOT);
					this.setState(526);
					this.match(IkalaScriptParser.Identifier);
					this.setState(527);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(529);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if ((((_la) & ~0x3f) == 0
						&& ((1L << _la) & 4222674673860608L) != 0)) {
						{
							this.setState(528);
							this.argumentList();
						}
					}

					this.setState(531);
					this.match(IkalaScriptParser.RPAREN);
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final MethodInvocation_extensionContext methodInvocation_extension()
		throws RecognitionException {
		MethodInvocation_extensionContext _localctx =
			new MethodInvocation_extensionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 108,
			IkalaScriptParser.RULE_methodInvocation_extension);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(535);
				this.match(IkalaScriptParser.DOT);
				this.setState(536);
				this.match(IkalaScriptParser.Identifier);
				this.setState(537);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(539);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 4222674673860608L) != 0)) {
					{
						this.setState(538);
						this.argumentList();
					}
				}

				this.setState(541);
				this.match(IkalaScriptParser.RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final MethodInvocation_LHSContext methodInvocation_LHS()
		throws RecognitionException {
		MethodInvocation_LHSContext _localctx =
			new MethodInvocation_LHSContext(this._ctx, this.getState());
		this.enterRule(_localctx, 110,
			IkalaScriptParser.RULE_methodInvocation_LHS);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(543);
				this.match(IkalaScriptParser.Identifier);
				this.setState(544);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(546);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 4222674673860608L) != 0)) {
					{
						this.setState(545);
						this.argumentList();
					}
				}

				this.setState(548);
				this.match(IkalaScriptParser.RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final MultiplicativeExpressionContext multiplicativeExpression()
		throws RecognitionException {
		return this.multiplicativeExpression(0);
	}

	private MultiplicativeExpressionContext multiplicativeExpression(int _p)
		throws RecognitionException {
		ParserRuleContext _parentctx = this._ctx;
		int _parentState = this.getState();
		MultiplicativeExpressionContext _localctx =
			new MultiplicativeExpressionContext(this._ctx, _parentState);
		MultiplicativeExpressionContext _prevctx = _localctx;
		int _startState = 134;
		this.enterRecursionRule(_localctx, 134,
			IkalaScriptParser.RULE_multiplicativeExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(652);
					this.unaryExpression();
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(665);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 60,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							this.setState(663);
							this._errHandler.sync(this);
							switch (this.getInterpreter()
								.adaptivePredict(this._input, 59, this._ctx)) {
								case 1: {
									_localctx =
										new MultiplicativeExpressionContext(
											_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_multiplicativeExpression);
									this.setState(654);
									if (!(this.precpred(this._ctx, 3))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 3)");
									}
									this.setState(655);
									this.match(IkalaScriptParser.MUL);
									this.setState(656);
									this.unaryExpression();
								}
									break;
								case 2: {
									_localctx =
										new MultiplicativeExpressionContext(
											_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_multiplicativeExpression);
									this.setState(657);
									if (!(this.precpred(this._ctx, 2))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 2)");
									}
									this.setState(658);
									this.match(IkalaScriptParser.DIV);
									this.setState(659);
									this.unaryExpression();
								}
									break;
								case 3: {
									_localctx =
										new MultiplicativeExpressionContext(
											_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_multiplicativeExpression);
									this.setState(660);
									if (!(this.precpred(this._ctx, 1))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 1)");
									}
									this.setState(661);
									this.match(IkalaScriptParser.MOD);
									this.setState(662);
									this.unaryExpression();
								}
									break;
							}
						}
					}
					this.setState(667);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						60, this._ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	private boolean multiplicativeExpression_sempred(
		MultiplicativeExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
			case 10:
				return this.precpred(this._ctx, 3);
			case 11:
				return this.precpred(this._ctx, 2);
			case 12:
				return this.precpred(this._ctx, 1);
		}
		return true;
	}

	public final NumericTypeContext numericType() throws RecognitionException {
		NumericTypeContext _localctx =
			new NumericTypeContext(this._ctx, this.getState());
		this.enterRule(_localctx, 4, IkalaScriptParser.RULE_numericType);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(159);
				_la = this._input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 33040L) != 0))) {
					this._errHandler.recoverInline(this);
				}
				else {
					if (this._input.LA(1) == Token.EOF) {
						this.matchedEOF = true;
					}
					this._errHandler.reportMatch(this);
					this.consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final PostDecrementExpressionContext postDecrementExpression()
		throws RecognitionException {
		PostDecrementExpressionContext _localctx =
			new PostDecrementExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 148,
			IkalaScriptParser.RULE_postDecrementExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(702);
				this.postfixExpression();
				this.setState(703);
				this.match(IkalaScriptParser.DEC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final PostfixExpressionContext postfixExpression()
		throws RecognitionException {
		PostfixExpressionContext _localctx =
			new PostfixExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 144,
			IkalaScriptParser.RULE_postfixExpression);
		int _la;
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(691);
				this._errHandler.sync(this);
				switch (this.getInterpreter().adaptivePredict(this._input, 63,
					this._ctx)) {
					case 1: {
						this.setState(689);
						this.primary();
					}
						break;
					case 2: {
						this.setState(690);
						this.match(IkalaScriptParser.Identifier);
					}
						break;
				}
				this.setState(696);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 64,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							{
								this.setState(693);
								_la = this._input.LA(1);
								if (!(_la == IkalaScriptParser.INC
									|| _la == IkalaScriptParser.DEC)) {
									this._errHandler.recoverInline(this);
								}
								else {
									if (this._input.LA(1) == Token.EOF) {
										this.matchedEOF = true;
									}
									this._errHandler.reportMatch(this);
									this.consume();
								}
							}
						}
					}
					this.setState(698);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						64, this._ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final PostIncrementExpressionContext postIncrementExpression()
		throws RecognitionException {
		PostIncrementExpressionContext _localctx =
			new PostIncrementExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 146,
			IkalaScriptParser.RULE_postIncrementExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(699);
				this.postfixExpression();
				this.setState(700);
				this.match(IkalaScriptParser.INC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final PreDecrementExpressionContext preDecrementExpression()
		throws RecognitionException {
		PreDecrementExpressionContext _localctx =
			new PreDecrementExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 140,
			IkalaScriptParser.RULE_preDecrementExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(680);
				this.match(IkalaScriptParser.DEC);
				this.setState(681);
				this.unaryExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final PreIncrementExpressionContext preIncrementExpression()
		throws RecognitionException {
		PreIncrementExpressionContext _localctx =
			new PreIncrementExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 138,
			IkalaScriptParser.RULE_preIncrementExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(677);
				this.match(IkalaScriptParser.INC);
				this.setState(678);
				this.unaryExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx =
			new PrimaryContext(this._ctx, this.getState());
		this.enterRule(_localctx, 88, IkalaScriptParser.RULE_primary);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(430);
					this.primary_LHS();
				}
				this.setState(434);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 30,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							{
								this.setState(431);
								this.primary_extension();
							}
						}
					}
					this.setState(436);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						30, this._ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final Primary_extensionContext primary_extension()
		throws RecognitionException {
		Primary_extensionContext _localctx =
			new Primary_extensionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 92, IkalaScriptParser.RULE_primary_extension);
		try {
			this.setState(447);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 32,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(445);
					this.arrayAccess_extension();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(446);
					this.methodInvocation_extension();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final Primary_extension_accessContext primary_extension_access()
		throws RecognitionException {
		Primary_extension_accessContext _localctx =
			new Primary_extension_accessContext(this._ctx, this.getState());
		this.enterRule(_localctx, 94,
			IkalaScriptParser.RULE_primary_extension_access);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(449);
				this.methodInvocation_extension();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final Primary_LHSContext primary_LHS() throws RecognitionException {
		Primary_LHSContext _localctx =
			new Primary_LHSContext(this._ctx, this.getState());
		this.enterRule(_localctx, 96, IkalaScriptParser.RULE_primary_LHS);
		try {
			this.setState(459);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 33,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(451);
					this.literal();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(452);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(453);
					this.expression();
					this.setState(454);
					this.match(IkalaScriptParser.RPAREN);
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(456);
					this.arrayAccess_LHS();
				}
					break;
				case 4:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(457);
					this.methodInvocation_LHS();
				}
					break;
				case 5:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(458);
					this.match(IkalaScriptParser.Identifier);
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final Primary_LHS_accessContext primary_LHS_access()
		throws RecognitionException {
		Primary_LHS_accessContext _localctx =
			new Primary_LHS_accessContext(this._ctx, this.getState());
		this.enterRule(_localctx, 98,
			IkalaScriptParser.RULE_primary_LHS_access);
		try {
			this.setState(467);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case IntegerLiteral:
				case FloatingPointLiteral:
				case BooleanLiteral:
				case CharacterLiteral:
				case StringLiteral:
				case NullLiteral:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(461);
					this.literal();
				}
					break;
				case LPAREN:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(462);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(463);
					this.expression();
					this.setState(464);
					this.match(IkalaScriptParser.RPAREN);
				}
					break;
				case Identifier:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(466);
					this.methodInvocation_LHS();
				}
					break;
				default:
					throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final PrimitiveTypeContext primitiveType()
		throws RecognitionException {
		PrimitiveTypeContext _localctx =
			new PrimitiveTypeContext(this._ctx, this.getState());
		this.enterRule(_localctx, 2, IkalaScriptParser.RULE_primitiveType);
		try {
			this.setState(157);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case CHAR:
				case DOUBLE:
				case INT:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(154);
					this.numericType();
				}
					break;
				case BOOLEAN:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(155);
					this.match(IkalaScriptParser.BOOLEAN);
				}
					break;
				case STRING:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(156);
					this.match(IkalaScriptParser.STRING);
				}
					break;
				default:
					throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final ReferenceTypeContext referenceType()
		throws RecognitionException {
		ReferenceTypeContext _localctx =
			new ReferenceTypeContext(this._ctx, this.getState());
		this.enterRule(_localctx, 6, IkalaScriptParser.RULE_referenceType);
		try {
			this.setState(164);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 1,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(161);
					this.classOrInterfaceType();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(162);
					this.match(IkalaScriptParser.Identifier);
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(163);
					this.arrayType();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final RelationalExpressionContext relationalExpression()
		throws RecognitionException {
		return this.relationalExpression(0);
	}

	private RelationalExpressionContext relationalExpression(int _p)
		throws RecognitionException {
		ParserRuleContext _parentctx = this._ctx;
		int _parentState = this.getState();
		RelationalExpressionContext _localctx =
			new RelationalExpressionContext(this._ctx, _parentState);
		RelationalExpressionContext _prevctx = _localctx;
		int _startState = 130;
		this.enterRecursionRule(_localctx, 130,
			IkalaScriptParser.RULE_relationalExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(618);
					this.additiveExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(634);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 56,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							this.setState(632);
							this._errHandler.sync(this);
							switch (this.getInterpreter()
								.adaptivePredict(this._input, 55, this._ctx)) {
								case 1: {
									_localctx = new RelationalExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_relationalExpression);
									this.setState(620);
									if (!(this.precpred(this._ctx, 4))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 4)");
									}
									this.setState(621);
									this.match(IkalaScriptParser.LT);
									this.setState(622);
									this.additiveExpression(0);
								}
									break;
								case 2: {
									_localctx = new RelationalExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_relationalExpression);
									this.setState(623);
									if (!(this.precpred(this._ctx, 3))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 3)");
									}
									this.setState(624);
									this.match(IkalaScriptParser.GT);
									this.setState(625);
									this.additiveExpression(0);
								}
									break;
								case 3: {
									_localctx = new RelationalExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_relationalExpression);
									this.setState(626);
									if (!(this.precpred(this._ctx, 2))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 2)");
									}
									this.setState(627);
									this.match(IkalaScriptParser.LTE);
									this.setState(628);
									this.additiveExpression(0);
								}
									break;
								case 4: {
									_localctx = new RelationalExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_relationalExpression);
									this.setState(629);
									if (!(this.precpred(this._ctx, 1))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 1)");
									}
									this.setState(630);
									this.match(IkalaScriptParser.GTE);
									this.setState(631);
									this.additiveExpression(0);
								}
									break;
							}
						}
					}
					this.setState(636);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						56, this._ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	private boolean relationalExpression_sempred(
		RelationalExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
			case 4:
				return this.precpred(this._ctx, 4);
			case 5:
				return this.precpred(this._ctx, 3);
			case 6:
				return this.precpred(this._ctx, 2);
			case 7:
				return this.precpred(this._ctx, 1);
		}
		return true;
	}

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex,
		int predIndex) {
		switch (ruleIndex) {
			case 62:
				return this.conditionalOrExpression_sempred(
					(ConditionalOrExpressionContext) _localctx, predIndex);
			case 63:
				return this.conditionalAndExpression_sempred(
					(ConditionalAndExpressionContext) _localctx, predIndex);
			case 64:
				return this.equalityExpression_sempred(
					(EqualityExpressionContext) _localctx, predIndex);
			case 65:
				return this.relationalExpression_sempred(
					(RelationalExpressionContext) _localctx, predIndex);
			case 66:
				return this.additiveExpression_sempred(
					(AdditiveExpressionContext) _localctx, predIndex);
			case 67:
				return this.multiplicativeExpression_sempred(
					(MultiplicativeExpressionContext) _localctx, predIndex);
		}
		return true;
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx =
			new StatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 34, IkalaScriptParser.RULE_statement);
		try {
			this.setState(253);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 14,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(247);
					this.statementWithoutTrailingSubstatement();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(248);
					this.labeledStatement();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(249);
					this.ifThenStatement();
				}
					break;
				case 4:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(250);
					this.ifThenElseStatement();
				}
					break;
				case 5:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(251);
					this.whileStatement();
				}
					break;
				case 6:
					this.enterOuterAlt(_localctx, 6); {
					this.setState(252);
					this.forStatement();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final StatementExpressionContext statementExpression()
		throws RecognitionException {
		StatementExpressionContext _localctx =
			new StatementExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 50,
			IkalaScriptParser.RULE_statementExpression);
		try {
			this.setState(293);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 17,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(287);
					this.assignment();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(288);
					this.preIncrementExpression();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(289);
					this.preDecrementExpression();
				}
					break;
				case 4:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(290);
					this.postIncrementExpression();
				}
					break;
				case 5:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(291);
					this.postDecrementExpression();
				}
					break;
				case 6:
					this.enterOuterAlt(_localctx, 6); {
					this.setState(292);
					this.methodInvocation();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final StatementExpressionListContext statementExpressionList()
		throws RecognitionException {
		StatementExpressionListContext _localctx =
			new StatementExpressionListContext(this._ctx, this.getState());
		this.enterRule(_localctx, 78,
			IkalaScriptParser.RULE_statementExpressionList);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(409);
				this.statementExpression();
				this.setState(414);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.COMMA) {
					{
						{
							this.setState(410);
							this.match(IkalaScriptParser.COMMA);
							this.setState(411);
							this.statementExpression();
						}
					}
					this.setState(416);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final StatementNoShortIfContext statementNoShortIf()
		throws RecognitionException {
		StatementNoShortIfContext _localctx =
			new StatementNoShortIfContext(this._ctx, this.getState());
		this.enterRule(_localctx, 36,
			IkalaScriptParser.RULE_statementNoShortIf);
		try {
			this.setState(260);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 15,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(255);
					this.statementWithoutTrailingSubstatement();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(256);
					this.labeledStatementNoShortIf();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(257);
					this.ifThenElseStatementNoShortIf();
				}
					break;
				case 4:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(258);
					this.whileStatementNoShortIf();
				}
					break;
				case 5:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(259);
					this.forStatementNoShortIf();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final StatementWithoutTrailingSubstatementContext
		statementWithoutTrailingSubstatement() throws RecognitionException {
		StatementWithoutTrailingSubstatementContext _localctx =
			new StatementWithoutTrailingSubstatementContext(this._ctx,
				this.getState());
		this.enterRule(_localctx, 38,
			IkalaScriptParser.RULE_statementWithoutTrailingSubstatement);
		try {
			this.setState(271);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case LBRACE:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(262);
					this.block();
				}
					break;
				case SEMICOLON:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(263);
					this.emptyStatement();
				}
					break;
				case IntegerLiteral:
				case FloatingPointLiteral:
				case BooleanLiteral:
				case CharacterLiteral:
				case StringLiteral:
				case NullLiteral:
				case Identifier:
				case LPAREN:
				case INC:
				case DEC:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(264);
					this.expressionStatement();
				}
					break;
				case SWITCH:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(265);
					this.switchStatement();
				}
					break;
				case DO:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(266);
					this.doStatement();
				}
					break;
				case BREAK:
					this.enterOuterAlt(_localctx, 6); {
					this.setState(267);
					this.breakStatement();
				}
					break;
				case CONTINUE:
					this.enterOuterAlt(_localctx, 7); {
					this.setState(268);
					this.continueStatement();
				}
					break;
				case GOTO:
					this.enterOuterAlt(_localctx, 8); {
					this.setState(269);
					this.gotoStatement();
				}
					break;
				case EXIT:
					this.enterOuterAlt(_localctx, 9); {
					this.setState(270);
					this.exitStatement();
				}
					break;
				default:
					throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final SwitchBlockContext switchBlock() throws RecognitionException {
		SwitchBlockContext _localctx =
			new SwitchBlockContext(this._ctx, this.getState());
		this.enterRule(_localctx, 60, IkalaScriptParser.RULE_switchBlock);
		int _la;
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(323);
				this.match(IkalaScriptParser.LBRACE);
				this.setState(327);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 18,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							{
								this.setState(324);
								this.switchBlockStatementGroup();
							}
						}
					}
					this.setState(329);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						18, this._ctx);
				}
				this.setState(333);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.CASE
					|| _la == IkalaScriptParser.DEFAULT) {
					{
						{
							this.setState(330);
							this.switchLabel();
						}
					}
					this.setState(335);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.setState(336);
				this.match(IkalaScriptParser.RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final SwitchBlockStatementGroupContext switchBlockStatementGroup()
		throws RecognitionException {
		SwitchBlockStatementGroupContext _localctx =
			new SwitchBlockStatementGroupContext(this._ctx, this.getState());
		this.enterRule(_localctx, 62,
			IkalaScriptParser.RULE_switchBlockStatementGroup);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(339);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
						{
							this.setState(338);
							this.switchLabel();
						}
					}
					this.setState(341);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				while (_la == IkalaScriptParser.CASE
					|| _la == IkalaScriptParser.DEFAULT);
				this.setState(343);
				this.blockStatements();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final SwitchLabelContext switchLabel() throws RecognitionException {
		SwitchLabelContext _localctx =
			new SwitchLabelContext(this._ctx, this.getState());
		this.enterRule(_localctx, 64, IkalaScriptParser.RULE_switchLabel);
		try {
			this.setState(351);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case CASE:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(345);
					this.match(IkalaScriptParser.CASE);
					this.setState(346);
					this.expression();
					this.setState(347);
					this.match(IkalaScriptParser.COLON);
				}
					break;
				case DEFAULT:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(349);
					this.match(IkalaScriptParser.DEFAULT);
					this.setState(350);
					this.match(IkalaScriptParser.COLON);
				}
					break;
				default:
					throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final SwitchStatementContext switchStatement()
		throws RecognitionException {
		SwitchStatementContext _localctx =
			new SwitchStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 58, IkalaScriptParser.RULE_switchStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(317);
				this.match(IkalaScriptParser.SWITCH);
				this.setState(318);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(319);
				this.expression();
				this.setState(320);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(321);
				this.switchBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(this._ctx, this.getState());
		this.enterRule(_localctx, 20, IkalaScriptParser.RULE_type);
		try {
			this.setState(212);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 8,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(210);
					this.primitiveType();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(211);
					this.referenceType();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final UnaryExpressionContext unaryExpression()
		throws RecognitionException {
		UnaryExpressionContext _localctx =
			new UnaryExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 136, IkalaScriptParser.RULE_unaryExpression);
		try {
			this.setState(675);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case INC:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(668);
					this.preIncrementExpression();
				}
					break;
				case DEC:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(669);
					this.preDecrementExpression();
				}
					break;
				case ADD:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(670);
					this.match(IkalaScriptParser.ADD);
					this.setState(671);
					this.unaryExpression();
				}
					break;
				case SUB:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(672);
					this.match(IkalaScriptParser.SUB);
					this.setState(673);
					this.unaryExpression();
				}
					break;
				case IntegerLiteral:
				case FloatingPointLiteral:
				case BooleanLiteral:
				case CharacterLiteral:
				case StringLiteral:
				case NullLiteral:
				case Identifier:
				case LPAREN:
				case NOT:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(674);
					this.unaryExpressionNotPlusMinus();
				}
					break;
				default:
					throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final UnaryExpressionNotPlusMinusContext
		unaryExpressionNotPlusMinus() throws RecognitionException {
		UnaryExpressionNotPlusMinusContext _localctx =
			new UnaryExpressionNotPlusMinusContext(this._ctx, this.getState());
		this.enterRule(_localctx, 142,
			IkalaScriptParser.RULE_unaryExpressionNotPlusMinus);
		try {
			this.setState(687);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 62,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(683);
					this.postfixExpression();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(684);
					this.match(IkalaScriptParser.NOT);
					this.setState(685);
					this.unaryExpression();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(686);
					this.castExpression();
				}
					break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final VariableDeclaratorContext variableDeclarator()
		throws RecognitionException {
		VariableDeclaratorContext _localctx =
			new VariableDeclaratorContext(this._ctx, this.getState());
		this.enterRule(_localctx, 16,
			IkalaScriptParser.RULE_variableDeclarator);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(201);
				this.variableDeclaratorId();
				this.setState(204);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la == IkalaScriptParser.ASSIGN) {
					{
						this.setState(202);
						this.match(IkalaScriptParser.ASSIGN);
						this.setState(203);
						this.expression();
					}
				}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final VariableDeclaratorIdContext variableDeclaratorId()
		throws RecognitionException {
		VariableDeclaratorIdContext _localctx =
			new VariableDeclaratorIdContext(this._ctx, this.getState());
		this.enterRule(_localctx, 18,
			IkalaScriptParser.RULE_variableDeclaratorId);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(206);
				this.match(IkalaScriptParser.Identifier);
				this.setState(208);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la == IkalaScriptParser.LBRACK) {
					{
						this.setState(207);
						this.dims();
					}
				}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final VariableDeclaratorListContext variableDeclaratorList()
		throws RecognitionException {
		VariableDeclaratorListContext _localctx =
			new VariableDeclaratorListContext(this._ctx, this.getState());
		this.enterRule(_localctx, 14,
			IkalaScriptParser.RULE_variableDeclaratorList);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(193);
				this.variableDeclarator();
				this.setState(198);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.COMMA) {
					{
						{
							this.setState(194);
							this.match(IkalaScriptParser.COMMA);
							this.setState(195);
							this.variableDeclarator();
						}
					}
					this.setState(200);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final WhileStatementContext whileStatement()
		throws RecognitionException {
		WhileStatementContext _localctx =
			new WhileStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 66, IkalaScriptParser.RULE_whileStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(353);
				this.match(IkalaScriptParser.WHILE);
				this.setState(354);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(355);
				this.expression();
				this.setState(356);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(357);
				this.statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public final WhileStatementNoShortIfContext whileStatementNoShortIf()
		throws RecognitionException {
		WhileStatementNoShortIfContext _localctx =
			new WhileStatementNoShortIfContext(this._ctx, this.getState());
		this.enterRule(_localctx, 68,
			IkalaScriptParser.RULE_whileStatementNoShortIf);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(359);
				this.match(IkalaScriptParser.WHILE);
				this.setState(360);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(361);
				this.expression();
				this.setState(362);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(363);
				this.statementNoShortIf();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
}