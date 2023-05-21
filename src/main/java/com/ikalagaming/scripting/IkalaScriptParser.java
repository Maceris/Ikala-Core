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
	public static class ArrayAccessContext extends ParserRuleContext {
		public ArrayAccessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
	public static class Primary_extensionContext extends ParserRuleContext {
		public Primary_extensionContext(ParserRuleContext parent,
			int invokingState) {
			super(parent, invokingState);
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

		public ArrayAccessContext arrayAccess() {
			return this.getRuleContext(ArrayAccessContext.class, 0);
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
		RULE_exitStatement = 43, RULE_primary = 44, RULE_primary_extension = 45,
		RULE_primary_LHS = 46, RULE_primary_LHS_access = 47,
		RULE_arrayAccess = 48, RULE_methodInvocation = 49,
		RULE_methodInvocation_extension = 50, RULE_methodInvocation_LHS = 51,
		RULE_argumentList = 52, RULE_expression = 53, RULE_assignment = 54,
		RULE_leftHandSide = 55, RULE_assignmentOperator = 56,
		RULE_conditionalExpression = 57, RULE_conditionalOrExpression = 58,
		RULE_conditionalAndExpression = 59, RULE_equalityExpression = 60,
		RULE_relationalExpression = 61, RULE_additiveExpression = 62,
		RULE_multiplicativeExpression = 63, RULE_unaryExpression = 64,
		RULE_preIncrementExpression = 65, RULE_preDecrementExpression = 66,
		RULE_unaryExpressionNotPlusMinus = 67, RULE_postfixExpression = 68,
		RULE_postIncrementExpression = 69, RULE_postDecrementExpression = 70,
		RULE_castExpression = 71;

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
		"\u0004\u0001>\u0292\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"
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
			+ "F\u0007F\u0002G\u0007G\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"
			+ "\u0001\u0001\u0003\u0001\u0096\b\u0001\u0001\u0002\u0001\u0002\u0001\u0003"
			+ "\u0001\u0003\u0001\u0003\u0003\u0003\u009d\b\u0003\u0001\u0004\u0001\u0004"
			+ "\u0001\u0004\u0005\u0004\u00a2\b\u0004\n\u0004\f\u0004\u00a5\t\u0004\u0001"
			+ "\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"
			+ "\u0005\u0001\u0005\u0003\u0005\u00af\b\u0005\u0001\u0006\u0001\u0006\u0001"
			+ "\u0006\u0001\u0006\u0005\u0006\u00b5\b\u0006\n\u0006\f\u0006\u00b8\t\u0006"
			+ "\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00bd\b\u0007\n\u0007"
			+ "\f\u0007\u00c0\t\u0007\u0001\b\u0001\b\u0001\b\u0003\b\u00c5\b\b\u0001"
			+ "\t\u0001\t\u0003\t\u00c9\b\t\u0001\n\u0001\n\u0003\n\u00cd\b\n\u0001\u000b"
			+ "\u0005\u000b\u00d0\b\u000b\n\u000b\f\u000b\u00d3\t\u000b\u0001\u000b\u0001"
			+ "\u000b\u0001\f\u0001\f\u0003\f\u00d9\b\f\u0001\f\u0001\f\u0001\r\u0004"
			+ "\r\u00de\b\r\u000b\r\f\r\u00df\u0001\u000e\u0001\u000e\u0001\u000e\u0003"
			+ "\u000e\u00e5\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0003"
			+ "\u0010\u00eb\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001"
			+ "\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00f6"
			+ "\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003"
			+ "\u0012\u00fd\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"
			+ "\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0108"
			+ "\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001"
			+ "\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001"
			+ "\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001"
			+ "\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u011e\b\u0019\u0001\u001a\u0001"
			+ "\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001"
			+ "\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"
			+ "\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"
			+ "\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001"
			+ "\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0005\u001e\u013e"
			+ "\b\u001e\n\u001e\f\u001e\u0141\t\u001e\u0001\u001e\u0005\u001e\u0144\b"
			+ "\u001e\n\u001e\f\u001e\u0147\t\u001e\u0001\u001e\u0001\u001e\u0001\u001f"
			+ "\u0004\u001f\u014c\b\u001f\u000b\u001f\f\u001f\u014d\u0001\u001f\u0001"
			+ "\u001f\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0003 \u0158\b \u0001"
			+ "!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001\"\u0001"
			+ "\"\u0001\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001"
			+ "$\u0001$\u0001$\u0003$\u0171\b$\u0001$\u0001$\u0003$\u0175\b$\u0001$\u0001"
			+ "$\u0003$\u0179\b$\u0001$\u0001$\u0001$\u0001%\u0001%\u0001%\u0003%\u0181"
			+ "\b%\u0001%\u0001%\u0003%\u0185\b%\u0001%\u0001%\u0003%\u0189\b%\u0001"
			+ "%\u0001%\u0001%\u0001&\u0001&\u0003&\u0190\b&\u0001\'\u0001\'\u0001\'"
			+ "\u0005\'\u0195\b\'\n\'\f\'\u0198\t\'\u0001(\u0001(\u0001(\u0001)\u0001"
			+ ")\u0001)\u0001*\u0001*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001,\u0001"
			+ ",\u0005,\u01a9\b,\n,\f,\u01ac\t,\u0001-\u0001-\u0001.\u0001.\u0001.\u0001"
			+ ".\u0001.\u0001.\u0001.\u0001.\u0003.\u01b8\b.\u0001/\u0001/\u0001/\u0001"
			+ "/\u0001/\u0001/\u0003/\u01c0\b/\u00010\u00010\u00010\u00010\u00010\u0004"
			+ "0\u01c7\b0\u000b0\f0\u01c8\u00011\u00011\u00011\u00031\u01ce\b1\u0001"
			+ "1\u00011\u00011\u00011\u00011\u00011\u00031\u01d6\b1\u00011\u00011\u0003"
			+ "1\u01da\b1\u00012\u00012\u00012\u00012\u00032\u01e0\b2\u00012\u00012\u0001"
			+ "3\u00013\u00013\u00033\u01e7\b3\u00013\u00013\u00014\u00014\u00014\u0005"
			+ "4\u01ee\b4\n4\f4\u01f1\t4\u00015\u00015\u00035\u01f5\b5\u00016\u00016"
			+ "\u00016\u00016\u00017\u00017\u00037\u01fd\b7\u00018\u00018\u00019\u0001"
			+ "9\u00019\u00019\u00019\u00019\u00019\u00039\u0208\b9\u0001:\u0001:\u0001"
			+ ":\u0001:\u0001:\u0001:\u0005:\u0210\b:\n:\f:\u0213\t:\u0001;\u0001;\u0001"
			+ ";\u0001;\u0001;\u0001;\u0005;\u021b\b;\n;\f;\u021e\t;\u0001<\u0001<\u0001"
			+ "<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0005<\u0229\b<\n<\f<\u022c"
			+ "\t<\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001"
			+ "=\u0001=\u0001=\u0001=\u0001=\u0001=\u0005=\u023d\b=\n=\f=\u0240\t=\u0001"
			+ ">\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0005>\u024b"
			+ "\b>\n>\f>\u024e\t>\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001"
			+ "?\u0001?\u0001?\u0001?\u0001?\u0005?\u025c\b?\n?\f?\u025f\t?\u0001@\u0001"
			+ "@\u0001@\u0001@\u0001@\u0001@\u0001@\u0003@\u0268\b@\u0001A\u0001A\u0001"
			+ "A\u0001B\u0001B\u0001B\u0001C\u0001C\u0001C\u0001C\u0003C\u0274\bC\u0001"
			+ "D\u0001D\u0003D\u0278\bD\u0001D\u0005D\u027b\bD\nD\fD\u027e\tD\u0001E"
			+ "\u0001E\u0001E\u0001F\u0001F\u0001F\u0001G\u0001G\u0001G\u0001G\u0001"
			+ "G\u0001G\u0001G\u0001G\u0001G\u0001G\u0003G\u0290\bG\u0001G\u0000\u0006"
			+ "tvxz|~H\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"
			+ "\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080"
			+ "\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0000\u0004\u0001\u0000\u0014"
			+ "\u0019\u0003\u0000\u0004\u0004\b\b\u000f\u000f\u0002\u0000$$7;\u0001\u0000"
			+ "01\u02a4\u0000\u0090\u0001\u0000\u0000\u0000\u0002\u0095\u0001\u0000\u0000"
			+ "\u0000\u0004\u0097\u0001\u0000\u0000\u0000\u0006\u009c\u0001\u0000\u0000"
			+ "\u0000\b\u009e\u0001\u0000\u0000\u0000\n\u00ae\u0001\u0000\u0000\u0000"
			+ "\f\u00b0\u0001\u0000\u0000\u0000\u000e\u00b9\u0001\u0000\u0000\u0000\u0010"
			+ "\u00c1\u0001\u0000\u0000\u0000\u0012\u00c6\u0001\u0000\u0000\u0000\u0014"
			+ "\u00cc\u0001\u0000\u0000\u0000\u0016\u00d1\u0001\u0000\u0000\u0000\u0018"
			+ "\u00d6\u0001\u0000\u0000\u0000\u001a\u00dd\u0001\u0000\u0000\u0000\u001c"
			+ "\u00e4\u0001\u0000\u0000\u0000\u001e\u00e6\u0001\u0000\u0000\u0000 \u00ea"
			+ "\u0001\u0000\u0000\u0000\"\u00f5\u0001\u0000\u0000\u0000$\u00fc\u0001"
			+ "\u0000\u0000\u0000&\u0107\u0001\u0000\u0000\u0000(\u0109\u0001\u0000\u0000"
			+ "\u0000*\u010c\u0001\u0000\u0000\u0000,\u010f\u0001\u0000\u0000\u0000."
			+ "\u0112\u0001\u0000\u0000\u00000\u0114\u0001\u0000\u0000\u00002\u011d\u0001"
			+ "\u0000\u0000\u00004\u011f\u0001\u0000\u0000\u00006\u0125\u0001\u0000\u0000"
			+ "\u00008\u012d\u0001\u0000\u0000\u0000:\u0135\u0001\u0000\u0000\u0000<"
			+ "\u013b\u0001\u0000\u0000\u0000>\u014b\u0001\u0000\u0000\u0000@\u0157\u0001"
			+ "\u0000\u0000\u0000B\u0159\u0001\u0000\u0000\u0000D\u015f\u0001\u0000\u0000"
			+ "\u0000F\u0165\u0001\u0000\u0000\u0000H\u016d\u0001\u0000\u0000\u0000J"
			+ "\u017d\u0001\u0000\u0000\u0000L\u018f\u0001\u0000\u0000\u0000N\u0191\u0001"
			+ "\u0000\u0000\u0000P\u0199\u0001\u0000\u0000\u0000R\u019c\u0001\u0000\u0000"
			+ "\u0000T\u019f\u0001\u0000\u0000\u0000V\u01a3\u0001\u0000\u0000\u0000X"
			+ "\u01a6\u0001\u0000\u0000\u0000Z\u01ad\u0001\u0000\u0000\u0000\\\u01b7"
			+ "\u0001\u0000\u0000\u0000^\u01bf\u0001\u0000\u0000\u0000`\u01c1\u0001\u0000"
			+ "\u0000\u0000b\u01d9\u0001\u0000\u0000\u0000d\u01db\u0001\u0000\u0000\u0000"
			+ "f\u01e3\u0001\u0000\u0000\u0000h\u01ea\u0001\u0000\u0000\u0000j\u01f4"
			+ "\u0001\u0000\u0000\u0000l\u01f6\u0001\u0000\u0000\u0000n\u01fc\u0001\u0000"
			+ "\u0000\u0000p\u01fe\u0001\u0000\u0000\u0000r\u0207\u0001\u0000\u0000\u0000"
			+ "t\u0209\u0001\u0000\u0000\u0000v\u0214\u0001\u0000\u0000\u0000x\u021f"
			+ "\u0001\u0000\u0000\u0000z\u022d\u0001\u0000\u0000\u0000|\u0241\u0001\u0000"
			+ "\u0000\u0000~\u024f\u0001\u0000\u0000\u0000\u0080\u0267\u0001\u0000\u0000"
			+ "\u0000\u0082\u0269\u0001\u0000\u0000\u0000\u0084\u026c\u0001\u0000\u0000"
			+ "\u0000\u0086\u0273\u0001\u0000\u0000\u0000\u0088\u0277\u0001\u0000\u0000"
			+ "\u0000\u008a\u027f\u0001\u0000\u0000\u0000\u008c\u0282\u0001\u0000\u0000"
			+ "\u0000\u008e\u028f\u0001\u0000\u0000\u0000\u0090\u0091\u0007\u0000\u0000"
			+ "\u0000\u0091\u0001\u0001\u0000\u0000\u0000\u0092\u0096\u0003\u0004\u0002"
			+ "\u0000\u0093\u0096\u0005\u0001\u0000\u0000\u0094\u0096\u0005\u0010\u0000"
			+ "\u0000\u0095\u0092\u0001\u0000\u0000\u0000\u0095\u0093\u0001\u0000\u0000"
			+ "\u0000\u0095\u0094\u0001\u0000\u0000\u0000\u0096\u0003\u0001\u0000\u0000"
			+ "\u0000\u0097\u0098\u0007\u0001\u0000\u0000\u0098\u0005\u0001\u0000\u0000"
			+ "\u0000\u0099\u009d\u0003\b\u0004\u0000\u009a\u009d\u0005\u001a\u0000\u0000"
			+ "\u009b\u009d\u0003\n\u0005\u0000\u009c\u0099\u0001\u0000\u0000\u0000\u009c"
			+ "\u009a\u0001\u0000\u0000\u0000\u009c\u009b\u0001\u0000\u0000\u0000\u009d"
			+ "\u0007\u0001\u0000\u0000\u0000\u009e\u00a3\u0005\u001a\u0000\u0000\u009f"
			+ "\u00a0\u0005#\u0000\u0000\u00a0\u00a2\u0005\u001a\u0000\u0000\u00a1\u009f"
			+ "\u0001\u0000\u0000\u0000\u00a2\u00a5\u0001\u0000\u0000\u0000\u00a3\u00a1"
			+ "\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000\u0000\u00a4\t\u0001"
			+ "\u0000\u0000\u0000\u00a5\u00a3\u0001\u0000\u0000\u0000\u00a6\u00a7\u0003"
			+ "\u0002\u0001\u0000\u00a7\u00a8\u0003\f\u0006\u0000\u00a8\u00af\u0001\u0000"
			+ "\u0000\u0000\u00a9\u00aa\u0003\b\u0004\u0000\u00aa\u00ab\u0003\f\u0006"
			+ "\u0000\u00ab\u00af\u0001\u0000\u0000\u0000\u00ac\u00ad\u0005\u001a\u0000"
			+ "\u0000\u00ad\u00af\u0003\f\u0006\u0000\u00ae\u00a6\u0001\u0000\u0000\u0000"
			+ "\u00ae\u00a9\u0001\u0000\u0000\u0000\u00ae\u00ac\u0001\u0000\u0000\u0000"
			+ "\u00af\u000b\u0001\u0000\u0000\u0000\u00b0\u00b1\u0005\u001f\u0000\u0000"
			+ "\u00b1\u00b6\u0005 \u0000\u0000\u00b2\u00b3\u0005\u001f\u0000\u0000\u00b3"
			+ "\u00b5\u0005 \u0000\u0000\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b5\u00b8"
			+ "\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b6\u00b7"
			+ "\u0001\u0000\u0000\u0000\u00b7\r\u0001\u0000\u0000\u0000\u00b8\u00b6\u0001"
			+ "\u0000\u0000\u0000\u00b9\u00be\u0003\u0010\b\u0000\u00ba\u00bb\u0005\""
			+ "\u0000\u0000\u00bb\u00bd\u0003\u0010\b\u0000\u00bc\u00ba\u0001\u0000\u0000"
			+ "\u0000\u00bd\u00c0\u0001\u0000\u0000\u0000\u00be\u00bc\u0001\u0000\u0000"
			+ "\u0000\u00be\u00bf\u0001\u0000\u0000\u0000\u00bf\u000f\u0001\u0000\u0000"
			+ "\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c1\u00c4\u0003\u0012\t\u0000"
			+ "\u00c2\u00c3\u0005$\u0000\u0000\u00c3\u00c5\u0003j5\u0000\u00c4\u00c2"
			+ "\u0001\u0000\u0000\u0000\u00c4\u00c5\u0001\u0000\u0000\u0000\u00c5\u0011"
			+ "\u0001\u0000\u0000\u0000\u00c6\u00c8\u0005\u001a\u0000\u0000\u00c7\u00c9"
			+ "\u0003\f\u0006\u0000\u00c8\u00c7\u0001\u0000\u0000\u0000\u00c8\u00c9\u0001"
			+ "\u0000\u0000\u0000\u00c9\u0013\u0001\u0000\u0000\u0000\u00ca\u00cd\u0003"
			+ "\u0002\u0001\u0000\u00cb\u00cd\u0003\u0006\u0003\u0000\u00cc\u00ca\u0001"
			+ "\u0000\u0000\u0000\u00cc\u00cb\u0001\u0000\u0000\u0000\u00cd\u0015\u0001"
			+ "\u0000\u0000\u0000\u00ce\u00d0\u0003\u001c\u000e\u0000\u00cf\u00ce\u0001"
			+ "\u0000\u0000\u0000\u00d0\u00d3\u0001\u0000\u0000\u0000\u00d1\u00cf\u0001"
			+ "\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d4\u0001"
			+ "\u0000\u0000\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000\u00d4\u00d5\u0005"
			+ "\u0000\u0000\u0001\u00d5\u0017\u0001\u0000\u0000\u0000\u00d6\u00d8\u0005"
			+ "\u001d\u0000\u0000\u00d7\u00d9\u0003\u001a\r\u0000\u00d8\u00d7\u0001\u0000"
			+ "\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u00da\u0001\u0000"
			+ "\u0000\u0000\u00da\u00db\u0005\u001e\u0000\u0000\u00db\u0019\u0001\u0000"
			+ "\u0000\u0000\u00dc\u00de\u0003\u001c\u000e\u0000\u00dd\u00dc\u0001\u0000"
			+ "\u0000\u0000\u00de\u00df\u0001\u0000\u0000\u0000\u00df\u00dd\u0001\u0000"
			+ "\u0000\u0000\u00df\u00e0\u0001\u0000\u0000\u0000\u00e0\u001b\u0001\u0000"
			+ "\u0000\u0000\u00e1\u00e5\u0003\u001e\u000f\u0000\u00e2\u00e5\u0003\"\u0011"
			+ "\u0000\u00e3\u00e5\u0003(\u0014\u0000\u00e4\u00e1\u0001\u0000\u0000\u0000"
			+ "\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e4\u00e3\u0001\u0000\u0000\u0000"
			+ "\u00e5\u001d\u0001\u0000\u0000\u0000\u00e6\u00e7\u0003 \u0010\u0000\u00e7"
			+ "\u00e8\u0005!\u0000\u0000\u00e8\u001f\u0001\u0000\u0000\u0000\u00e9\u00eb"
			+ "\u0005\u000b\u0000\u0000\u00ea\u00e9\u0001\u0000\u0000\u0000\u00ea\u00eb"
			+ "\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000\u0000\u0000\u00ec\u00ed"
			+ "\u0003\u0014\n\u0000\u00ed\u00ee\u0003\u000e\u0007\u0000\u00ee!\u0001"
			+ "\u0000\u0000\u0000\u00ef\u00f6\u0003&\u0013\u0000\u00f0\u00f6\u0003*\u0015"
			+ "\u0000\u00f1\u00f6\u00034\u001a\u0000\u00f2\u00f6\u00036\u001b\u0000\u00f3"
			+ "\u00f6\u0003B!\u0000\u00f4\u00f6\u0003H$\u0000\u00f5\u00ef\u0001\u0000"
			+ "\u0000\u0000\u00f5\u00f0\u0001\u0000\u0000\u0000\u00f5\u00f1\u0001\u0000"
			+ "\u0000\u0000\u00f5\u00f2\u0001\u0000\u0000\u0000\u00f5\u00f3\u0001\u0000"
			+ "\u0000\u0000\u00f5\u00f4\u0001\u0000\u0000\u0000\u00f6#\u0001\u0000\u0000"
			+ "\u0000\u00f7\u00fd\u0003&\u0013\u0000\u00f8\u00fd\u0003,\u0016\u0000\u00f9"
			+ "\u00fd\u00038\u001c\u0000\u00fa\u00fd\u0003D\"\u0000\u00fb\u00fd\u0003"
			+ "J%\u0000\u00fc\u00f7\u0001\u0000\u0000\u0000\u00fc\u00f8\u0001\u0000\u0000"
			+ "\u0000\u00fc\u00f9\u0001\u0000\u0000\u0000\u00fc\u00fa\u0001\u0000\u0000"
			+ "\u0000\u00fc\u00fb\u0001\u0000\u0000\u0000\u00fd%\u0001\u0000\u0000\u0000"
			+ "\u00fe\u0108\u0003\u0018\f\u0000\u00ff\u0108\u0003.\u0017\u0000\u0100"
			+ "\u0108\u00030\u0018\u0000\u0101\u0108\u0003:\u001d\u0000\u0102\u0108\u0003"
			+ "F#\u0000\u0103\u0108\u0003P(\u0000\u0104\u0108\u0003R)\u0000\u0105\u0108"
			+ "\u0003T*\u0000\u0106\u0108\u0003V+\u0000\u0107\u00fe\u0001\u0000\u0000"
			+ "\u0000\u0107\u00ff\u0001\u0000\u0000\u0000\u0107\u0100\u0001\u0000\u0000"
			+ "\u0000\u0107\u0101\u0001\u0000\u0000\u0000\u0107\u0102\u0001\u0000\u0000"
			+ "\u0000\u0107\u0103\u0001\u0000\u0000\u0000\u0107\u0104\u0001\u0000\u0000"
			+ "\u0000\u0107\u0105\u0001\u0000\u0000\u0000\u0107\u0106\u0001\u0000\u0000"
			+ "\u0000\u0108\'\u0001\u0000\u0000\u0000\u0109\u010a\u0005\u001a\u0000\u0000"
			+ "\u010a\u010b\u0005)\u0000\u0000\u010b)\u0001\u0000\u0000\u0000\u010c\u010d"
			+ "\u0003(\u0014\u0000\u010d\u010e\u0003\"\u0011\u0000\u010e+\u0001\u0000"
			+ "\u0000\u0000\u010f\u0110\u0003(\u0014\u0000\u0110\u0111\u0003$\u0012\u0000"
			+ "\u0111-\u0001\u0000\u0000\u0000\u0112\u0113\u0005!\u0000\u0000\u0113/"
			+ "\u0001\u0000\u0000\u0000\u0114\u0115\u00032\u0019\u0000\u0115\u0116\u0005"
			+ "!\u0000\u0000\u01161\u0001\u0000\u0000\u0000\u0117\u011e\u0003l6\u0000"
			+ "\u0118\u011e\u0003\u0082A\u0000\u0119\u011e\u0003\u0084B\u0000\u011a\u011e"
			+ "\u0003\u008aE\u0000\u011b\u011e\u0003\u008cF\u0000\u011c\u011e\u0003b"
			+ "1\u0000\u011d\u0117\u0001\u0000\u0000\u0000\u011d\u0118\u0001\u0000\u0000"
			+ "\u0000\u011d\u0119\u0001\u0000\u0000\u0000\u011d\u011a\u0001\u0000\u0000"
			+ "\u0000\u011d\u011b\u0001\u0000\u0000\u0000\u011d\u011c\u0001\u0000\u0000"
			+ "\u0000\u011e3\u0001\u0000\u0000\u0000\u011f\u0120\u0005\u000e\u0000\u0000"
			+ "\u0120\u0121\u0005\u001b\u0000\u0000\u0121\u0122\u0003j5\u0000\u0122\u0123"
			+ "\u0005\u001c\u0000\u0000\u0123\u0124\u0003\"\u0011\u0000\u01245\u0001"
			+ "\u0000\u0000\u0000\u0125\u0126\u0005\u000e\u0000\u0000\u0126\u0127\u0005"
			+ "\u001b\u0000\u0000\u0127\u0128\u0003j5\u0000\u0128\u0129\u0005\u001c\u0000"
			+ "\u0000\u0129\u012a\u0003$\u0012\u0000\u012a\u012b\u0005\t\u0000\u0000"
			+ "\u012b\u012c\u0003\"\u0011\u0000\u012c7\u0001\u0000\u0000\u0000\u012d"
			+ "\u012e\u0005\u000e\u0000\u0000\u012e\u012f\u0005\u001b\u0000\u0000\u012f"
			+ "\u0130\u0003j5\u0000\u0130\u0131\u0005\u001c\u0000\u0000\u0131\u0132\u0003"
			+ "$\u0012\u0000\u0132\u0133\u0005\t\u0000\u0000\u0133\u0134\u0003$\u0012"
			+ "\u0000\u01349\u0001\u0000\u0000\u0000\u0135\u0136\u0005\u0011\u0000\u0000"
			+ "\u0136\u0137\u0005\u001b\u0000\u0000\u0137\u0138\u0003j5\u0000\u0138\u0139"
			+ "\u0005\u001c\u0000\u0000\u0139\u013a\u0003<\u001e\u0000\u013a;\u0001\u0000"
			+ "\u0000\u0000\u013b\u013f\u0005\u001d\u0000\u0000\u013c\u013e\u0003>\u001f"
			+ "\u0000\u013d\u013c\u0001\u0000\u0000\u0000\u013e\u0141\u0001\u0000\u0000"
			+ "\u0000\u013f\u013d\u0001\u0000\u0000\u0000\u013f\u0140\u0001\u0000\u0000"
			+ "\u0000\u0140\u0145\u0001\u0000\u0000\u0000\u0141\u013f\u0001\u0000\u0000"
			+ "\u0000\u0142\u0144\u0003@ \u0000\u0143\u0142\u0001\u0000\u0000\u0000\u0144"
			+ "\u0147\u0001\u0000\u0000\u0000\u0145\u0143\u0001\u0000\u0000\u0000\u0145"
			+ "\u0146\u0001\u0000\u0000\u0000\u0146\u0148\u0001\u0000\u0000\u0000\u0147"
			+ "\u0145\u0001\u0000\u0000\u0000\u0148\u0149\u0005\u001e\u0000\u0000\u0149"
			+ "=\u0001\u0000\u0000\u0000\u014a\u014c\u0003@ \u0000\u014b\u014a\u0001"
			+ "\u0000\u0000\u0000\u014c\u014d\u0001\u0000\u0000\u0000\u014d\u014b\u0001"
			+ "\u0000\u0000\u0000\u014d\u014e\u0001\u0000\u0000\u0000\u014e\u014f\u0001"
			+ "\u0000\u0000\u0000\u014f\u0150\u0003\u001a\r\u0000\u0150?\u0001\u0000"
			+ "\u0000\u0000\u0151\u0152\u0005\u0003\u0000\u0000\u0152\u0153\u0003j5\u0000"
			+ "\u0153\u0154\u0005)\u0000\u0000\u0154\u0158\u0001\u0000\u0000\u0000\u0155"
			+ "\u0156\u0005\u0006\u0000\u0000\u0156\u0158\u0005)\u0000\u0000\u0157\u0151"
			+ "\u0001\u0000\u0000\u0000\u0157\u0155\u0001\u0000\u0000\u0000\u0158A\u0001"
			+ "\u0000\u0000\u0000\u0159\u015a\u0005\u0013\u0000\u0000\u015a\u015b\u0005"
			+ "\u001b\u0000\u0000\u015b\u015c\u0003j5\u0000\u015c\u015d\u0005\u001c\u0000"
			+ "\u0000\u015d\u015e\u0003\"\u0011\u0000\u015eC\u0001\u0000\u0000\u0000"
			+ "\u015f\u0160\u0005\u0013\u0000\u0000\u0160\u0161\u0005\u001b\u0000\u0000"
			+ "\u0161\u0162\u0003j5\u0000\u0162\u0163\u0005\u001c\u0000\u0000\u0163\u0164"
			+ "\u0003$\u0012\u0000\u0164E\u0001\u0000\u0000\u0000\u0165\u0166\u0005\u0007"
			+ "\u0000\u0000\u0166\u0167\u0003\"\u0011\u0000\u0167\u0168\u0005\u0013\u0000"
			+ "\u0000\u0168\u0169\u0005\u001b\u0000\u0000\u0169\u016a\u0003j5\u0000\u016a"
			+ "\u016b\u0005\u001c\u0000\u0000\u016b\u016c\u0005!\u0000\u0000\u016cG\u0001"
			+ "\u0000\u0000\u0000\u016d\u016e\u0005\f\u0000\u0000\u016e\u0170\u0005\u001b"
			+ "\u0000\u0000\u016f\u0171\u0003L&\u0000\u0170\u016f\u0001\u0000\u0000\u0000"
			+ "\u0170\u0171\u0001\u0000\u0000\u0000\u0171\u0172\u0001\u0000\u0000\u0000"
			+ "\u0172\u0174\u0005!\u0000\u0000\u0173\u0175\u0003j5\u0000\u0174\u0173"
			+ "\u0001\u0000\u0000\u0000\u0174\u0175\u0001\u0000\u0000\u0000\u0175\u0176"
			+ "\u0001\u0000\u0000\u0000\u0176\u0178\u0005!\u0000\u0000\u0177\u0179\u0003"
			+ "N\'\u0000\u0178\u0177\u0001\u0000\u0000\u0000\u0178\u0179\u0001\u0000"
			+ "\u0000\u0000\u0179\u017a\u0001\u0000\u0000\u0000\u017a\u017b\u0005\u001c"
			+ "\u0000\u0000\u017b\u017c\u0003\"\u0011\u0000\u017cI\u0001\u0000\u0000"
			+ "\u0000\u017d\u017e\u0005\f\u0000\u0000\u017e\u0180\u0005\u001b\u0000\u0000"
			+ "\u017f\u0181\u0003L&\u0000\u0180\u017f\u0001\u0000\u0000\u0000\u0180\u0181"
			+ "\u0001\u0000\u0000\u0000\u0181\u0182\u0001\u0000\u0000\u0000\u0182\u0184"
			+ "\u0005!\u0000\u0000\u0183\u0185\u0003j5\u0000\u0184\u0183\u0001\u0000"
			+ "\u0000\u0000\u0184\u0185\u0001\u0000\u0000\u0000\u0185\u0186\u0001\u0000"
			+ "\u0000\u0000\u0186\u0188\u0005!\u0000\u0000\u0187\u0189\u0003N\'\u0000"
			+ "\u0188\u0187\u0001\u0000\u0000\u0000\u0188\u0189\u0001\u0000\u0000\u0000"
			+ "\u0189\u018a\u0001\u0000\u0000\u0000\u018a\u018b\u0005\u001c\u0000\u0000"
			+ "\u018b\u018c\u0003$\u0012\u0000\u018cK\u0001\u0000\u0000\u0000\u018d\u0190"
			+ "\u0003N\'\u0000\u018e\u0190\u0003 \u0010\u0000\u018f\u018d\u0001\u0000"
			+ "\u0000\u0000\u018f\u018e\u0001\u0000\u0000\u0000\u0190M\u0001\u0000\u0000"
			+ "\u0000\u0191\u0196\u00032\u0019\u0000\u0192\u0193\u0005\"\u0000\u0000"
			+ "\u0193\u0195\u00032\u0019\u0000\u0194\u0192\u0001\u0000\u0000\u0000\u0195"
			+ "\u0198\u0001\u0000\u0000\u0000\u0196\u0194\u0001\u0000\u0000\u0000\u0196"
			+ "\u0197\u0001\u0000\u0000\u0000\u0197O\u0001\u0000\u0000\u0000\u0198\u0196"
			+ "\u0001\u0000\u0000\u0000\u0199\u019a\u0005\u0002\u0000\u0000\u019a\u019b"
			+ "\u0005!\u0000\u0000\u019bQ\u0001\u0000\u0000\u0000\u019c\u019d\u0005\u0005"
			+ "\u0000\u0000\u019d\u019e\u0005!\u0000\u0000\u019eS\u0001\u0000\u0000\u0000"
			+ "\u019f\u01a0\u0005\r\u0000\u0000\u01a0\u01a1\u0005\u001a\u0000\u0000\u01a1"
			+ "\u01a2\u0005!\u0000\u0000\u01a2U\u0001\u0000\u0000\u0000\u01a3\u01a4\u0005"
			+ "\n\u0000\u0000\u01a4\u01a5\u0005!\u0000\u0000\u01a5W\u0001\u0000\u0000"
			+ "\u0000\u01a6\u01aa\u0003\\.\u0000\u01a7\u01a9\u0003Z-\u0000\u01a8\u01a7"
			+ "\u0001\u0000\u0000\u0000\u01a9\u01ac\u0001\u0000\u0000\u0000\u01aa\u01a8"
			+ "\u0001\u0000\u0000\u0000\u01aa\u01ab\u0001\u0000\u0000\u0000\u01abY\u0001"
			+ "\u0000\u0000\u0000\u01ac\u01aa\u0001\u0000\u0000\u0000\u01ad\u01ae\u0003"
			+ "d2\u0000\u01ae[\u0001\u0000\u0000\u0000\u01af\u01b8\u0003\u0000\u0000"
			+ "\u0000\u01b0\u01b1\u0005\u001b\u0000\u0000\u01b1\u01b2\u0003j5\u0000\u01b2"
			+ "\u01b3\u0005\u001c\u0000\u0000\u01b3\u01b8\u0001\u0000\u0000\u0000\u01b4"
			+ "\u01b8\u0003`0\u0000\u01b5\u01b8\u0003f3\u0000\u01b6\u01b8\u0005\u001a"
			+ "\u0000\u0000\u01b7\u01af\u0001\u0000\u0000\u0000\u01b7\u01b0\u0001\u0000"
			+ "\u0000\u0000\u01b7\u01b4\u0001\u0000\u0000\u0000\u01b7\u01b5\u0001\u0000"
			+ "\u0000\u0000\u01b7\u01b6\u0001\u0000\u0000\u0000\u01b8]\u0001\u0000\u0000"
			+ "\u0000\u01b9\u01c0\u0003\u0000\u0000\u0000\u01ba\u01bb\u0005\u001b\u0000"
			+ "\u0000\u01bb\u01bc\u0003j5\u0000\u01bc\u01bd\u0005\u001c\u0000\u0000\u01bd"
			+ "\u01c0\u0001\u0000\u0000\u0000\u01be\u01c0\u0003f3\u0000\u01bf\u01b9\u0001"
			+ "\u0000\u0000\u0000\u01bf\u01ba\u0001\u0000\u0000\u0000\u01bf\u01be\u0001"
			+ "\u0000\u0000\u0000\u01c0_\u0001\u0000\u0000\u0000\u01c1\u01c6\u0005\u001a"
			+ "\u0000\u0000\u01c2\u01c3\u0005\u001f\u0000\u0000\u01c3\u01c4\u0003j5\u0000"
			+ "\u01c4\u01c5\u0005 \u0000\u0000\u01c5\u01c7\u0001\u0000\u0000\u0000\u01c6"
			+ "\u01c2\u0001\u0000\u0000\u0000\u01c7\u01c8\u0001\u0000\u0000\u0000\u01c8"
			+ "\u01c6\u0001\u0000\u0000\u0000\u01c8\u01c9\u0001\u0000\u0000\u0000\u01c9"
			+ "a\u0001\u0000\u0000\u0000\u01ca\u01cb\u0005\u001a\u0000\u0000\u01cb\u01cd"
			+ "\u0005\u001b\u0000\u0000\u01cc\u01ce\u0003h4\u0000\u01cd\u01cc\u0001\u0000"
			+ "\u0000\u0000\u01cd\u01ce\u0001\u0000\u0000\u0000\u01ce\u01cf\u0001\u0000"
			+ "\u0000\u0000\u01cf\u01da\u0005\u001c\u0000\u0000\u01d0\u01d1\u0003X,\u0000"
			+ "\u01d1\u01d2\u0005#\u0000\u0000\u01d2\u01d3\u0005\u001a\u0000\u0000\u01d3"
			+ "\u01d5\u0005\u001b\u0000\u0000\u01d4\u01d6\u0003h4\u0000\u01d5\u01d4\u0001"
			+ "\u0000\u0000\u0000\u01d5\u01d6\u0001\u0000\u0000\u0000\u01d6\u01d7\u0001"
			+ "\u0000\u0000\u0000\u01d7\u01d8\u0005\u001c\u0000\u0000\u01d8\u01da\u0001"
			+ "\u0000\u0000\u0000\u01d9\u01ca\u0001\u0000\u0000\u0000\u01d9\u01d0\u0001"
			+ "\u0000\u0000\u0000\u01dac\u0001\u0000\u0000\u0000\u01db\u01dc\u0005#\u0000"
			+ "\u0000\u01dc\u01dd\u0005\u001a\u0000\u0000\u01dd\u01df\u0005\u001b\u0000"
			+ "\u0000\u01de\u01e0\u0003h4\u0000\u01df\u01de\u0001\u0000\u0000\u0000\u01df"
			+ "\u01e0\u0001\u0000\u0000\u0000\u01e0\u01e1\u0001\u0000\u0000\u0000\u01e1"
			+ "\u01e2\u0005\u001c\u0000\u0000\u01e2e\u0001\u0000\u0000\u0000\u01e3\u01e4"
			+ "\u0005\u001a\u0000\u0000\u01e4\u01e6\u0005\u001b\u0000\u0000\u01e5\u01e7"
			+ "\u0003h4\u0000\u01e6\u01e5\u0001\u0000\u0000\u0000\u01e6\u01e7\u0001\u0000"
			+ "\u0000\u0000\u01e7\u01e8\u0001\u0000\u0000\u0000\u01e8\u01e9\u0005\u001c"
			+ "\u0000\u0000\u01e9g\u0001\u0000\u0000\u0000\u01ea\u01ef\u0003j5\u0000"
			+ "\u01eb\u01ec\u0005\"\u0000\u0000\u01ec\u01ee\u0003j5\u0000\u01ed\u01eb"
			+ "\u0001\u0000\u0000\u0000\u01ee\u01f1\u0001\u0000\u0000\u0000\u01ef\u01ed"
			+ "\u0001\u0000\u0000\u0000\u01ef\u01f0\u0001\u0000\u0000\u0000\u01f0i\u0001"
			+ "\u0000\u0000\u0000\u01f1\u01ef\u0001\u0000\u0000\u0000\u01f2\u01f5\u0003"
			+ "r9\u0000\u01f3\u01f5\u0003l6\u0000\u01f4\u01f2\u0001\u0000\u0000\u0000"
			+ "\u01f4\u01f3\u0001\u0000\u0000\u0000\u01f5k\u0001\u0000\u0000\u0000\u01f6"
			+ "\u01f7\u0003n7\u0000\u01f7\u01f8\u0003p8\u0000\u01f8\u01f9\u0003j5\u0000"
			+ "\u01f9m\u0001\u0000\u0000\u0000\u01fa\u01fd\u0005\u001a\u0000\u0000\u01fb"
			+ "\u01fd\u0003`0\u0000\u01fc\u01fa\u0001\u0000\u0000\u0000\u01fc\u01fb\u0001"
			+ "\u0000\u0000\u0000\u01fdo\u0001\u0000\u0000\u0000\u01fe\u01ff\u0007\u0002"
			+ "\u0000\u0000\u01ffq\u0001\u0000\u0000\u0000\u0200\u0208\u0003t:\u0000"
			+ "\u0201\u0202\u0003t:\u0000\u0202\u0203\u0005(\u0000\u0000\u0203\u0204"
			+ "\u0003j5\u0000\u0204\u0205\u0005)\u0000\u0000\u0205\u0206\u0003r9\u0000"
			+ "\u0206\u0208\u0001\u0000\u0000\u0000\u0207\u0200\u0001\u0000\u0000\u0000"
			+ "\u0207\u0201\u0001\u0000\u0000\u0000\u0208s\u0001\u0000\u0000\u0000\u0209"
			+ "\u020a\u0006:\uffff\uffff\u0000\u020a\u020b\u0003v;\u0000\u020b\u0211"
			+ "\u0001\u0000\u0000\u0000\u020c\u020d\n\u0001\u0000\u0000\u020d\u020e\u0005"
			+ "/\u0000\u0000\u020e\u0210\u0003v;\u0000\u020f\u020c\u0001\u0000\u0000"
			+ "\u0000\u0210\u0213\u0001\u0000\u0000\u0000\u0211\u020f\u0001\u0000\u0000"
			+ "\u0000\u0211\u0212\u0001\u0000\u0000\u0000\u0212u\u0001\u0000\u0000\u0000"
			+ "\u0213\u0211\u0001\u0000\u0000\u0000\u0214\u0215\u0006;\uffff\uffff\u0000"
			+ "\u0215\u0216\u0003x<\u0000\u0216\u021c\u0001\u0000\u0000\u0000\u0217\u0218"
			+ "\n\u0001\u0000\u0000\u0218\u0219\u0005.\u0000\u0000\u0219\u021b\u0003"
			+ "x<\u0000\u021a\u0217\u0001\u0000\u0000\u0000\u021b\u021e\u0001\u0000\u0000"
			+ "\u0000\u021c\u021a\u0001\u0000\u0000\u0000\u021c\u021d\u0001\u0000\u0000"
			+ "\u0000\u021dw\u0001\u0000\u0000\u0000\u021e\u021c\u0001\u0000\u0000\u0000"
			+ "\u021f\u0220\u0006<\uffff\uffff\u0000\u0220\u0221\u0003z=\u0000\u0221"
			+ "\u022a\u0001\u0000\u0000\u0000\u0222\u0223\n\u0002\u0000\u0000\u0223\u0224"
			+ "\u0005*\u0000\u0000\u0224\u0229\u0003z=\u0000\u0225\u0226\n\u0001\u0000"
			+ "\u0000\u0226\u0227\u0005-\u0000\u0000\u0227\u0229\u0003z=\u0000\u0228"
			+ "\u0222\u0001\u0000\u0000\u0000\u0228\u0225\u0001\u0000\u0000\u0000\u0229"
			+ "\u022c\u0001\u0000\u0000\u0000\u022a\u0228\u0001\u0000\u0000\u0000\u022a"
			+ "\u022b\u0001\u0000\u0000\u0000\u022by\u0001\u0000\u0000\u0000\u022c\u022a"
			+ "\u0001\u0000\u0000\u0000\u022d\u022e\u0006=\uffff\uffff\u0000\u022e\u022f"
			+ "\u0003|>\u0000\u022f\u023e\u0001\u0000\u0000\u0000\u0230\u0231\n\u0004"
			+ "\u0000\u0000\u0231\u0232\u0005&\u0000\u0000\u0232\u023d\u0003|>\u0000"
			+ "\u0233\u0234\n\u0003\u0000\u0000\u0234\u0235\u0005%\u0000\u0000\u0235"
			+ "\u023d\u0003|>\u0000\u0236\u0237\n\u0002\u0000\u0000\u0237\u0238\u0005"
			+ "+\u0000\u0000\u0238\u023d\u0003|>\u0000\u0239\u023a\n\u0001\u0000\u0000"
			+ "\u023a\u023b\u0005,\u0000\u0000\u023b\u023d\u0003|>\u0000\u023c\u0230"
			+ "\u0001\u0000\u0000\u0000\u023c\u0233\u0001\u0000\u0000\u0000\u023c\u0236"
			+ "\u0001\u0000\u0000\u0000\u023c\u0239\u0001\u0000\u0000\u0000\u023d\u0240"
			+ "\u0001\u0000\u0000\u0000\u023e\u023c\u0001\u0000\u0000\u0000\u023e\u023f"
			+ "\u0001\u0000\u0000\u0000\u023f{\u0001\u0000\u0000\u0000\u0240\u023e\u0001"
			+ "\u0000\u0000\u0000\u0241\u0242\u0006>\uffff\uffff\u0000\u0242\u0243\u0003"
			+ "~?\u0000\u0243\u024c\u0001\u0000\u0000\u0000\u0244\u0245\n\u0002\u0000"
			+ "\u0000\u0245\u0246\u00052\u0000\u0000\u0246\u024b\u0003~?\u0000\u0247"
			+ "\u0248\n\u0001\u0000\u0000\u0248\u0249\u00053\u0000\u0000\u0249\u024b"
			+ "\u0003~?\u0000\u024a\u0244\u0001\u0000\u0000\u0000\u024a\u0247\u0001\u0000"
			+ "\u0000\u0000\u024b\u024e\u0001\u0000\u0000\u0000\u024c\u024a\u0001\u0000"
			+ "\u0000\u0000\u024c\u024d\u0001\u0000\u0000\u0000\u024d}\u0001\u0000\u0000"
			+ "\u0000\u024e\u024c\u0001\u0000\u0000\u0000\u024f\u0250\u0006?\uffff\uffff"
			+ "\u0000\u0250\u0251\u0003\u0080@\u0000\u0251\u025d\u0001\u0000\u0000\u0000"
			+ "\u0252\u0253\n\u0003\u0000\u0000\u0253\u0254\u00054\u0000\u0000\u0254"
			+ "\u025c\u0003\u0080@\u0000\u0255\u0256\n\u0002\u0000\u0000\u0256\u0257"
			+ "\u00055\u0000\u0000\u0257\u025c\u0003\u0080@\u0000\u0258\u0259\n\u0001"
			+ "\u0000\u0000\u0259\u025a\u00056\u0000\u0000\u025a\u025c\u0003\u0080@\u0000"
			+ "\u025b\u0252\u0001\u0000\u0000\u0000\u025b\u0255\u0001\u0000\u0000\u0000"
			+ "\u025b\u0258\u0001\u0000\u0000\u0000\u025c\u025f\u0001\u0000\u0000\u0000"
			+ "\u025d\u025b\u0001\u0000\u0000\u0000\u025d\u025e\u0001\u0000\u0000\u0000"
			+ "\u025e\u007f\u0001\u0000\u0000\u0000\u025f\u025d\u0001\u0000\u0000\u0000"
			+ "\u0260\u0268\u0003\u0082A\u0000\u0261\u0268\u0003\u0084B\u0000\u0262\u0263"
			+ "\u00052\u0000\u0000\u0263\u0268\u0003\u0080@\u0000\u0264\u0265\u00053"
			+ "\u0000\u0000\u0265\u0268\u0003\u0080@\u0000\u0266\u0268\u0003\u0086C\u0000"
			+ "\u0267\u0260\u0001\u0000\u0000\u0000\u0267\u0261\u0001\u0000\u0000\u0000"
			+ "\u0267\u0262\u0001\u0000\u0000\u0000\u0267\u0264\u0001\u0000\u0000\u0000"
			+ "\u0267\u0266\u0001\u0000\u0000\u0000\u0268\u0081\u0001\u0000\u0000\u0000"
			+ "\u0269\u026a\u00050\u0000\u0000\u026a\u026b\u0003\u0080@\u0000\u026b\u0083"
			+ "\u0001\u0000\u0000\u0000\u026c\u026d\u00051\u0000\u0000\u026d\u026e\u0003"
			+ "\u0080@\u0000\u026e\u0085\u0001\u0000\u0000\u0000\u026f\u0274\u0003\u0088"
			+ "D\u0000\u0270\u0271\u0005\'\u0000\u0000\u0271\u0274\u0003\u0080@\u0000"
			+ "\u0272\u0274\u0003\u008eG\u0000\u0273\u026f\u0001\u0000\u0000\u0000\u0273"
			+ "\u0270\u0001\u0000\u0000\u0000\u0273\u0272\u0001\u0000\u0000\u0000\u0274"
			+ "\u0087\u0001\u0000\u0000\u0000\u0275\u0278\u0003X,\u0000\u0276\u0278\u0005"
			+ "\u001a\u0000\u0000\u0277\u0275\u0001\u0000\u0000\u0000\u0277\u0276\u0001"
			+ "\u0000\u0000\u0000\u0278\u027c\u0001\u0000\u0000\u0000\u0279\u027b\u0007"
			+ "\u0003\u0000\u0000\u027a\u0279\u0001\u0000\u0000\u0000\u027b\u027e\u0001"
			+ "\u0000\u0000\u0000\u027c\u027a\u0001\u0000\u0000\u0000\u027c\u027d\u0001"
			+ "\u0000\u0000\u0000\u027d\u0089\u0001\u0000\u0000\u0000\u027e\u027c\u0001"
			+ "\u0000\u0000\u0000\u027f\u0280\u0003\u0088D\u0000\u0280\u0281\u00050\u0000"
			+ "\u0000\u0281\u008b\u0001\u0000\u0000\u0000\u0282\u0283\u0003\u0088D\u0000"
			+ "\u0283\u0284\u00051\u0000\u0000\u0284\u008d\u0001\u0000\u0000\u0000\u0285"
			+ "\u0286\u0005\u001b\u0000\u0000\u0286\u0287\u0003\u0002\u0001\u0000\u0287"
			+ "\u0288\u0005\u001c\u0000\u0000\u0288\u0289\u0003\u0080@\u0000\u0289\u0290"
			+ "\u0001\u0000\u0000\u0000\u028a\u028b\u0005\u001b\u0000\u0000\u028b\u028c"
			+ "\u0003\u0006\u0003\u0000\u028c\u028d\u0005\u001c\u0000\u0000\u028d\u028e"
			+ "\u0003\u0086C\u0000\u028e\u0290\u0001\u0000\u0000\u0000\u028f\u0285\u0001"
			+ "\u0000\u0000\u0000\u028f\u028a\u0001\u0000\u0000\u0000\u0290\u008f\u0001"
			+ "\u0000\u0000\u0000:\u0095\u009c\u00a3\u00ae\u00b6\u00be\u00c4\u00c8\u00cc"
			+ "\u00d1\u00d8\u00df\u00e4\u00ea\u00f5\u00fc\u0107\u011d\u013f\u0145\u014d"
			+ "\u0157\u0170\u0174\u0178\u0180\u0184\u0188\u018f\u0196\u01aa\u01b7\u01bf"
			+ "\u01c8\u01cd\u01d5\u01d9\u01df\u01e6\u01ef\u01f4\u01fc\u0207\u0211\u021c"
			+ "\u0228\u022a\u023c\u023e\u024a\u024c\u025b\u025d\u0267\u0273\u0277\u027c"
			+ "\u028f";

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
			"gotoStatement", "exitStatement", "primary", "primary_extension",
			"primary_LHS", "primary_LHS_access", "arrayAccess",
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
		int _startState = 124;
		this.enterRecursionRule(_localctx, 124,
			IkalaScriptParser.RULE_additiveExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(578);
					this.multiplicativeExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(588);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 50,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							this.setState(586);
							this._errHandler.sync(this);
							switch (this.getInterpreter()
								.adaptivePredict(this._input, 49, this._ctx)) {
								case 1: {
									_localctx = new AdditiveExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_additiveExpression);
									this.setState(580);
									if (!(this.precpred(this._ctx, 2))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 2)");
									}
									this.setState(581);
									this.match(IkalaScriptParser.ADD);
									this.setState(582);
									this.multiplicativeExpression(0);
								}
									break;
								case 2: {
									_localctx = new AdditiveExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_additiveExpression);
									this.setState(583);
									if (!(this.precpred(this._ctx, 1))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 1)");
									}
									this.setState(584);
									this.match(IkalaScriptParser.SUB);
									this.setState(585);
									this.multiplicativeExpression(0);
								}
									break;
							}
						}
					}
					this.setState(590);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						50, this._ctx);
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
		this.enterRule(_localctx, 104, IkalaScriptParser.RULE_argumentList);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(490);
				this.expression();
				this.setState(495);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.COMMA) {
					{
						{
							this.setState(491);
							this.match(IkalaScriptParser.COMMA);
							this.setState(492);
							this.expression();
						}
					}
					this.setState(497);
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
		this.enterRule(_localctx, 96, IkalaScriptParser.RULE_arrayAccess);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(449);
				this.match(IkalaScriptParser.Identifier);
				this.setState(454);
				this._errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								this.setState(450);
								this.match(IkalaScriptParser.LBRACK);
								this.setState(451);
								this.expression();
								this.setState(452);
								this.match(IkalaScriptParser.RBRACK);
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
					}
					this.setState(456);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						33, this._ctx);
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

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx =
			new ArrayTypeContext(this._ctx, this.getState());
		this.enterRule(_localctx, 10, IkalaScriptParser.RULE_arrayType);
		try {
			this.setState(174);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 3,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(166);
					this.primitiveType();
					this.setState(167);
					this.dims();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(169);
					this.classOrInterfaceType();
					this.setState(170);
					this.dims();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(172);
					this.match(IkalaScriptParser.Identifier);
					this.setState(173);
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
		this.enterRule(_localctx, 108, IkalaScriptParser.RULE_assignment);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(502);
				this.leftHandSide();
				this.setState(503);
				this.assignmentOperator();
				this.setState(504);
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
		this.enterRule(_localctx, 112,
			IkalaScriptParser.RULE_assignmentOperator);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(510);
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
				this.setState(214);
				this.match(IkalaScriptParser.LBRACE);
				this.setState(216);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844434325110198L) != 0)) {
					{
						this.setState(215);
						this.blockStatements();
					}
				}

				this.setState(218);
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
			this.setState(228);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 12,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(225);
					this.localVariableDeclarationStatement();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(226);
					this.statement();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(227);
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
				this.setState(221);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
						{
							this.setState(220);
							this.blockStatement();
						}
					}
					this.setState(223);
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
				this.setState(409);
				this.match(IkalaScriptParser.BREAK);
				this.setState(410);
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
		this.enterRule(_localctx, 142, IkalaScriptParser.RULE_castExpression);
		try {
			this.setState(655);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 57,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(645);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(646);
					this.primitiveType();
					this.setState(647);
					this.match(IkalaScriptParser.RPAREN);
					this.setState(648);
					this.unaryExpression();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(650);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(651);
					this.referenceType();
					this.setState(652);
					this.match(IkalaScriptParser.RPAREN);
					this.setState(653);
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
				this.setState(158);
				this.match(IkalaScriptParser.Identifier);
				this.setState(163);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.DOT) {
					{
						{
							this.setState(159);
							this.match(IkalaScriptParser.DOT);
							this.setState(160);
							this.match(IkalaScriptParser.Identifier);
						}
					}
					this.setState(165);
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
				this.setState(209);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844434325110198L) != 0)) {
					{
						{
							this.setState(206);
							this.blockStatement();
						}
					}
					this.setState(211);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.setState(212);
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
		int _startState = 118;
		this.enterRecursionRule(_localctx, 118,
			IkalaScriptParser.RULE_conditionalAndExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(533);
					this.equalityExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(540);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 44,
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
								this.setState(535);
								if (!(this.precpred(this._ctx, 1))) {
									throw new FailedPredicateException(this,
										"precpred(_ctx, 1)");
								}
								this.setState(536);
								this.match(IkalaScriptParser.AND);
								this.setState(537);
								this.equalityExpression(0);
							}
						}
					}
					this.setState(542);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						44, this._ctx);
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
		this.enterRule(_localctx, 114,
			IkalaScriptParser.RULE_conditionalExpression);
		try {
			this.setState(519);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 42,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(512);
					this.conditionalOrExpression(0);
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(513);
					this.conditionalOrExpression(0);
					this.setState(514);
					this.match(IkalaScriptParser.QUESTION);
					this.setState(515);
					this.expression();
					this.setState(516);
					this.match(IkalaScriptParser.COLON);
					this.setState(517);
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
		int _startState = 116;
		this.enterRecursionRule(_localctx, 116,
			IkalaScriptParser.RULE_conditionalOrExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(522);
					this.conditionalAndExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(529);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 43,
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
								this.setState(524);
								if (!(this.precpred(this._ctx, 1))) {
									throw new FailedPredicateException(this,
										"precpred(_ctx, 1)");
								}
								this.setState(525);
								this.match(IkalaScriptParser.OR);
								this.setState(526);
								this.conditionalAndExpression(0);
							}
						}
					}
					this.setState(531);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						43, this._ctx);
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
				this.setState(412);
				this.match(IkalaScriptParser.CONTINUE);
				this.setState(413);
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
				this.setState(176);
				this.match(IkalaScriptParser.LBRACK);
				this.setState(177);
				this.match(IkalaScriptParser.RBRACK);
				this.setState(182);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.LBRACK) {
					{
						{
							this.setState(178);
							this.match(IkalaScriptParser.LBRACK);
							this.setState(179);
							this.match(IkalaScriptParser.RBRACK);
						}
					}
					this.setState(184);
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
				this.setState(357);
				this.match(IkalaScriptParser.DO);
				this.setState(358);
				this.statement();
				this.setState(359);
				this.match(IkalaScriptParser.WHILE);
				this.setState(360);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(361);
				this.expression();
				this.setState(362);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(363);
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
				this.setState(274);
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
		int _startState = 120;
		this.enterRecursionRule(_localctx, 120,
			IkalaScriptParser.RULE_equalityExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(544);
					this.relationalExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(554);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 46,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							this.setState(552);
							this._errHandler.sync(this);
							switch (this.getInterpreter()
								.adaptivePredict(this._input, 45, this._ctx)) {
								case 1: {
									_localctx = new EqualityExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_equalityExpression);
									this.setState(546);
									if (!(this.precpred(this._ctx, 2))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 2)");
									}
									this.setState(547);
									this.match(IkalaScriptParser.EQUAL);
									this.setState(548);
									this.relationalExpression(0);
								}
									break;
								case 2: {
									_localctx = new EqualityExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_equalityExpression);
									this.setState(549);
									if (!(this.precpred(this._ctx, 1))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 1)");
									}
									this.setState(550);
									this.match(IkalaScriptParser.NOTEQUAL);
									this.setState(551);
									this.relationalExpression(0);
								}
									break;
							}
						}
					}
					this.setState(556);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						46, this._ctx);
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
				this.setState(419);
				this.match(IkalaScriptParser.EXIT);
				this.setState(420);
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
		this.enterRule(_localctx, 106, IkalaScriptParser.RULE_expression);
		try {
			this.setState(500);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 40,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(498);
					this.conditionalExpression();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(499);
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
				this.setState(276);
				this.statementExpression();
				this.setState(277);
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
			this.setState(399);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 28,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(397);
					this.statementExpressionList();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(398);
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
				this.setState(365);
				this.match(IkalaScriptParser.FOR);
				this.setState(366);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(368);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844425197619474L) != 0)) {
					{
						this.setState(367);
						this.forInit();
					}
				}

				this.setState(370);
				this.match(IkalaScriptParser.SEMICOLON);
				this.setState(372);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 4222674673860608L) != 0)) {
					{
						this.setState(371);
						this.expression();
					}
				}

				this.setState(374);
				this.match(IkalaScriptParser.SEMICOLON);
				this.setState(376);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844425197518848L) != 0)) {
					{
						this.setState(375);
						this.statementExpressionList();
					}
				}

				this.setState(378);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(379);
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
				this.setState(381);
				this.match(IkalaScriptParser.FOR);
				this.setState(382);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(384);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844425197619474L) != 0)) {
					{
						this.setState(383);
						this.forInit();
					}
				}

				this.setState(386);
				this.match(IkalaScriptParser.SEMICOLON);
				this.setState(388);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 4222674673860608L) != 0)) {
					{
						this.setState(387);
						this.expression();
					}
				}

				this.setState(390);
				this.match(IkalaScriptParser.SEMICOLON);
				this.setState(392);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 844425197518848L) != 0)) {
					{
						this.setState(391);
						this.statementExpressionList();
					}
				}

				this.setState(394);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(395);
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
				this.setState(415);
				this.match(IkalaScriptParser.GOTO);
				this.setState(416);
				this.match(IkalaScriptParser.Identifier);
				this.setState(417);
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
				this.setState(293);
				this.match(IkalaScriptParser.IF);
				this.setState(294);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(295);
				this.expression();
				this.setState(296);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(297);
				this.statementNoShortIf();
				this.setState(298);
				this.match(IkalaScriptParser.ELSE);
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

	public final IfThenElseStatementNoShortIfContext
		ifThenElseStatementNoShortIf() throws RecognitionException {
		IfThenElseStatementNoShortIfContext _localctx =
			new IfThenElseStatementNoShortIfContext(this._ctx, this.getState());
		this.enterRule(_localctx, 56,
			IkalaScriptParser.RULE_ifThenElseStatementNoShortIf);
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
				this.setState(287);
				this.match(IkalaScriptParser.IF);
				this.setState(288);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(289);
				this.expression();
				this.setState(290);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(291);
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
				this.setState(265);
				this.match(IkalaScriptParser.Identifier);
				this.setState(266);
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
				this.setState(268);
				this.label();
				this.setState(269);
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
				this.setState(271);
				this.label();
				this.setState(272);
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
		this.enterRule(_localctx, 110, IkalaScriptParser.RULE_leftHandSide);
		try {
			this.setState(508);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 41,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(506);
					this.match(IkalaScriptParser.Identifier);
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(507);
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
				this.setState(144);
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
				this.setState(234);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la == IkalaScriptParser.FINAL) {
					{
						this.setState(233);
						this.match(IkalaScriptParser.FINAL);
					}
				}

				this.setState(236);
				this.type();
				this.setState(237);
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
				this.setState(230);
				this.localVariableDeclaration();
				this.setState(231);
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
		this.enterRule(_localctx, 98, IkalaScriptParser.RULE_methodInvocation);
		int _la;
		try {
			this.setState(473);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 36,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(458);
					this.match(IkalaScriptParser.Identifier);
					this.setState(459);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(461);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if ((((_la) & ~0x3f) == 0
						&& ((1L << _la) & 4222674673860608L) != 0)) {
						{
							this.setState(460);
							this.argumentList();
						}
					}

					this.setState(463);
					this.match(IkalaScriptParser.RPAREN);
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(464);
					this.primary();
					this.setState(465);
					this.match(IkalaScriptParser.DOT);
					this.setState(466);
					this.match(IkalaScriptParser.Identifier);
					this.setState(467);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(469);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if ((((_la) & ~0x3f) == 0
						&& ((1L << _la) & 4222674673860608L) != 0)) {
						{
							this.setState(468);
							this.argumentList();
						}
					}

					this.setState(471);
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
		this.enterRule(_localctx, 100,
			IkalaScriptParser.RULE_methodInvocation_extension);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(475);
				this.match(IkalaScriptParser.DOT);
				this.setState(476);
				this.match(IkalaScriptParser.Identifier);
				this.setState(477);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(479);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 4222674673860608L) != 0)) {
					{
						this.setState(478);
						this.argumentList();
					}
				}

				this.setState(481);
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
		this.enterRule(_localctx, 102,
			IkalaScriptParser.RULE_methodInvocation_LHS);
		int _la;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(483);
				this.match(IkalaScriptParser.Identifier);
				this.setState(484);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(486);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0
					&& ((1L << _la) & 4222674673860608L) != 0)) {
					{
						this.setState(485);
						this.argumentList();
					}
				}

				this.setState(488);
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
		int _startState = 126;
		this.enterRecursionRule(_localctx, 126,
			IkalaScriptParser.RULE_multiplicativeExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(592);
					this.unaryExpression();
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(605);
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
							this.setState(603);
							this._errHandler.sync(this);
							switch (this.getInterpreter()
								.adaptivePredict(this._input, 51, this._ctx)) {
								case 1: {
									_localctx =
										new MultiplicativeExpressionContext(
											_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_multiplicativeExpression);
									this.setState(594);
									if (!(this.precpred(this._ctx, 3))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 3)");
									}
									this.setState(595);
									this.match(IkalaScriptParser.MUL);
									this.setState(596);
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
									this.setState(597);
									if (!(this.precpred(this._ctx, 2))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 2)");
									}
									this.setState(598);
									this.match(IkalaScriptParser.DIV);
									this.setState(599);
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
									this.setState(600);
									if (!(this.precpred(this._ctx, 1))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 1)");
									}
									this.setState(601);
									this.match(IkalaScriptParser.MOD);
									this.setState(602);
									this.unaryExpression();
								}
									break;
							}
						}
					}
					this.setState(607);
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
				this.setState(151);
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
		this.enterRule(_localctx, 140,
			IkalaScriptParser.RULE_postDecrementExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(642);
				this.postfixExpression();
				this.setState(643);
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
		this.enterRule(_localctx, 136,
			IkalaScriptParser.RULE_postfixExpression);
		int _la;
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(631);
				this._errHandler.sync(this);
				switch (this.getInterpreter().adaptivePredict(this._input, 55,
					this._ctx)) {
					case 1: {
						this.setState(629);
						this.primary();
					}
						break;
					case 2: {
						this.setState(630);
						this.match(IkalaScriptParser.Identifier);
					}
						break;
				}
				this.setState(636);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 56,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							{
								this.setState(633);
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
					this.setState(638);
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
			this.exitRule();
		}
		return _localctx;
	}

	public final PostIncrementExpressionContext postIncrementExpression()
		throws RecognitionException {
		PostIncrementExpressionContext _localctx =
			new PostIncrementExpressionContext(this._ctx, this.getState());
		this.enterRule(_localctx, 138,
			IkalaScriptParser.RULE_postIncrementExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(639);
				this.postfixExpression();
				this.setState(640);
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
		this.enterRule(_localctx, 132,
			IkalaScriptParser.RULE_preDecrementExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(620);
				this.match(IkalaScriptParser.DEC);
				this.setState(621);
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
		this.enterRule(_localctx, 130,
			IkalaScriptParser.RULE_preIncrementExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(617);
				this.match(IkalaScriptParser.INC);
				this.setState(618);
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
					this.setState(422);
					this.primary_LHS();
				}
				this.setState(426);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 30,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							{
								this.setState(423);
								this.primary_extension();
							}
						}
					}
					this.setState(428);
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
		this.enterRule(_localctx, 90, IkalaScriptParser.RULE_primary_extension);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(429);
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
		this.enterRule(_localctx, 92, IkalaScriptParser.RULE_primary_LHS);
		try {
			this.setState(439);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 31,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(431);
					this.literal();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(432);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(433);
					this.expression();
					this.setState(434);
					this.match(IkalaScriptParser.RPAREN);
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(436);
					this.arrayAccess();
				}
					break;
				case 4:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(437);
					this.methodInvocation_LHS();
				}
					break;
				case 5:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(438);
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
		this.enterRule(_localctx, 94,
			IkalaScriptParser.RULE_primary_LHS_access);
		try {
			this.setState(447);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case IntegerLiteral:
				case FloatingPointLiteral:
				case BooleanLiteral:
				case CharacterLiteral:
				case StringLiteral:
				case NullLiteral:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(441);
					this.literal();
				}
					break;
				case LPAREN:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(442);
					this.match(IkalaScriptParser.LPAREN);
					this.setState(443);
					this.expression();
					this.setState(444);
					this.match(IkalaScriptParser.RPAREN);
				}
					break;
				case Identifier:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(446);
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
			this.setState(149);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case CHAR:
				case DOUBLE:
				case INT:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(146);
					this.numericType();
				}
					break;
				case BOOLEAN:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(147);
					this.match(IkalaScriptParser.BOOLEAN);
				}
					break;
				case STRING:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(148);
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
			this.setState(156);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 1,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(153);
					this.classOrInterfaceType();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(154);
					this.match(IkalaScriptParser.Identifier);
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(155);
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
		int _startState = 122;
		this.enterRecursionRule(_localctx, 122,
			IkalaScriptParser.RULE_relationalExpression, _p);
		try {
			int _alt;
			this.enterOuterAlt(_localctx, 1);
			{
				{
					this.setState(558);
					this.additiveExpression(0);
				}
				this._ctx.stop = this._input.LT(-1);
				this.setState(574);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 48,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (this._parseListeners != null) {
							this.triggerExitRuleEvent();
						}
						_prevctx = _localctx;
						{
							this.setState(572);
							this._errHandler.sync(this);
							switch (this.getInterpreter()
								.adaptivePredict(this._input, 47, this._ctx)) {
								case 1: {
									_localctx = new RelationalExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_relationalExpression);
									this.setState(560);
									if (!(this.precpred(this._ctx, 4))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 4)");
									}
									this.setState(561);
									this.match(IkalaScriptParser.LT);
									this.setState(562);
									this.additiveExpression(0);
								}
									break;
								case 2: {
									_localctx = new RelationalExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_relationalExpression);
									this.setState(563);
									if (!(this.precpred(this._ctx, 3))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 3)");
									}
									this.setState(564);
									this.match(IkalaScriptParser.GT);
									this.setState(565);
									this.additiveExpression(0);
								}
									break;
								case 3: {
									_localctx = new RelationalExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_relationalExpression);
									this.setState(566);
									if (!(this.precpred(this._ctx, 2))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 2)");
									}
									this.setState(567);
									this.match(IkalaScriptParser.LTE);
									this.setState(568);
									this.additiveExpression(0);
								}
									break;
								case 4: {
									_localctx = new RelationalExpressionContext(
										_parentctx, _parentState);
									this.pushNewRecursionContext(_localctx,
										_startState,
										IkalaScriptParser.RULE_relationalExpression);
									this.setState(569);
									if (!(this.precpred(this._ctx, 1))) {
										throw new FailedPredicateException(this,
											"precpred(_ctx, 1)");
									}
									this.setState(570);
									this.match(IkalaScriptParser.GTE);
									this.setState(571);
									this.additiveExpression(0);
								}
									break;
							}
						}
					}
					this.setState(576);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						48, this._ctx);
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
			case 58:
				return this.conditionalOrExpression_sempred(
					(ConditionalOrExpressionContext) _localctx, predIndex);
			case 59:
				return this.conditionalAndExpression_sempred(
					(ConditionalAndExpressionContext) _localctx, predIndex);
			case 60:
				return this.equalityExpression_sempred(
					(EqualityExpressionContext) _localctx, predIndex);
			case 61:
				return this.relationalExpression_sempred(
					(RelationalExpressionContext) _localctx, predIndex);
			case 62:
				return this.additiveExpression_sempred(
					(AdditiveExpressionContext) _localctx, predIndex);
			case 63:
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
			this.setState(245);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 14,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(239);
					this.statementWithoutTrailingSubstatement();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(240);
					this.labeledStatement();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(241);
					this.ifThenStatement();
				}
					break;
				case 4:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(242);
					this.ifThenElseStatement();
				}
					break;
				case 5:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(243);
					this.whileStatement();
				}
					break;
				case 6:
					this.enterOuterAlt(_localctx, 6); {
					this.setState(244);
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
			this.setState(285);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 17,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(279);
					this.assignment();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(280);
					this.preIncrementExpression();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(281);
					this.preDecrementExpression();
				}
					break;
				case 4:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(282);
					this.postIncrementExpression();
				}
					break;
				case 5:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(283);
					this.postDecrementExpression();
				}
					break;
				case 6:
					this.enterOuterAlt(_localctx, 6); {
					this.setState(284);
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
				this.setState(401);
				this.statementExpression();
				this.setState(406);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.COMMA) {
					{
						{
							this.setState(402);
							this.match(IkalaScriptParser.COMMA);
							this.setState(403);
							this.statementExpression();
						}
					}
					this.setState(408);
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
			this.setState(252);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 15,
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
					this.labeledStatementNoShortIf();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(249);
					this.ifThenElseStatementNoShortIf();
				}
					break;
				case 4:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(250);
					this.whileStatementNoShortIf();
				}
					break;
				case 5:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(251);
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
			this.setState(263);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case LBRACE:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(254);
					this.block();
				}
					break;
				case SEMICOLON:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(255);
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
					this.setState(256);
					this.expressionStatement();
				}
					break;
				case SWITCH:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(257);
					this.switchStatement();
				}
					break;
				case DO:
					this.enterOuterAlt(_localctx, 5); {
					this.setState(258);
					this.doStatement();
				}
					break;
				case BREAK:
					this.enterOuterAlt(_localctx, 6); {
					this.setState(259);
					this.breakStatement();
				}
					break;
				case CONTINUE:
					this.enterOuterAlt(_localctx, 7); {
					this.setState(260);
					this.continueStatement();
				}
					break;
				case GOTO:
					this.enterOuterAlt(_localctx, 8); {
					this.setState(261);
					this.gotoStatement();
				}
					break;
				case EXIT:
					this.enterOuterAlt(_localctx, 9); {
					this.setState(262);
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
				this.setState(315);
				this.match(IkalaScriptParser.LBRACE);
				this.setState(319);
				this._errHandler.sync(this);
				_alt = this.getInterpreter().adaptivePredict(this._input, 18,
					this._ctx);
				while (_alt != 2
					&& _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							{
								this.setState(316);
								this.switchBlockStatementGroup();
							}
						}
					}
					this.setState(321);
					this._errHandler.sync(this);
					_alt = this.getInterpreter().adaptivePredict(this._input,
						18, this._ctx);
				}
				this.setState(325);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.CASE
					|| _la == IkalaScriptParser.DEFAULT) {
					{
						{
							this.setState(322);
							this.switchLabel();
						}
					}
					this.setState(327);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.setState(328);
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
				this.setState(331);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
						{
							this.setState(330);
							this.switchLabel();
						}
					}
					this.setState(333);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				while (_la == IkalaScriptParser.CASE
					|| _la == IkalaScriptParser.DEFAULT);
				this.setState(335);
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
			this.setState(343);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case CASE:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(337);
					this.match(IkalaScriptParser.CASE);
					this.setState(338);
					this.expression();
					this.setState(339);
					this.match(IkalaScriptParser.COLON);
				}
					break;
				case DEFAULT:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(341);
					this.match(IkalaScriptParser.DEFAULT);
					this.setState(342);
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
				this.setState(309);
				this.match(IkalaScriptParser.SWITCH);
				this.setState(310);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(311);
				this.expression();
				this.setState(312);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(313);
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
			this.setState(204);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 8,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(202);
					this.primitiveType();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(203);
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
		this.enterRule(_localctx, 128, IkalaScriptParser.RULE_unaryExpression);
		try {
			this.setState(615);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
				case INC:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(608);
					this.preIncrementExpression();
				}
					break;
				case DEC:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(609);
					this.preDecrementExpression();
				}
					break;
				case ADD:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(610);
					this.match(IkalaScriptParser.ADD);
					this.setState(611);
					this.unaryExpression();
				}
					break;
				case SUB:
					this.enterOuterAlt(_localctx, 4); {
					this.setState(612);
					this.match(IkalaScriptParser.SUB);
					this.setState(613);
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
					this.setState(614);
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
		this.enterRule(_localctx, 134,
			IkalaScriptParser.RULE_unaryExpressionNotPlusMinus);
		try {
			this.setState(627);
			this._errHandler.sync(this);
			switch (this.getInterpreter().adaptivePredict(this._input, 54,
				this._ctx)) {
				case 1:
					this.enterOuterAlt(_localctx, 1); {
					this.setState(623);
					this.postfixExpression();
				}
					break;
				case 2:
					this.enterOuterAlt(_localctx, 2); {
					this.setState(624);
					this.match(IkalaScriptParser.NOT);
					this.setState(625);
					this.unaryExpression();
				}
					break;
				case 3:
					this.enterOuterAlt(_localctx, 3); {
					this.setState(626);
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
				this.setState(193);
				this.variableDeclaratorId();
				this.setState(196);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la == IkalaScriptParser.ASSIGN) {
					{
						this.setState(194);
						this.match(IkalaScriptParser.ASSIGN);
						this.setState(195);
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
				this.setState(198);
				this.match(IkalaScriptParser.Identifier);
				this.setState(200);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la == IkalaScriptParser.LBRACK) {
					{
						this.setState(199);
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
				this.setState(185);
				this.variableDeclarator();
				this.setState(190);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la == IkalaScriptParser.COMMA) {
					{
						{
							this.setState(186);
							this.match(IkalaScriptParser.COMMA);
							this.setState(187);
							this.variableDeclarator();
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

	public final WhileStatementContext whileStatement()
		throws RecognitionException {
		WhileStatementContext _localctx =
			new WhileStatementContext(this._ctx, this.getState());
		this.enterRule(_localctx, 66, IkalaScriptParser.RULE_whileStatement);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
				this.setState(345);
				this.match(IkalaScriptParser.WHILE);
				this.setState(346);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(347);
				this.expression();
				this.setState(348);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(349);
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
				this.setState(351);
				this.match(IkalaScriptParser.WHILE);
				this.setState(352);
				this.match(IkalaScriptParser.LPAREN);
				this.setState(353);
				this.expression();
				this.setState(354);
				this.match(IkalaScriptParser.RPAREN);
				this.setState(355);
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