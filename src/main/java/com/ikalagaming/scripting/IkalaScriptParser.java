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

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class IkalaScriptParser extends Parser {
    @SuppressWarnings("CheckReturnValue")
    public static class AdditiveExpressionContext extends ParserRuleContext {
        public AdditiveExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ADD() {
            return getToken(IkalaScriptParser.ADD, 0);
        }

        public AdditiveExpressionContext additiveExpression() {
            return this.getRuleContext(AdditiveExpressionContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterAdditiveExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitAdditiveExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_additiveExpression;
        }

        public MultiplicativeExpressionContext multiplicativeExpression() {
            return this.getRuleContext(MultiplicativeExpressionContext.class, 0);
        }

        public TerminalNode SUB() {
            return getToken(IkalaScriptParser.SUB, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ArgumentListContext extends ParserRuleContext {
        public ArgumentListContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(IkalaScriptParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(IkalaScriptParser.COMMA, i);
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
            return getToken(IkalaScriptParser.Identifier, 0);
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
        public AssignmentOperatorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ADD_ASSIGN() {
            return getToken(IkalaScriptParser.ADD_ASSIGN, 0);
        }

        public TerminalNode ASSIGN() {
            return getToken(IkalaScriptParser.ASSIGN, 0);
        }

        public TerminalNode DIV_ASSIGN() {
            return getToken(IkalaScriptParser.DIV_ASSIGN, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterAssignmentOperator(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitAssignmentOperator(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_assignmentOperator;
        }

        public TerminalNode MOD_ASSIGN() {
            return getToken(IkalaScriptParser.MOD_ASSIGN, 0);
        }

        public TerminalNode MUL_ASSIGN() {
            return getToken(IkalaScriptParser.MUL_ASSIGN, 0);
        }

        public TerminalNode SUB_ASSIGN() {
            return getToken(IkalaScriptParser.SUB_ASSIGN, 0);
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
            return getToken(IkalaScriptParser.LBRACE, 0);
        }

        public TerminalNode RBRACE() {
            return getToken(IkalaScriptParser.RBRACE, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class BlockStatementContext extends ParserRuleContext {
        public BlockStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterBlockStatement(this);
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

        public LocalVariableDeclarationStatementContext localVariableDeclarationStatement() {
            return this.getRuleContext(LocalVariableDeclarationStatementContext.class, 0);
        }

        public StatementContext statement() {
            return this.getRuleContext(StatementContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class BlockStatementsContext extends ParserRuleContext {
        public BlockStatementsContext(ParserRuleContext parent, int invokingState) {
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
                ((IkalaScriptParserListener) listener).enterBlockStatements(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitBlockStatements(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_blockStatements;
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class BreakStatementContext extends ParserRuleContext {
        public BreakStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode BREAK() {
            return getToken(IkalaScriptParser.BREAK, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterBreakStatement(this);
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
            return getToken(IkalaScriptParser.SEMICOLON, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class CastExpressionContext extends ParserRuleContext {
        public CastExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterCastExpression(this);
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
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public PrimitiveTypeContext primitiveType() {
            return this.getRuleContext(PrimitiveTypeContext.class, 0);
        }

        public ReferenceTypeContext referenceType() {
            return this.getRuleContext(ReferenceTypeContext.class, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }

        public UnaryExpressionContext unaryExpression() {
            return this.getRuleContext(UnaryExpressionContext.class, 0);
        }

        public UnaryExpressionNotPlusMinusContext unaryExpressionNotPlusMinus() {
            return this.getRuleContext(UnaryExpressionNotPlusMinusContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ClassOrInterfaceTypeContext extends ParserRuleContext {
        public ClassOrInterfaceTypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> DOT() {
            return getTokens(IkalaScriptParser.DOT);
        }

        public TerminalNode DOT(int i) {
            return getToken(IkalaScriptParser.DOT, i);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterClassOrInterfaceType(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitClassOrInterfaceType(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_classOrInterfaceType;
        }

        public List<TerminalNode> Identifier() {
            return getTokens(IkalaScriptParser.Identifier);
        }

        public TerminalNode Identifier(int i) {
            return getToken(IkalaScriptParser.Identifier, i);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class CompilationUnitContext extends ParserRuleContext {
        public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
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
                ((IkalaScriptParserListener) listener).enterCompilationUnit(this);
            }
        }

        public TerminalNode EOF() {
            return getToken(Recognizer.EOF, 0);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitCompilationUnit(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_compilationUnit;
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ConditionalAndExpressionContext extends ParserRuleContext {
        public ConditionalAndExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode AND() {
            return getToken(IkalaScriptParser.AND, 0);
        }

        public ConditionalAndExpressionContext conditionalAndExpression() {
            return this.getRuleContext(ConditionalAndExpressionContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterConditionalAndExpression(this);
            }
        }

        public EqualityExpressionContext equalityExpression() {
            return this.getRuleContext(EqualityExpressionContext.class, 0);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitConditionalAndExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_conditionalAndExpression;
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ConditionalExpressionContext extends ParserRuleContext {
        public ConditionalExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode COLON() {
            return getToken(IkalaScriptParser.COLON, 0);
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
                ((IkalaScriptParserListener) listener).enterConditionalExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitConditionalExpression(this);
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
            return getToken(IkalaScriptParser.QUESTION, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ConditionalOrExpressionContext extends ParserRuleContext {
        public ConditionalOrExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public ConditionalAndExpressionContext conditionalAndExpression() {
            return this.getRuleContext(ConditionalAndExpressionContext.class, 0);
        }

        public ConditionalOrExpressionContext conditionalOrExpression() {
            return this.getRuleContext(ConditionalOrExpressionContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterConditionalOrExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitConditionalOrExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_conditionalOrExpression;
        }

        public TerminalNode OR() {
            return getToken(IkalaScriptParser.OR, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ContinueStatementContext extends ParserRuleContext {
        public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode CONTINUE() {
            return getToken(IkalaScriptParser.CONTINUE, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterContinueStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitContinueStatement(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_continueStatement;
        }

        public TerminalNode SEMICOLON() {
            return getToken(IkalaScriptParser.SEMICOLON, 0);
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
            return getTokens(IkalaScriptParser.LBRACK);
        }

        public TerminalNode LBRACK(int i) {
            return getToken(IkalaScriptParser.LBRACK, i);
        }

        public List<TerminalNode> RBRACK() {
            return getTokens(IkalaScriptParser.RBRACK);
        }

        public TerminalNode RBRACK(int i) {
            return getToken(IkalaScriptParser.RBRACK, i);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DoStatementContext extends ParserRuleContext {
        public DoStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode DO() {
            return getToken(IkalaScriptParser.DO, 0);
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
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }

        public TerminalNode SEMICOLON() {
            return getToken(IkalaScriptParser.SEMICOLON, 0);
        }

        public StatementContext statement() {
            return this.getRuleContext(StatementContext.class, 0);
        }

        public TerminalNode WHILE() {
            return getToken(IkalaScriptParser.WHILE, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EmptyStatementContext extends ParserRuleContext {
        public EmptyStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterEmptyStatement(this);
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
            return getToken(IkalaScriptParser.SEMICOLON, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EqualityExpressionContext extends ParserRuleContext {
        public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterEqualityExpression(this);
            }
        }

        public TerminalNode EQUAL() {
            return getToken(IkalaScriptParser.EQUAL, 0);
        }

        public EqualityExpressionContext equalityExpression() {
            return this.getRuleContext(EqualityExpressionContext.class, 0);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitEqualityExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_equalityExpression;
        }

        public TerminalNode NOTEQUAL() {
            return getToken(IkalaScriptParser.NOTEQUAL, 0);
        }

        public RelationalExpressionContext relationalExpression() {
            return this.getRuleContext(RelationalExpressionContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ExitStatementContext extends ParserRuleContext {
        public ExitStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterExitStatement(this);
            }
        }

        public TerminalNode EXIT() {
            return getToken(IkalaScriptParser.EXIT, 0);
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
            return getToken(IkalaScriptParser.SEMICOLON, 0);
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
        public ExpressionStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterExpressionStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitExpressionStatement(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_expressionStatement;
        }

        public TerminalNode SEMICOLON() {
            return getToken(IkalaScriptParser.SEMICOLON, 0);
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
            return this.getRuleContext(LocalVariableDeclarationContext.class, 0);
        }

        public StatementExpressionListContext statementExpressionList() {
            return this.getRuleContext(StatementExpressionListContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ForStatementContext extends ParserRuleContext {
        public ForStatementContext(ParserRuleContext parent, int invokingState) {
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
            return getToken(IkalaScriptParser.FOR, 0);
        }

        public ForInitContext forInit() {
            return this.getRuleContext(ForInitContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_forStatement;
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }

        public List<TerminalNode> SEMICOLON() {
            return getTokens(IkalaScriptParser.SEMICOLON);
        }

        public TerminalNode SEMICOLON(int i) {
            return getToken(IkalaScriptParser.SEMICOLON, i);
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
        public ForStatementNoShortIfContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterForStatementNoShortIf(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitForStatementNoShortIf(this);
            }
        }

        public ExpressionContext expression() {
            return this.getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode FOR() {
            return getToken(IkalaScriptParser.FOR, 0);
        }

        public ForInitContext forInit() {
            return this.getRuleContext(ForInitContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_forStatementNoShortIf;
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }

        public List<TerminalNode> SEMICOLON() {
            return getTokens(IkalaScriptParser.SEMICOLON);
        }

        public TerminalNode SEMICOLON(int i) {
            return getToken(IkalaScriptParser.SEMICOLON, i);
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
        public GotoStatementContext(ParserRuleContext parent, int invokingState) {
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
            return getToken(IkalaScriptParser.GOTO, 0);
        }

        public TerminalNode Identifier() {
            return getToken(IkalaScriptParser.Identifier, 0);
        }

        public TerminalNode SEMICOLON() {
            return getToken(IkalaScriptParser.SEMICOLON, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfThenElseStatementContext extends ParserRuleContext {
        public IfThenElseStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ELSE() {
            return getToken(IkalaScriptParser.ELSE, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterIfThenElseStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitIfThenElseStatement(this);
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
            return getToken(IkalaScriptParser.IF, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }

        public StatementContext statement() {
            return this.getRuleContext(StatementContext.class, 0);
        }

        public StatementNoShortIfContext statementNoShortIf() {
            return this.getRuleContext(StatementNoShortIfContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IfThenElseStatementNoShortIfContext extends ParserRuleContext {
        public IfThenElseStatementNoShortIfContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ELSE() {
            return getToken(IkalaScriptParser.ELSE, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterIfThenElseStatementNoShortIf(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitIfThenElseStatementNoShortIf(this);
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
            return getToken(IkalaScriptParser.IF, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
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
        public IfThenStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterIfThenStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitIfThenStatement(this);
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
            return getToken(IkalaScriptParser.IF, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
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
            return getToken(IkalaScriptParser.COLON, 0);
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
            return getToken(IkalaScriptParser.Identifier, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LabeledStatementContext extends ParserRuleContext {
        public LabeledStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterLabeledStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitLabeledStatement(this);
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
    public static class LabeledStatementNoShortIfContext extends ParserRuleContext {
        public LabeledStatementNoShortIfContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterLabeledStatementNoShortIf(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitLabeledStatementNoShortIf(this);
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
        public LeftHandSideContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
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
            return getToken(IkalaScriptParser.Identifier, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LiteralContext extends ParserRuleContext {
        public LiteralContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode BooleanLiteral() {
            return getToken(IkalaScriptParser.BooleanLiteral, 0);
        }

        public TerminalNode CharacterLiteral() {
            return getToken(IkalaScriptParser.CharacterLiteral, 0);
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
            return getToken(IkalaScriptParser.FloatingPointLiteral, 0);
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_literal;
        }

        public TerminalNode IntegerLiteral() {
            return getToken(IkalaScriptParser.IntegerLiteral, 0);
        }

        public TerminalNode NullLiteral() {
            return getToken(IkalaScriptParser.NullLiteral, 0);
        }

        public TerminalNode StringLiteral() {
            return getToken(IkalaScriptParser.StringLiteral, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LocalVariableDeclarationContext extends ParserRuleContext {
        public LocalVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterLocalVariableDeclaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitLocalVariableDeclaration(this);
            }
        }

        public TerminalNode FINAL() {
            return getToken(IkalaScriptParser.FINAL, 0);
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
    public static class LocalVariableDeclarationStatementContext extends ParserRuleContext {
        public LocalVariableDeclarationStatementContext(
                ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterLocalVariableDeclarationStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitLocalVariableDeclarationStatement(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_localVariableDeclarationStatement;
        }

        public LocalVariableDeclarationContext localVariableDeclaration() {
            return this.getRuleContext(LocalVariableDeclarationContext.class, 0);
        }

        public TerminalNode SEMICOLON() {
            return getToken(IkalaScriptParser.SEMICOLON, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MethodInvocation_extensionContext extends ParserRuleContext {
        public MethodInvocation_extensionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public ArgumentListContext argumentList() {
            return this.getRuleContext(ArgumentListContext.class, 0);
        }

        public TerminalNode DOT() {
            return getToken(IkalaScriptParser.DOT, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterMethodInvocation_extension(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitMethodInvocation_extension(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_methodInvocation_extension;
        }

        public TerminalNode Identifier() {
            return getToken(IkalaScriptParser.Identifier, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MethodInvocation_LHSContext extends ParserRuleContext {
        public MethodInvocation_LHSContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public ArgumentListContext argumentList() {
            return this.getRuleContext(ArgumentListContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterMethodInvocation_LHS(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitMethodInvocation_LHS(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_methodInvocation_LHS;
        }

        public TerminalNode Identifier() {
            return getToken(IkalaScriptParser.Identifier, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MethodInvocationContext extends ParserRuleContext {
        public MethodInvocationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public ArgumentListContext argumentList() {
            return this.getRuleContext(ArgumentListContext.class, 0);
        }

        public TerminalNode DOT() {
            return getToken(IkalaScriptParser.DOT, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterMethodInvocation(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitMethodInvocation(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_methodInvocation;
        }

        public TerminalNode Identifier() {
            return getToken(IkalaScriptParser.Identifier, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public PrimaryContext primary() {
            return this.getRuleContext(PrimaryContext.class, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MultiplicativeExpressionContext extends ParserRuleContext {
        public MultiplicativeExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode DIV() {
            return getToken(IkalaScriptParser.DIV, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterMultiplicativeExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitMultiplicativeExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_multiplicativeExpression;
        }

        public TerminalNode MOD() {
            return getToken(IkalaScriptParser.MOD, 0);
        }

        public TerminalNode MUL() {
            return getToken(IkalaScriptParser.MUL, 0);
        }

        public MultiplicativeExpressionContext multiplicativeExpression() {
            return this.getRuleContext(MultiplicativeExpressionContext.class, 0);
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
            return getToken(IkalaScriptParser.CHAR, 0);
        }

        public TerminalNode DOUBLE() {
            return getToken(IkalaScriptParser.DOUBLE, 0);
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
            return getToken(IkalaScriptParser.INT, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PostDecrementExpressionContext extends ParserRuleContext {
        public PostDecrementExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode DEC() {
            return getToken(IkalaScriptParser.DEC, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterPostDecrementExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitPostDecrementExpression(this);
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
        public PostfixExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> DEC() {
            return getTokens(IkalaScriptParser.DEC);
        }

        public TerminalNode DEC(int i) {
            return getToken(IkalaScriptParser.DEC, i);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterPostfixExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitPostfixExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_postfixExpression;
        }

        public TerminalNode Identifier() {
            return getToken(IkalaScriptParser.Identifier, 0);
        }

        public List<TerminalNode> INC() {
            return getTokens(IkalaScriptParser.INC);
        }

        public TerminalNode INC(int i) {
            return getToken(IkalaScriptParser.INC, i);
        }

        public PrimaryContext primary() {
            return this.getRuleContext(PrimaryContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PostIncrementExpressionContext extends ParserRuleContext {
        public PostIncrementExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterPostIncrementExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitPostIncrementExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_postIncrementExpression;
        }

        public TerminalNode INC() {
            return getToken(IkalaScriptParser.INC, 0);
        }

        public PostfixExpressionContext postfixExpression() {
            return this.getRuleContext(PostfixExpressionContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PreDecrementExpressionContext extends ParserRuleContext {
        public PreDecrementExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode DEC() {
            return getToken(IkalaScriptParser.DEC, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterPreDecrementExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitPreDecrementExpression(this);
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
    public static class PreIncrementExpressionContext extends ParserRuleContext {
        public PreIncrementExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterPreIncrementExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitPreIncrementExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_preIncrementExpression;
        }

        public TerminalNode INC() {
            return getToken(IkalaScriptParser.INC, 0);
        }

        public UnaryExpressionContext unaryExpression() {
            return this.getRuleContext(UnaryExpressionContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Primary_extensionContext extends ParserRuleContext {
        public Primary_extensionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterPrimary_extension(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitPrimary_extension(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_primary_extension;
        }

        public MethodInvocation_extensionContext methodInvocation_extension() {
            return this.getRuleContext(MethodInvocation_extensionContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Primary_LHS_accessContext extends ParserRuleContext {
        public Primary_LHS_accessContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterPrimary_LHS_access(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitPrimary_LHS_access(this);
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
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public MethodInvocation_LHSContext methodInvocation_LHS() {
            return this.getRuleContext(MethodInvocation_LHSContext.class, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Primary_LHSContext extends ParserRuleContext {
        public Primary_LHSContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
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
            return getToken(IkalaScriptParser.Identifier, 0);
        }

        public LiteralContext literal() {
            return this.getRuleContext(LiteralContext.class, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public MethodInvocation_LHSContext methodInvocation_LHS() {
            return this.getRuleContext(MethodInvocation_LHSContext.class, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
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
        public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode BOOLEAN() {
            return getToken(IkalaScriptParser.BOOLEAN, 0);
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
            return getToken(IkalaScriptParser.STRING, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ReferenceTypeContext extends ParserRuleContext {
        public ReferenceTypeContext(ParserRuleContext parent, int invokingState) {
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
            return getToken(IkalaScriptParser.Identifier, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class RelationalExpressionContext extends ParserRuleContext {
        public RelationalExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public AdditiveExpressionContext additiveExpression() {
            return this.getRuleContext(AdditiveExpressionContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterRelationalExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitRelationalExpression(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_relationalExpression;
        }

        public TerminalNode GT() {
            return getToken(IkalaScriptParser.GT, 0);
        }

        public TerminalNode GTE() {
            return getToken(IkalaScriptParser.GTE, 0);
        }

        public TerminalNode LT() {
            return getToken(IkalaScriptParser.LT, 0);
        }

        public TerminalNode LTE() {
            return getToken(IkalaScriptParser.LTE, 0);
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

        public StatementWithoutTrailingSubstatementContext statementWithoutTrailingSubstatement() {
            return this.getRuleContext(StatementWithoutTrailingSubstatementContext.class, 0);
        }

        public WhileStatementContext whileStatement() {
            return this.getRuleContext(WhileStatementContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class StatementExpressionContext extends ParserRuleContext {
        public StatementExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public AssignmentContext assignment() {
            return this.getRuleContext(AssignmentContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterStatementExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitStatementExpression(this);
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
    public static class StatementExpressionListContext extends ParserRuleContext {
        public StatementExpressionListContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(IkalaScriptParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(IkalaScriptParser.COMMA, i);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterStatementExpressionList(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitStatementExpressionList(this);
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
        public StatementNoShortIfContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterStatementNoShortIf(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitStatementNoShortIf(this);
            }
        }

        public ForStatementNoShortIfContext forStatementNoShortIf() {
            return this.getRuleContext(ForStatementNoShortIfContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_statementNoShortIf;
        }

        public IfThenElseStatementNoShortIfContext ifThenElseStatementNoShortIf() {
            return this.getRuleContext(IfThenElseStatementNoShortIfContext.class, 0);
        }

        public LabeledStatementNoShortIfContext labeledStatementNoShortIf() {
            return this.getRuleContext(LabeledStatementNoShortIfContext.class, 0);
        }

        public StatementWithoutTrailingSubstatementContext statementWithoutTrailingSubstatement() {
            return this.getRuleContext(StatementWithoutTrailingSubstatementContext.class, 0);
        }

        public WhileStatementNoShortIfContext whileStatementNoShortIf() {
            return this.getRuleContext(WhileStatementNoShortIfContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class StatementWithoutTrailingSubstatementContext extends ParserRuleContext {
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
            return getToken(IkalaScriptParser.LBRACE, 0);
        }

        public TerminalNode RBRACE() {
            return getToken(IkalaScriptParser.RBRACE, 0);
        }

        public List<SwitchBlockStatementGroupContext> switchBlockStatementGroup() {
            return this.getRuleContexts(SwitchBlockStatementGroupContext.class);
        }

        public SwitchBlockStatementGroupContext switchBlockStatementGroup(int i) {
            return this.getRuleContext(SwitchBlockStatementGroupContext.class, i);
        }

        public List<SwitchLabelContext> switchLabel() {
            return this.getRuleContexts(SwitchLabelContext.class);
        }

        public SwitchLabelContext switchLabel(int i) {
            return this.getRuleContext(SwitchLabelContext.class, i);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SwitchBlockStatementGroupContext extends ParserRuleContext {
        public SwitchBlockStatementGroupContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public BlockStatementsContext blockStatements() {
            return this.getRuleContext(BlockStatementsContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterSwitchBlockStatementGroup(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitSwitchBlockStatementGroup(this);
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
            return getToken(IkalaScriptParser.CASE, 0);
        }

        public TerminalNode COLON() {
            return getToken(IkalaScriptParser.COLON, 0);
        }

        public TerminalNode DEFAULT() {
            return getToken(IkalaScriptParser.DEFAULT, 0);
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
        public SwitchStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterSwitchStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitSwitchStatement(this);
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
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }

        public TerminalNode SWITCH() {
            return getToken(IkalaScriptParser.SWITCH, 0);
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
        public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ADD() {
            return getToken(IkalaScriptParser.ADD, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterUnaryExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitUnaryExpression(this);
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
            return getToken(IkalaScriptParser.SUB, 0);
        }

        public UnaryExpressionContext unaryExpression() {
            return this.getRuleContext(UnaryExpressionContext.class, 0);
        }

        public UnaryExpressionNotPlusMinusContext unaryExpressionNotPlusMinus() {
            return this.getRuleContext(UnaryExpressionNotPlusMinusContext.class, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class UnaryExpressionNotPlusMinusContext extends ParserRuleContext {
        public UnaryExpressionNotPlusMinusContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public CastExpressionContext castExpression() {
            return this.getRuleContext(CastExpressionContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterUnaryExpressionNotPlusMinus(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitUnaryExpressionNotPlusMinus(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_unaryExpressionNotPlusMinus;
        }

        public TerminalNode NOT() {
            return getToken(IkalaScriptParser.NOT, 0);
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
        public VariableDeclaratorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ASSIGN() {
            return getToken(IkalaScriptParser.ASSIGN, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterVariableDeclarator(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitVariableDeclarator(this);
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
        public VariableDeclaratorIdContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public DimsContext dims() {
            return this.getRuleContext(DimsContext.class, 0);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterVariableDeclaratorId(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitVariableDeclaratorId(this);
            }
        }

        @Override
        public int getRuleIndex() {
            return IkalaScriptParser.RULE_variableDeclaratorId;
        }

        public TerminalNode Identifier() {
            return getToken(IkalaScriptParser.Identifier, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class VariableDeclaratorListContext extends ParserRuleContext {
        public VariableDeclaratorListContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(IkalaScriptParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(IkalaScriptParser.COMMA, i);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterVariableDeclaratorList(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitVariableDeclaratorList(this);
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
        public WhileStatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterWhileStatement(this);
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
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }

        public StatementContext statement() {
            return this.getRuleContext(StatementContext.class, 0);
        }

        public TerminalNode WHILE() {
            return getToken(IkalaScriptParser.WHILE, 0);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class WhileStatementNoShortIfContext extends ParserRuleContext {
        public WhileStatementNoShortIfContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).enterWhileStatementNoShortIf(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof IkalaScriptParserListener) {
                ((IkalaScriptParserListener) listener).exitWhileStatementNoShortIf(this);
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
            return getToken(IkalaScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(IkalaScriptParser.RPAREN, 0);
        }

        public StatementNoShortIfContext statementNoShortIf() {
            return this.getRuleContext(StatementNoShortIfContext.class, 0);
        }

        public TerminalNode WHILE() {
            return getToken(IkalaScriptParser.WHILE, 0);
        }
    }

    static {
        RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;

    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();

    public static final int BOOLEAN = 1,
            BREAK = 2,
            CASE = 3,
            CHAR = 4,
            CONTINUE = 5,
            DEFAULT = 6,
            DO = 7,
            DOUBLE = 8,
            ELSE = 9,
            EXIT = 10,
            FINAL = 11,
            FOR = 12,
            GOTO = 13,
            IF = 14,
            INT = 15,
            STRING = 16,
            SWITCH = 17,
            VOID = 18,
            WHILE = 19,
            IntegerLiteral = 20,
            FloatingPointLiteral = 21,
            BooleanLiteral = 22,
            CharacterLiteral = 23,
            StringLiteral = 24,
            NullLiteral = 25,
            Identifier = 26,
            LPAREN = 27,
            RPAREN = 28,
            LBRACE = 29,
            RBRACE = 30,
            LBRACK = 31,
            RBRACK = 32,
            SEMICOLON = 33,
            COMMA = 34,
            DOT = 35,
            ASSIGN = 36,
            GT = 37,
            LT = 38,
            NOT = 39,
            QUESTION = 40,
            COLON = 41,
            EQUAL = 42,
            LTE = 43,
            GTE = 44,
            NOTEQUAL = 45,
            AND = 46,
            OR = 47,
            INC = 48,
            DEC = 49,
            ADD = 50,
            SUB = 51,
            MUL = 52,
            DIV = 53,
            MOD = 54,
            ADD_ASSIGN = 55,
            SUB_ASSIGN = 56,
            MUL_ASSIGN = 57,
            DIV_ASSIGN = 58,
            MOD_ASSIGN = 59,
            WS = 60,
            COMMENT = 61,
            LINE_COMMENT = 62;
    public static final int RULE_literal = 0,
            RULE_primitiveType = 1,
            RULE_numericType = 2,
            RULE_referenceType = 3,
            RULE_classOrInterfaceType = 4,
            RULE_arrayType = 5,
            RULE_dims = 6,
            RULE_variableDeclaratorList = 7,
            RULE_variableDeclarator = 8,
            RULE_variableDeclaratorId = 9,
            RULE_type = 10,
            RULE_compilationUnit = 11,
            RULE_block = 12,
            RULE_blockStatements = 13,
            RULE_blockStatement = 14,
            RULE_localVariableDeclarationStatement = 15,
            RULE_localVariableDeclaration = 16,
            RULE_statement = 17,
            RULE_statementNoShortIf = 18,
            RULE_statementWithoutTrailingSubstatement = 19,
            RULE_label = 20,
            RULE_labeledStatement = 21,
            RULE_labeledStatementNoShortIf = 22,
            RULE_emptyStatement = 23,
            RULE_expressionStatement = 24,
            RULE_statementExpression = 25,
            RULE_ifThenStatement = 26,
            RULE_ifThenElseStatement = 27,
            RULE_ifThenElseStatementNoShortIf = 28,
            RULE_switchStatement = 29,
            RULE_switchBlock = 30,
            RULE_switchBlockStatementGroup = 31,
            RULE_switchLabel = 32,
            RULE_whileStatement = 33,
            RULE_whileStatementNoShortIf = 34,
            RULE_doStatement = 35,
            RULE_forStatement = 36,
            RULE_forStatementNoShortIf = 37,
            RULE_forInit = 38,
            RULE_statementExpressionList = 39,
            RULE_breakStatement = 40,
            RULE_continueStatement = 41,
            RULE_gotoStatement = 42,
            RULE_exitStatement = 43,
            RULE_primary = 44,
            RULE_primary_extension = 45,
            RULE_primary_LHS = 46,
            RULE_primary_LHS_access = 47,
            RULE_methodInvocation = 48,
            RULE_methodInvocation_extension = 49,
            RULE_methodInvocation_LHS = 50,
            RULE_argumentList = 51,
            RULE_expression = 52,
            RULE_assignment = 53,
            RULE_leftHandSide = 54,
            RULE_assignmentOperator = 55,
            RULE_conditionalExpression = 56,
            RULE_conditionalOrExpression = 57,
            RULE_conditionalAndExpression = 58,
            RULE_equalityExpression = 59,
            RULE_relationalExpression = 60,
            RULE_additiveExpression = 61,
            RULE_multiplicativeExpression = 62,
            RULE_unaryExpression = 63,
            RULE_preIncrementExpression = 64,
            RULE_preDecrementExpression = 65,
            RULE_unaryExpressionNotPlusMinus = 66,
            RULE_postfixExpression = 67,
            RULE_postIncrementExpression = 68,
            RULE_postDecrementExpression = 69,
            RULE_castExpression = 70;

    public static final String[] ruleNames = IkalaScriptParser.makeRuleNames();

    private static final String[] _LITERAL_NAMES = IkalaScriptParser.makeLiteralNames();

    private static final String[] _SYMBOLIC_NAMES = IkalaScriptParser.makeSymbolicNames();

    public static final Vocabulary VOCABULARY =
            new VocabularyImpl(IkalaScriptParser._LITERAL_NAMES, IkalaScriptParser._SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated public static final String[] tokenNames;

    static {
        tokenNames = new String[IkalaScriptParser._SYMBOLIC_NAMES.length];
        for (int i = 0; i < IkalaScriptParser.tokenNames.length; i++) {
            IkalaScriptParser.tokenNames[i] = IkalaScriptParser.VOCABULARY.getLiteralName(i);
            if (IkalaScriptParser.tokenNames[i] == null) {
                IkalaScriptParser.tokenNames[i] = IkalaScriptParser.VOCABULARY.getSymbolicName(i);
            }

            if (IkalaScriptParser.tokenNames[i] == null) {
                IkalaScriptParser.tokenNames[i] = "<INVALID>";
            }
        }
    }

    public static final String _serializedATN =
            "\u0004\u0001>\u0284\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"
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
                    + "F\u0007F\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0003"
                    + "\u0001\u0094\b\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"
                    + "\u0003\u0003\u0003\u009b\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0005"
                    + "\u0004\u00a0\b\u0004\n\u0004\f\u0004\u00a3\t\u0004\u0001\u0005\u0001\u0005"
                    + "\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"
                    + "\u0003\u0005\u00ad\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"
                    + "\u0005\u0006\u00b3\b\u0006\n\u0006\f\u0006\u00b6\t\u0006\u0001\u0007\u0001"
                    + "\u0007\u0001\u0007\u0005\u0007\u00bb\b\u0007\n\u0007\f\u0007\u00be\t\u0007"
                    + "\u0001\b\u0001\b\u0001\b\u0003\b\u00c3\b\b\u0001\t\u0001\t\u0003\t\u00c7"
                    + "\b\t\u0001\n\u0001\n\u0003\n\u00cb\b\n\u0001\u000b\u0005\u000b\u00ce\b"
                    + "\u000b\n\u000b\f\u000b\u00d1\t\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001"
                    + "\f\u0003\f\u00d7\b\f\u0001\f\u0001\f\u0001\r\u0004\r\u00dc\b\r\u000b\r"
                    + "\f\r\u00dd\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00e3\b\u000e"
                    + "\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0003\u0010\u00e9\b\u0010"
                    + "\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011"
                    + "\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00f4\b\u0011\u0001\u0012"
                    + "\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00fb\b\u0012"
                    + "\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"
                    + "\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0106\b\u0013\u0001\u0014"
                    + "\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016"
                    + "\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018"
                    + "\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"
                    + "\u0001\u0019\u0003\u0019\u011c\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a"
                    + "\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b"
                    + "\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c"
                    + "\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"
                    + "\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"
                    + "\u0001\u001d\u0001\u001e\u0001\u001e\u0005\u001e\u013c\b\u001e\n\u001e"
                    + "\f\u001e\u013f\t\u001e\u0001\u001e\u0005\u001e\u0142\b\u001e\n\u001e\f"
                    + "\u001e\u0145\t\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0004\u001f\u014a"
                    + "\b\u001f\u000b\u001f\f\u001f\u014b\u0001\u001f\u0001\u001f\u0001 \u0001"
                    + " \u0001 \u0001 \u0001 \u0001 \u0003 \u0156\b \u0001!\u0001!\u0001!\u0001"
                    + "!\u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001"
                    + "#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001$\u0001$\u0001"
                    + "$\u0003$\u016f\b$\u0001$\u0001$\u0003$\u0173\b$\u0001$\u0001$\u0003$\u0177"
                    + "\b$\u0001$\u0001$\u0001$\u0001%\u0001%\u0001%\u0003%\u017f\b%\u0001%\u0001"
                    + "%\u0003%\u0183\b%\u0001%\u0001%\u0003%\u0187\b%\u0001%\u0001%\u0001%\u0001"
                    + "&\u0001&\u0003&\u018e\b&\u0001\'\u0001\'\u0001\'\u0005\'\u0193\b\'\n\'"
                    + "\f\'\u0196\t\'\u0001(\u0001(\u0001(\u0001)\u0001)\u0001)\u0001*\u0001"
                    + "*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001,\u0001,\u0005,\u01a7\b,\n,"
                    + "\f,\u01aa\t,\u0001-\u0001-\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001"
                    + ".\u0003.\u01b5\b.\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0003/\u01bd"
                    + "\b/\u00010\u00010\u00010\u00030\u01c2\b0\u00010\u00010\u00010\u00010\u0001"
                    + "0\u00010\u00030\u01ca\b0\u00010\u00010\u00030\u01ce\b0\u00011\u00011\u0001"
                    + "1\u00011\u00031\u01d4\b1\u00011\u00011\u00012\u00012\u00012\u00032\u01db"
                    + "\b2\u00012\u00012\u00013\u00013\u00013\u00053\u01e2\b3\n3\f3\u01e5\t3"
                    + "\u00014\u00014\u00034\u01e9\b4\u00015\u00015\u00015\u00015\u00016\u0001"
                    + "6\u00017\u00017\u00018\u00018\u00018\u00018\u00018\u00018\u00018\u0003"
                    + "8\u01fa\b8\u00019\u00019\u00019\u00019\u00019\u00019\u00059\u0202\b9\n"
                    + "9\f9\u0205\t9\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0005:\u020d\b"
                    + ":\n:\f:\u0210\t:\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"
                    + ";\u0001;\u0005;\u021b\b;\n;\f;\u021e\t;\u0001<\u0001<\u0001<\u0001<\u0001"
                    + "<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001<\u0001"
                    + "<\u0005<\u022f\b<\n<\f<\u0232\t<\u0001=\u0001=\u0001=\u0001=\u0001=\u0001"
                    + "=\u0001=\u0001=\u0001=\u0005=\u023d\b=\n=\f=\u0240\t=\u0001>\u0001>\u0001"
                    + ">\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0005"
                    + ">\u024e\b>\n>\f>\u0251\t>\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001"
                    + "?\u0003?\u025a\b?\u0001@\u0001@\u0001@\u0001A\u0001A\u0001A\u0001B\u0001"
                    + "B\u0001B\u0001B\u0003B\u0266\bB\u0001C\u0001C\u0003C\u026a\bC\u0001C\u0005"
                    + "C\u026d\bC\nC\fC\u0270\tC\u0001D\u0001D\u0001D\u0001E\u0001E\u0001E\u0001"
                    + "F\u0001F\u0001F\u0001F\u0001F\u0001F\u0001F\u0001F\u0001F\u0001F\u0003"
                    + "F\u0282\bF\u0001F\u0000\u0006rtvxz|G\u0000\u0002\u0004\u0006\b\n\f\u000e"
                    + "\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"
                    + "HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"
                    + "\u0000\u0004\u0001\u0000\u0014\u0019\u0003\u0000\u0004\u0004\b\b\u000f"
                    + "\u000f\u0002\u0000$$7;\u0001\u000001\u0294\u0000\u008e\u0001\u0000\u0000"
                    + "\u0000\u0002\u0093\u0001\u0000\u0000\u0000\u0004\u0095\u0001\u0000\u0000"
                    + "\u0000\u0006\u009a\u0001\u0000\u0000\u0000\b\u009c\u0001\u0000\u0000\u0000"
                    + "\n\u00ac\u0001\u0000\u0000\u0000\f\u00ae\u0001\u0000\u0000\u0000\u000e"
                    + "\u00b7\u0001\u0000\u0000\u0000\u0010\u00bf\u0001\u0000\u0000\u0000\u0012"
                    + "\u00c4\u0001\u0000\u0000\u0000\u0014\u00ca\u0001\u0000\u0000\u0000\u0016"
                    + "\u00cf\u0001\u0000\u0000\u0000\u0018\u00d4\u0001\u0000\u0000\u0000\u001a"
                    + "\u00db\u0001\u0000\u0000\u0000\u001c\u00e2\u0001\u0000\u0000\u0000\u001e"
                    + "\u00e4\u0001\u0000\u0000\u0000 \u00e8\u0001\u0000\u0000\u0000\"\u00f3"
                    + "\u0001\u0000\u0000\u0000$\u00fa\u0001\u0000\u0000\u0000&\u0105\u0001\u0000"
                    + "\u0000\u0000(\u0107\u0001\u0000\u0000\u0000*\u010a\u0001\u0000\u0000\u0000"
                    + ",\u010d\u0001\u0000\u0000\u0000.\u0110\u0001\u0000\u0000\u00000\u0112"
                    + "\u0001\u0000\u0000\u00002\u011b\u0001\u0000\u0000\u00004\u011d\u0001\u0000"
                    + "\u0000\u00006\u0123\u0001\u0000\u0000\u00008\u012b\u0001\u0000\u0000\u0000"
                    + ":\u0133\u0001\u0000\u0000\u0000<\u0139\u0001\u0000\u0000\u0000>\u0149"
                    + "\u0001\u0000\u0000\u0000@\u0155\u0001\u0000\u0000\u0000B\u0157\u0001\u0000"
                    + "\u0000\u0000D\u015d\u0001\u0000\u0000\u0000F\u0163\u0001\u0000\u0000\u0000"
                    + "H\u016b\u0001\u0000\u0000\u0000J\u017b\u0001\u0000\u0000\u0000L\u018d"
                    + "\u0001\u0000\u0000\u0000N\u018f\u0001\u0000\u0000\u0000P\u0197\u0001\u0000"
                    + "\u0000\u0000R\u019a\u0001\u0000\u0000\u0000T\u019d\u0001\u0000\u0000\u0000"
                    + "V\u01a1\u0001\u0000\u0000\u0000X\u01a4\u0001\u0000\u0000\u0000Z\u01ab"
                    + "\u0001\u0000\u0000\u0000\\\u01b4\u0001\u0000\u0000\u0000^\u01bc\u0001"
                    + "\u0000\u0000\u0000`\u01cd\u0001\u0000\u0000\u0000b\u01cf\u0001\u0000\u0000"
                    + "\u0000d\u01d7\u0001\u0000\u0000\u0000f\u01de\u0001\u0000\u0000\u0000h"
                    + "\u01e8\u0001\u0000\u0000\u0000j\u01ea\u0001\u0000\u0000\u0000l\u01ee\u0001"
                    + "\u0000\u0000\u0000n\u01f0\u0001\u0000\u0000\u0000p\u01f9\u0001\u0000\u0000"
                    + "\u0000r\u01fb\u0001\u0000\u0000\u0000t\u0206\u0001\u0000\u0000\u0000v"
                    + "\u0211\u0001\u0000\u0000\u0000x\u021f\u0001\u0000\u0000\u0000z\u0233\u0001"
                    + "\u0000\u0000\u0000|\u0241\u0001\u0000\u0000\u0000~\u0259\u0001\u0000\u0000"
                    + "\u0000\u0080\u025b\u0001\u0000\u0000\u0000\u0082\u025e\u0001\u0000\u0000"
                    + "\u0000\u0084\u0265\u0001\u0000\u0000\u0000\u0086\u0269\u0001\u0000\u0000"
                    + "\u0000\u0088\u0271\u0001\u0000\u0000\u0000\u008a\u0274\u0001\u0000\u0000"
                    + "\u0000\u008c\u0281\u0001\u0000\u0000\u0000\u008e\u008f\u0007\u0000\u0000"
                    + "\u0000\u008f\u0001\u0001\u0000\u0000\u0000\u0090\u0094\u0003\u0004\u0002"
                    + "\u0000\u0091\u0094\u0005\u0001\u0000\u0000\u0092\u0094\u0005\u0010\u0000"
                    + "\u0000\u0093\u0090\u0001\u0000\u0000\u0000\u0093\u0091\u0001\u0000\u0000"
                    + "\u0000\u0093\u0092\u0001\u0000\u0000\u0000\u0094\u0003\u0001\u0000\u0000"
                    + "\u0000\u0095\u0096\u0007\u0001\u0000\u0000\u0096\u0005\u0001\u0000\u0000"
                    + "\u0000\u0097\u009b\u0003\b\u0004\u0000\u0098\u009b\u0005\u001a\u0000\u0000"
                    + "\u0099\u009b\u0003\n\u0005\u0000\u009a\u0097\u0001\u0000\u0000\u0000\u009a"
                    + "\u0098\u0001\u0000\u0000\u0000\u009a\u0099\u0001\u0000\u0000\u0000\u009b"
                    + "\u0007\u0001\u0000\u0000\u0000\u009c\u00a1\u0005\u001a\u0000\u0000\u009d"
                    + "\u009e\u0005#\u0000\u0000\u009e\u00a0\u0005\u001a\u0000\u0000\u009f\u009d"
                    + "\u0001\u0000\u0000\u0000\u00a0\u00a3\u0001\u0000\u0000\u0000\u00a1\u009f"
                    + "\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2\t\u0001"
                    + "\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a4\u00a5\u0003"
                    + "\u0002\u0001\u0000\u00a5\u00a6\u0003\f\u0006\u0000\u00a6\u00ad\u0001\u0000"
                    + "\u0000\u0000\u00a7\u00a8\u0003\b\u0004\u0000\u00a8\u00a9\u0003\f\u0006"
                    + "\u0000\u00a9\u00ad\u0001\u0000\u0000\u0000\u00aa\u00ab\u0005\u001a\u0000"
                    + "\u0000\u00ab\u00ad\u0003\f\u0006\u0000\u00ac\u00a4\u0001\u0000\u0000\u0000"
                    + "\u00ac\u00a7\u0001\u0000\u0000\u0000\u00ac\u00aa\u0001\u0000\u0000\u0000"
                    + "\u00ad\u000b\u0001\u0000\u0000\u0000\u00ae\u00af\u0005\u001f\u0000\u0000"
                    + "\u00af\u00b4\u0005 \u0000\u0000\u00b0\u00b1\u0005\u001f\u0000\u0000\u00b1"
                    + "\u00b3\u0005 \u0000\u0000\u00b2\u00b0\u0001\u0000\u0000\u0000\u00b3\u00b6"
                    + "\u0001\u0000\u0000\u0000\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b4\u00b5"
                    + "\u0001\u0000\u0000\u0000\u00b5\r\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001"
                    + "\u0000\u0000\u0000\u00b7\u00bc\u0003\u0010\b\u0000\u00b8\u00b9\u0005\""
                    + "\u0000\u0000\u00b9\u00bb\u0003\u0010\b\u0000\u00ba\u00b8\u0001\u0000\u0000"
                    + "\u0000\u00bb\u00be\u0001\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000"
                    + "\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd\u000f\u0001\u0000\u0000"
                    + "\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00bf\u00c2\u0003\u0012\t\u0000"
                    + "\u00c0\u00c1\u0005$\u0000\u0000\u00c1\u00c3\u0003h4\u0000\u00c2\u00c0"
                    + "\u0001\u0000\u0000\u0000\u00c2\u00c3\u0001\u0000\u0000\u0000\u00c3\u0011"
                    + "\u0001\u0000\u0000\u0000\u00c4\u00c6\u0005\u001a\u0000\u0000\u00c5\u00c7"
                    + "\u0003\f\u0006\u0000\u00c6\u00c5\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001"
                    + "\u0000\u0000\u0000\u00c7\u0013\u0001\u0000\u0000\u0000\u00c8\u00cb\u0003"
                    + "\u0002\u0001\u0000\u00c9\u00cb\u0003\u0006\u0003\u0000\u00ca\u00c8\u0001"
                    + "\u0000\u0000\u0000\u00ca\u00c9\u0001\u0000\u0000\u0000\u00cb\u0015\u0001"
                    + "\u0000\u0000\u0000\u00cc\u00ce\u0003\u001c\u000e\u0000\u00cd\u00cc\u0001"
                    + "\u0000\u0000\u0000\u00ce\u00d1\u0001\u0000\u0000\u0000\u00cf\u00cd\u0001"
                    + "\u0000\u0000\u0000\u00cf\u00d0\u0001\u0000\u0000\u0000\u00d0\u00d2\u0001"
                    + "\u0000\u0000\u0000\u00d1\u00cf\u0001\u0000\u0000\u0000\u00d2\u00d3\u0005"
                    + "\u0000\u0000\u0001\u00d3\u0017\u0001\u0000\u0000\u0000\u00d4\u00d6\u0005"
                    + "\u001d\u0000\u0000\u00d5\u00d7\u0003\u001a\r\u0000\u00d6\u00d5\u0001\u0000"
                    + "\u0000\u0000\u00d6\u00d7\u0001\u0000\u0000\u0000\u00d7\u00d8\u0001\u0000"
                    + "\u0000\u0000\u00d8\u00d9\u0005\u001e\u0000\u0000\u00d9\u0019\u0001\u0000"
                    + "\u0000\u0000\u00da\u00dc\u0003\u001c\u000e\u0000\u00db\u00da\u0001\u0000"
                    + "\u0000\u0000\u00dc\u00dd\u0001\u0000\u0000\u0000\u00dd\u00db\u0001\u0000"
                    + "\u0000\u0000\u00dd\u00de\u0001\u0000\u0000\u0000\u00de\u001b\u0001\u0000"
                    + "\u0000\u0000\u00df\u00e3\u0003\u001e\u000f\u0000\u00e0\u00e3\u0003\"\u0011"
                    + "\u0000\u00e1\u00e3\u0003(\u0014\u0000\u00e2\u00df\u0001\u0000\u0000\u0000"
                    + "\u00e2\u00e0\u0001\u0000\u0000\u0000\u00e2\u00e1\u0001\u0000\u0000\u0000"
                    + "\u00e3\u001d\u0001\u0000\u0000\u0000\u00e4\u00e5\u0003 \u0010\u0000\u00e5"
                    + "\u00e6\u0005!\u0000\u0000\u00e6\u001f\u0001\u0000\u0000\u0000\u00e7\u00e9"
                    + "\u0005\u000b\u0000\u0000\u00e8\u00e7\u0001\u0000\u0000\u0000\u00e8\u00e9"
                    + "\u0001\u0000\u0000\u0000\u00e9\u00ea\u0001\u0000\u0000\u0000\u00ea\u00eb"
                    + "\u0003\u0014\n\u0000\u00eb\u00ec\u0003\u000e\u0007\u0000\u00ec!\u0001"
                    + "\u0000\u0000\u0000\u00ed\u00f4\u0003&\u0013\u0000\u00ee\u00f4\u0003*\u0015"
                    + "\u0000\u00ef\u00f4\u00034\u001a\u0000\u00f0\u00f4\u00036\u001b\u0000\u00f1"
                    + "\u00f4\u0003B!\u0000\u00f2\u00f4\u0003H$\u0000\u00f3\u00ed\u0001\u0000"
                    + "\u0000\u0000\u00f3\u00ee\u0001\u0000\u0000\u0000\u00f3\u00ef\u0001\u0000"
                    + "\u0000\u0000\u00f3\u00f0\u0001\u0000\u0000\u0000\u00f3\u00f1\u0001\u0000"
                    + "\u0000\u0000\u00f3\u00f2\u0001\u0000\u0000\u0000\u00f4#\u0001\u0000\u0000"
                    + "\u0000\u00f5\u00fb\u0003&\u0013\u0000\u00f6\u00fb\u0003,\u0016\u0000\u00f7"
                    + "\u00fb\u00038\u001c\u0000\u00f8\u00fb\u0003D\"\u0000\u00f9\u00fb\u0003"
                    + "J%\u0000\u00fa\u00f5\u0001\u0000\u0000\u0000\u00fa\u00f6\u0001\u0000\u0000"
                    + "\u0000\u00fa\u00f7\u0001\u0000\u0000\u0000\u00fa\u00f8\u0001\u0000\u0000"
                    + "\u0000\u00fa\u00f9\u0001\u0000\u0000\u0000\u00fb%\u0001\u0000\u0000\u0000"
                    + "\u00fc\u0106\u0003\u0018\f\u0000\u00fd\u0106\u0003.\u0017\u0000\u00fe"
                    + "\u0106\u00030\u0018\u0000\u00ff\u0106\u0003:\u001d\u0000\u0100\u0106\u0003"
                    + "F#\u0000\u0101\u0106\u0003P(\u0000\u0102\u0106\u0003R)\u0000\u0103\u0106"
                    + "\u0003T*\u0000\u0104\u0106\u0003V+\u0000\u0105\u00fc\u0001\u0000\u0000"
                    + "\u0000\u0105\u00fd\u0001\u0000\u0000\u0000\u0105\u00fe\u0001\u0000\u0000"
                    + "\u0000\u0105\u00ff\u0001\u0000\u0000\u0000\u0105\u0100\u0001\u0000\u0000"
                    + "\u0000\u0105\u0101\u0001\u0000\u0000\u0000\u0105\u0102\u0001\u0000\u0000"
                    + "\u0000\u0105\u0103\u0001\u0000\u0000\u0000\u0105\u0104\u0001\u0000\u0000"
                    + "\u0000\u0106\'\u0001\u0000\u0000\u0000\u0107\u0108\u0005\u001a\u0000\u0000"
                    + "\u0108\u0109\u0005)\u0000\u0000\u0109)\u0001\u0000\u0000\u0000\u010a\u010b"
                    + "\u0003(\u0014\u0000\u010b\u010c\u0003\"\u0011\u0000\u010c+\u0001\u0000"
                    + "\u0000\u0000\u010d\u010e\u0003(\u0014\u0000\u010e\u010f\u0003$\u0012\u0000"
                    + "\u010f-\u0001\u0000\u0000\u0000\u0110\u0111\u0005!\u0000\u0000\u0111/"
                    + "\u0001\u0000\u0000\u0000\u0112\u0113\u00032\u0019\u0000\u0113\u0114\u0005"
                    + "!\u0000\u0000\u01141\u0001\u0000\u0000\u0000\u0115\u011c\u0003j5\u0000"
                    + "\u0116\u011c\u0003\u0080@\u0000\u0117\u011c\u0003\u0082A\u0000\u0118\u011c"
                    + "\u0003\u0088D\u0000\u0119\u011c\u0003\u008aE\u0000\u011a\u011c\u0003`"
                    + "0\u0000\u011b\u0115\u0001\u0000\u0000\u0000\u011b\u0116\u0001\u0000\u0000"
                    + "\u0000\u011b\u0117\u0001\u0000\u0000\u0000\u011b\u0118\u0001\u0000\u0000"
                    + "\u0000\u011b\u0119\u0001\u0000\u0000\u0000\u011b\u011a\u0001\u0000\u0000"
                    + "\u0000\u011c3\u0001\u0000\u0000\u0000\u011d\u011e\u0005\u000e\u0000\u0000"
                    + "\u011e\u011f\u0005\u001b\u0000\u0000\u011f\u0120\u0003h4\u0000\u0120\u0121"
                    + "\u0005\u001c\u0000\u0000\u0121\u0122\u0003\"\u0011\u0000\u01225\u0001"
                    + "\u0000\u0000\u0000\u0123\u0124\u0005\u000e\u0000\u0000\u0124\u0125\u0005"
                    + "\u001b\u0000\u0000\u0125\u0126\u0003h4\u0000\u0126\u0127\u0005\u001c\u0000"
                    + "\u0000\u0127\u0128\u0003$\u0012\u0000\u0128\u0129\u0005\t\u0000\u0000"
                    + "\u0129\u012a\u0003\"\u0011\u0000\u012a7\u0001\u0000\u0000\u0000\u012b"
                    + "\u012c\u0005\u000e\u0000\u0000\u012c\u012d\u0005\u001b\u0000\u0000\u012d"
                    + "\u012e\u0003h4\u0000\u012e\u012f\u0005\u001c\u0000\u0000\u012f\u0130\u0003"
                    + "$\u0012\u0000\u0130\u0131\u0005\t\u0000\u0000\u0131\u0132\u0003$\u0012"
                    + "\u0000\u01329\u0001\u0000\u0000\u0000\u0133\u0134\u0005\u0011\u0000\u0000"
                    + "\u0134\u0135\u0005\u001b\u0000\u0000\u0135\u0136\u0003h4\u0000\u0136\u0137"
                    + "\u0005\u001c\u0000\u0000\u0137\u0138\u0003<\u001e\u0000\u0138;\u0001\u0000"
                    + "\u0000\u0000\u0139\u013d\u0005\u001d\u0000\u0000\u013a\u013c\u0003>\u001f"
                    + "\u0000\u013b\u013a\u0001\u0000\u0000\u0000\u013c\u013f\u0001\u0000\u0000"
                    + "\u0000\u013d\u013b\u0001\u0000\u0000\u0000\u013d\u013e\u0001\u0000\u0000"
                    + "\u0000\u013e\u0143\u0001\u0000\u0000\u0000\u013f\u013d\u0001\u0000\u0000"
                    + "\u0000\u0140\u0142\u0003@ \u0000\u0141\u0140\u0001\u0000\u0000\u0000\u0142"
                    + "\u0145\u0001\u0000\u0000\u0000\u0143\u0141\u0001\u0000\u0000\u0000\u0143"
                    + "\u0144\u0001\u0000\u0000\u0000\u0144\u0146\u0001\u0000\u0000\u0000\u0145"
                    + "\u0143\u0001\u0000\u0000\u0000\u0146\u0147\u0005\u001e\u0000\u0000\u0147"
                    + "=\u0001\u0000\u0000\u0000\u0148\u014a\u0003@ \u0000\u0149\u0148\u0001"
                    + "\u0000\u0000\u0000\u014a\u014b\u0001\u0000\u0000\u0000\u014b\u0149\u0001"
                    + "\u0000\u0000\u0000\u014b\u014c\u0001\u0000\u0000\u0000\u014c\u014d\u0001"
                    + "\u0000\u0000\u0000\u014d\u014e\u0003\u001a\r\u0000\u014e?\u0001\u0000"
                    + "\u0000\u0000\u014f\u0150\u0005\u0003\u0000\u0000\u0150\u0151\u0003h4\u0000"
                    + "\u0151\u0152\u0005)\u0000\u0000\u0152\u0156\u0001\u0000\u0000\u0000\u0153"
                    + "\u0154\u0005\u0006\u0000\u0000\u0154\u0156\u0005)\u0000\u0000\u0155\u014f"
                    + "\u0001\u0000\u0000\u0000\u0155\u0153\u0001\u0000\u0000\u0000\u0156A\u0001"
                    + "\u0000\u0000\u0000\u0157\u0158\u0005\u0013\u0000\u0000\u0158\u0159\u0005"
                    + "\u001b\u0000\u0000\u0159\u015a\u0003h4\u0000\u015a\u015b\u0005\u001c\u0000"
                    + "\u0000\u015b\u015c\u0003\"\u0011\u0000\u015cC\u0001\u0000\u0000\u0000"
                    + "\u015d\u015e\u0005\u0013\u0000\u0000\u015e\u015f\u0005\u001b\u0000\u0000"
                    + "\u015f\u0160\u0003h4\u0000\u0160\u0161\u0005\u001c\u0000\u0000\u0161\u0162"
                    + "\u0003$\u0012\u0000\u0162E\u0001\u0000\u0000\u0000\u0163\u0164\u0005\u0007"
                    + "\u0000\u0000\u0164\u0165\u0003\"\u0011\u0000\u0165\u0166\u0005\u0013\u0000"
                    + "\u0000\u0166\u0167\u0005\u001b\u0000\u0000\u0167\u0168\u0003h4\u0000\u0168"
                    + "\u0169\u0005\u001c\u0000\u0000\u0169\u016a\u0005!\u0000\u0000\u016aG\u0001"
                    + "\u0000\u0000\u0000\u016b\u016c\u0005\f\u0000\u0000\u016c\u016e\u0005\u001b"
                    + "\u0000\u0000\u016d\u016f\u0003L&\u0000\u016e\u016d\u0001\u0000\u0000\u0000"
                    + "\u016e\u016f\u0001\u0000\u0000\u0000\u016f\u0170\u0001\u0000\u0000\u0000"
                    + "\u0170\u0172\u0005!\u0000\u0000\u0171\u0173\u0003h4\u0000\u0172\u0171"
                    + "\u0001\u0000\u0000\u0000\u0172\u0173\u0001\u0000\u0000\u0000\u0173\u0174"
                    + "\u0001\u0000\u0000\u0000\u0174\u0176\u0005!\u0000\u0000\u0175\u0177\u0003"
                    + "N\'\u0000\u0176\u0175\u0001\u0000\u0000\u0000\u0176\u0177\u0001\u0000"
                    + "\u0000\u0000\u0177\u0178\u0001\u0000\u0000\u0000\u0178\u0179\u0005\u001c"
                    + "\u0000\u0000\u0179\u017a\u0003\"\u0011\u0000\u017aI\u0001\u0000\u0000"
                    + "\u0000\u017b\u017c\u0005\f\u0000\u0000\u017c\u017e\u0005\u001b\u0000\u0000"
                    + "\u017d\u017f\u0003L&\u0000\u017e\u017d\u0001\u0000\u0000\u0000\u017e\u017f"
                    + "\u0001\u0000\u0000\u0000\u017f\u0180\u0001\u0000\u0000\u0000\u0180\u0182"
                    + "\u0005!\u0000\u0000\u0181\u0183\u0003h4\u0000\u0182\u0181\u0001\u0000"
                    + "\u0000\u0000\u0182\u0183\u0001\u0000\u0000\u0000\u0183\u0184\u0001\u0000"
                    + "\u0000\u0000\u0184\u0186\u0005!\u0000\u0000\u0185\u0187\u0003N\'\u0000"
                    + "\u0186\u0185\u0001\u0000\u0000\u0000\u0186\u0187\u0001\u0000\u0000\u0000"
                    + "\u0187\u0188\u0001\u0000\u0000\u0000\u0188\u0189\u0005\u001c\u0000\u0000"
                    + "\u0189\u018a\u0003$\u0012\u0000\u018aK\u0001\u0000\u0000\u0000\u018b\u018e"
                    + "\u0003N\'\u0000\u018c\u018e\u0003 \u0010\u0000\u018d\u018b\u0001\u0000"
                    + "\u0000\u0000\u018d\u018c\u0001\u0000\u0000\u0000\u018eM\u0001\u0000\u0000"
                    + "\u0000\u018f\u0194\u00032\u0019\u0000\u0190\u0191\u0005\"\u0000\u0000"
                    + "\u0191\u0193\u00032\u0019\u0000\u0192\u0190\u0001\u0000\u0000\u0000\u0193"
                    + "\u0196\u0001\u0000\u0000\u0000\u0194\u0192\u0001\u0000\u0000\u0000\u0194"
                    + "\u0195\u0001\u0000\u0000\u0000\u0195O\u0001\u0000\u0000\u0000\u0196\u0194"
                    + "\u0001\u0000\u0000\u0000\u0197\u0198\u0005\u0002\u0000\u0000\u0198\u0199"
                    + "\u0005!\u0000\u0000\u0199Q\u0001\u0000\u0000\u0000\u019a\u019b\u0005\u0005"
                    + "\u0000\u0000\u019b\u019c\u0005!\u0000\u0000\u019cS\u0001\u0000\u0000\u0000"
                    + "\u019d\u019e\u0005\r\u0000\u0000\u019e\u019f\u0005\u001a\u0000\u0000\u019f"
                    + "\u01a0\u0005!\u0000\u0000\u01a0U\u0001\u0000\u0000\u0000\u01a1\u01a2\u0005"
                    + "\n\u0000\u0000\u01a2\u01a3\u0005!\u0000\u0000\u01a3W\u0001\u0000\u0000"
                    + "\u0000\u01a4\u01a8\u0003\\.\u0000\u01a5\u01a7\u0003Z-\u0000\u01a6\u01a5"
                    + "\u0001\u0000\u0000\u0000\u01a7\u01aa\u0001\u0000\u0000\u0000\u01a8\u01a6"
                    + "\u0001\u0000\u0000\u0000\u01a8\u01a9\u0001\u0000\u0000\u0000\u01a9Y\u0001"
                    + "\u0000\u0000\u0000\u01aa\u01a8\u0001\u0000\u0000\u0000\u01ab\u01ac\u0003"
                    + "b1\u0000\u01ac[\u0001\u0000\u0000\u0000\u01ad\u01b5\u0003\u0000\u0000"
                    + "\u0000\u01ae\u01af\u0005\u001b\u0000\u0000\u01af\u01b0\u0003h4\u0000\u01b0"
                    + "\u01b1\u0005\u001c\u0000\u0000\u01b1\u01b5\u0001\u0000\u0000\u0000\u01b2"
                    + "\u01b5\u0003d2\u0000\u01b3\u01b5\u0005\u001a\u0000\u0000\u01b4\u01ad\u0001"
                    + "\u0000\u0000\u0000\u01b4\u01ae\u0001\u0000\u0000\u0000\u01b4\u01b2\u0001"
                    + "\u0000\u0000\u0000\u01b4\u01b3\u0001\u0000\u0000\u0000\u01b5]\u0001\u0000"
                    + "\u0000\u0000\u01b6\u01bd\u0003\u0000\u0000\u0000\u01b7\u01b8\u0005\u001b"
                    + "\u0000\u0000\u01b8\u01b9\u0003h4\u0000\u01b9\u01ba\u0005\u001c\u0000\u0000"
                    + "\u01ba\u01bd\u0001\u0000\u0000\u0000\u01bb\u01bd\u0003d2\u0000\u01bc\u01b6"
                    + "\u0001\u0000\u0000\u0000\u01bc\u01b7\u0001\u0000\u0000\u0000\u01bc\u01bb"
                    + "\u0001\u0000\u0000\u0000\u01bd_\u0001\u0000\u0000\u0000\u01be\u01bf\u0005"
                    + "\u001a\u0000\u0000\u01bf\u01c1\u0005\u001b\u0000\u0000\u01c0\u01c2\u0003"
                    + "f3\u0000\u01c1\u01c0\u0001\u0000\u0000\u0000\u01c1\u01c2\u0001\u0000\u0000"
                    + "\u0000\u01c2\u01c3\u0001\u0000\u0000\u0000\u01c3\u01ce\u0005\u001c\u0000"
                    + "\u0000\u01c4\u01c5\u0003X,\u0000\u01c5\u01c6\u0005#\u0000\u0000\u01c6"
                    + "\u01c7\u0005\u001a\u0000\u0000\u01c7\u01c9\u0005\u001b\u0000\u0000\u01c8"
                    + "\u01ca\u0003f3\u0000\u01c9\u01c8\u0001\u0000\u0000\u0000\u01c9\u01ca\u0001"
                    + "\u0000\u0000\u0000\u01ca\u01cb\u0001\u0000\u0000\u0000\u01cb\u01cc\u0005"
                    + "\u001c\u0000\u0000\u01cc\u01ce\u0001\u0000\u0000\u0000\u01cd\u01be\u0001"
                    + "\u0000\u0000\u0000\u01cd\u01c4\u0001\u0000\u0000\u0000\u01cea\u0001\u0000"
                    + "\u0000\u0000\u01cf\u01d0\u0005#\u0000\u0000\u01d0\u01d1\u0005\u001a\u0000"
                    + "\u0000\u01d1\u01d3\u0005\u001b\u0000\u0000\u01d2\u01d4\u0003f3\u0000\u01d3"
                    + "\u01d2\u0001\u0000\u0000\u0000\u01d3\u01d4\u0001\u0000\u0000\u0000\u01d4"
                    + "\u01d5\u0001\u0000\u0000\u0000\u01d5\u01d6\u0005\u001c\u0000\u0000\u01d6"
                    + "c\u0001\u0000\u0000\u0000\u01d7\u01d8\u0005\u001a\u0000\u0000\u01d8\u01da"
                    + "\u0005\u001b\u0000\u0000\u01d9\u01db\u0003f3\u0000\u01da\u01d9\u0001\u0000"
                    + "\u0000\u0000\u01da\u01db\u0001\u0000\u0000\u0000\u01db\u01dc\u0001\u0000"
                    + "\u0000\u0000\u01dc\u01dd\u0005\u001c\u0000\u0000\u01dde\u0001\u0000\u0000"
                    + "\u0000\u01de\u01e3\u0003h4\u0000\u01df\u01e0\u0005\"\u0000\u0000\u01e0"
                    + "\u01e2\u0003h4\u0000\u01e1\u01df\u0001\u0000\u0000\u0000\u01e2\u01e5\u0001"
                    + "\u0000\u0000\u0000\u01e3\u01e1\u0001\u0000\u0000\u0000\u01e3\u01e4\u0001"
                    + "\u0000\u0000\u0000\u01e4g\u0001\u0000\u0000\u0000\u01e5\u01e3\u0001\u0000"
                    + "\u0000\u0000\u01e6\u01e9\u0003p8\u0000\u01e7\u01e9\u0003j5\u0000\u01e8"
                    + "\u01e6\u0001\u0000\u0000\u0000\u01e8\u01e7\u0001\u0000\u0000\u0000\u01e9"
                    + "i\u0001\u0000\u0000\u0000\u01ea\u01eb\u0003l6\u0000\u01eb\u01ec\u0003"
                    + "n7\u0000\u01ec\u01ed\u0003h4\u0000\u01edk\u0001\u0000\u0000\u0000\u01ee"
                    + "\u01ef\u0005\u001a\u0000\u0000\u01efm\u0001\u0000\u0000\u0000\u01f0\u01f1"
                    + "\u0007\u0002\u0000\u0000\u01f1o\u0001\u0000\u0000\u0000\u01f2\u01fa\u0003"
                    + "r9\u0000\u01f3\u01f4\u0003r9\u0000\u01f4\u01f5\u0005(\u0000\u0000\u01f5"
                    + "\u01f6\u0003h4\u0000\u01f6\u01f7\u0005)\u0000\u0000\u01f7\u01f8\u0003"
                    + "p8\u0000\u01f8\u01fa\u0001\u0000\u0000\u0000\u01f9\u01f2\u0001\u0000\u0000"
                    + "\u0000\u01f9\u01f3\u0001\u0000\u0000\u0000\u01faq\u0001\u0000\u0000\u0000"
                    + "\u01fb\u01fc\u00069\uffff\uffff\u0000\u01fc\u01fd\u0003t:\u0000\u01fd"
                    + "\u0203\u0001\u0000\u0000\u0000\u01fe\u01ff\n\u0001\u0000\u0000\u01ff\u0200"
                    + "\u0005/\u0000\u0000\u0200\u0202\u0003t:\u0000\u0201\u01fe\u0001\u0000"
                    + "\u0000\u0000\u0202\u0205\u0001\u0000\u0000\u0000\u0203\u0201\u0001\u0000"
                    + "\u0000\u0000\u0203\u0204\u0001\u0000\u0000\u0000\u0204s\u0001\u0000\u0000"
                    + "\u0000\u0205\u0203\u0001\u0000\u0000\u0000\u0206\u0207\u0006:\uffff\uffff"
                    + "\u0000\u0207\u0208\u0003v;\u0000\u0208\u020e\u0001\u0000\u0000\u0000\u0209"
                    + "\u020a\n\u0001\u0000\u0000\u020a\u020b\u0005.\u0000\u0000\u020b\u020d"
                    + "\u0003v;\u0000\u020c\u0209\u0001\u0000\u0000\u0000\u020d\u0210\u0001\u0000"
                    + "\u0000\u0000\u020e\u020c\u0001\u0000\u0000\u0000\u020e\u020f\u0001\u0000"
                    + "\u0000\u0000\u020fu\u0001\u0000\u0000\u0000\u0210\u020e\u0001\u0000\u0000"
                    + "\u0000\u0211\u0212\u0006;\uffff\uffff\u0000\u0212\u0213\u0003x<\u0000"
                    + "\u0213\u021c\u0001\u0000\u0000\u0000\u0214\u0215\n\u0002\u0000\u0000\u0215"
                    + "\u0216\u0005*\u0000\u0000\u0216\u021b\u0003x<\u0000\u0217\u0218\n\u0001"
                    + "\u0000\u0000\u0218\u0219\u0005-\u0000\u0000\u0219\u021b\u0003x<\u0000"
                    + "\u021a\u0214\u0001\u0000\u0000\u0000\u021a\u0217\u0001\u0000\u0000\u0000"
                    + "\u021b\u021e\u0001\u0000\u0000\u0000\u021c\u021a\u0001\u0000\u0000\u0000"
                    + "\u021c\u021d\u0001\u0000\u0000\u0000\u021dw\u0001\u0000\u0000\u0000\u021e"
                    + "\u021c\u0001\u0000\u0000\u0000\u021f\u0220\u0006<\uffff\uffff\u0000\u0220"
                    + "\u0221\u0003z=\u0000\u0221\u0230\u0001\u0000\u0000\u0000\u0222\u0223\n"
                    + "\u0004\u0000\u0000\u0223\u0224\u0005&\u0000\u0000\u0224\u022f\u0003z="
                    + "\u0000\u0225\u0226\n\u0003\u0000\u0000\u0226\u0227\u0005%\u0000\u0000"
                    + "\u0227\u022f\u0003z=\u0000\u0228\u0229\n\u0002\u0000\u0000\u0229\u022a"
                    + "\u0005+\u0000\u0000\u022a\u022f\u0003z=\u0000\u022b\u022c\n\u0001\u0000"
                    + "\u0000\u022c\u022d\u0005,\u0000\u0000\u022d\u022f\u0003z=\u0000\u022e"
                    + "\u0222\u0001\u0000\u0000\u0000\u022e\u0225\u0001\u0000\u0000\u0000\u022e"
                    + "\u0228\u0001\u0000\u0000\u0000\u022e\u022b\u0001\u0000\u0000\u0000\u022f"
                    + "\u0232\u0001\u0000\u0000\u0000\u0230\u022e\u0001\u0000\u0000\u0000\u0230"
                    + "\u0231\u0001\u0000\u0000\u0000\u0231y\u0001\u0000\u0000\u0000\u0232\u0230"
                    + "\u0001\u0000\u0000\u0000\u0233\u0234\u0006=\uffff\uffff\u0000\u0234\u0235"
                    + "\u0003|>\u0000\u0235\u023e\u0001\u0000\u0000\u0000\u0236\u0237\n\u0002"
                    + "\u0000\u0000\u0237\u0238\u00052\u0000\u0000\u0238\u023d\u0003|>\u0000"
                    + "\u0239\u023a\n\u0001\u0000\u0000\u023a\u023b\u00053\u0000\u0000\u023b"
                    + "\u023d\u0003|>\u0000\u023c\u0236\u0001\u0000\u0000\u0000\u023c\u0239\u0001"
                    + "\u0000\u0000\u0000\u023d\u0240\u0001\u0000\u0000\u0000\u023e\u023c\u0001"
                    + "\u0000\u0000\u0000\u023e\u023f\u0001\u0000\u0000\u0000\u023f{\u0001\u0000"
                    + "\u0000\u0000\u0240\u023e\u0001\u0000\u0000\u0000\u0241\u0242\u0006>\uffff"
                    + "\uffff\u0000\u0242\u0243\u0003~?\u0000\u0243\u024f\u0001\u0000\u0000\u0000"
                    + "\u0244\u0245\n\u0003\u0000\u0000\u0245\u0246\u00054\u0000\u0000\u0246"
                    + "\u024e\u0003~?\u0000\u0247\u0248\n\u0002\u0000\u0000\u0248\u0249\u0005"
                    + "5\u0000\u0000\u0249\u024e\u0003~?\u0000\u024a\u024b\n\u0001\u0000\u0000"
                    + "\u024b\u024c\u00056\u0000\u0000\u024c\u024e\u0003~?\u0000\u024d\u0244"
                    + "\u0001\u0000\u0000\u0000\u024d\u0247\u0001\u0000\u0000\u0000\u024d\u024a"
                    + "\u0001\u0000\u0000\u0000\u024e\u0251\u0001\u0000\u0000\u0000\u024f\u024d"
                    + "\u0001\u0000\u0000\u0000\u024f\u0250\u0001\u0000\u0000\u0000\u0250}\u0001"
                    + "\u0000\u0000\u0000\u0251\u024f\u0001\u0000\u0000\u0000\u0252\u025a\u0003"
                    + "\u0080@\u0000\u0253\u025a\u0003\u0082A\u0000\u0254\u0255\u00052\u0000"
                    + "\u0000\u0255\u025a\u0003~?\u0000\u0256\u0257\u00053\u0000\u0000\u0257"
                    + "\u025a\u0003~?\u0000\u0258\u025a\u0003\u0084B\u0000\u0259\u0252\u0001"
                    + "\u0000\u0000\u0000\u0259\u0253\u0001\u0000\u0000\u0000\u0259\u0254\u0001"
                    + "\u0000\u0000\u0000\u0259\u0256\u0001\u0000\u0000\u0000\u0259\u0258\u0001"
                    + "\u0000\u0000\u0000\u025a\u007f\u0001\u0000\u0000\u0000\u025b\u025c\u0005"
                    + "0\u0000\u0000\u025c\u025d\u0003~?\u0000\u025d\u0081\u0001\u0000\u0000"
                    + "\u0000\u025e\u025f\u00051\u0000\u0000\u025f\u0260\u0003~?\u0000\u0260"
                    + "\u0083\u0001\u0000\u0000\u0000\u0261\u0266\u0003\u0086C\u0000\u0262\u0263"
                    + "\u0005\'\u0000\u0000\u0263\u0266\u0003~?\u0000\u0264\u0266\u0003\u008c"
                    + "F\u0000\u0265\u0261\u0001\u0000\u0000\u0000\u0265\u0262\u0001\u0000\u0000"
                    + "\u0000\u0265\u0264\u0001\u0000\u0000\u0000\u0266\u0085\u0001\u0000\u0000"
                    + "\u0000\u0267\u026a\u0003X,\u0000\u0268\u026a\u0005\u001a\u0000\u0000\u0269"
                    + "\u0267\u0001\u0000\u0000\u0000\u0269\u0268\u0001\u0000\u0000\u0000\u026a"
                    + "\u026e\u0001\u0000\u0000\u0000\u026b\u026d\u0007\u0003\u0000\u0000\u026c"
                    + "\u026b\u0001\u0000\u0000\u0000\u026d\u0270\u0001\u0000\u0000\u0000\u026e"
                    + "\u026c\u0001\u0000\u0000\u0000\u026e\u026f\u0001\u0000\u0000\u0000\u026f"
                    + "\u0087\u0001\u0000\u0000\u0000\u0270\u026e\u0001\u0000\u0000\u0000\u0271"
                    + "\u0272\u0003\u0086C\u0000\u0272\u0273\u00050\u0000\u0000\u0273\u0089\u0001"
                    + "\u0000\u0000\u0000\u0274\u0275\u0003\u0086C\u0000\u0275\u0276\u00051\u0000"
                    + "\u0000\u0276\u008b\u0001\u0000\u0000\u0000\u0277\u0278\u0005\u001b\u0000"
                    + "\u0000\u0278\u0279\u0003\u0002\u0001\u0000\u0279\u027a\u0005\u001c\u0000"
                    + "\u0000\u027a\u027b\u0003~?\u0000\u027b\u0282\u0001\u0000\u0000\u0000\u027c"
                    + "\u027d\u0005\u001b\u0000\u0000\u027d\u027e\u0003\u0006\u0003\u0000\u027e"
                    + "\u027f\u0005\u001c\u0000\u0000\u027f\u0280\u0003\u0084B\u0000\u0280\u0282"
                    + "\u0001\u0000\u0000\u0000\u0281\u0277\u0001\u0000\u0000\u0000\u0281\u027c"
                    + "\u0001\u0000\u0000\u0000\u0282\u008d\u0001\u0000\u0000\u00008\u0093\u009a"
                    + "\u00a1\u00ac\u00b4\u00bc\u00c2\u00c6\u00ca\u00cf\u00d6\u00dd\u00e2\u00e8"
                    + "\u00f3\u00fa\u0105\u011b\u013d\u0143\u014b\u0155\u016e\u0172\u0176\u017e"
                    + "\u0182\u0186\u018d\u0194\u01a8\u01b4\u01bc\u01c1\u01c9\u01cd\u01d3\u01da"
                    + "\u01e3\u01e8\u01f9\u0203\u020e\u021a\u021c\u022e\u0230\u023c\u023e\u024d"
                    + "\u024f\u0259\u0265\u0269\u026e\u0281";

    public static final ATN _ATN =
            new ATNDeserializer().deserialize(IkalaScriptParser._serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[IkalaScriptParser._ATN.getNumberOfDecisions()];
        for (int i = 0; i < IkalaScriptParser._ATN.getNumberOfDecisions(); i++) {
            IkalaScriptParser._decisionToDFA[i] =
                    new DFA(IkalaScriptParser._ATN.getDecisionState(i), i);
        }
    }

    private static String[] makeLiteralNames() {
        return new String[] {
            null,
            "'boolean'",
            "'break'",
            "'case'",
            "'char'",
            "'continue'",
            "'default'",
            "'do'",
            "'double'",
            "'else'",
            "'exit'",
            "'final'",
            "'for'",
            "'goto'",
            "'if'",
            "'int'",
            "'string'",
            "'switch'",
            "'void'",
            "'while'",
            null,
            null,
            null,
            null,
            null,
            "'null'",
            null,
            "'('",
            "')'",
            "'{'",
            "'}'",
            "'['",
            "']'",
            "';'",
            "','",
            "'.'",
            "'='",
            "'>'",
            "'<'",
            "'!'",
            "'?'",
            "':'",
            "'=='",
            "'<='",
            "'>='",
            "'!='",
            "'&&'",
            "'||'",
            "'++'",
            "'--'",
            "'+'",
            "'-'",
            "'*'",
            "'/'",
            "'%'",
            "'+='",
            "'-='",
            "'*='",
            "'/='",
            "'%='"
        };
    }

    private static String[] makeRuleNames() {
        return new String[] {
            "literal",
            "primitiveType",
            "numericType",
            "referenceType",
            "classOrInterfaceType",
            "arrayType",
            "dims",
            "variableDeclaratorList",
            "variableDeclarator",
            "variableDeclaratorId",
            "type",
            "compilationUnit",
            "block",
            "blockStatements",
            "blockStatement",
            "localVariableDeclarationStatement",
            "localVariableDeclaration",
            "statement",
            "statementNoShortIf",
            "statementWithoutTrailingSubstatement",
            "label",
            "labeledStatement",
            "labeledStatementNoShortIf",
            "emptyStatement",
            "expressionStatement",
            "statementExpression",
            "ifThenStatement",
            "ifThenElseStatement",
            "ifThenElseStatementNoShortIf",
            "switchStatement",
            "switchBlock",
            "switchBlockStatementGroup",
            "switchLabel",
            "whileStatement",
            "whileStatementNoShortIf",
            "doStatement",
            "forStatement",
            "forStatementNoShortIf",
            "forInit",
            "statementExpressionList",
            "breakStatement",
            "continueStatement",
            "gotoStatement",
            "exitStatement",
            "primary",
            "primary_extension",
            "primary_LHS",
            "primary_LHS_access",
            "methodInvocation",
            "methodInvocation_extension",
            "methodInvocation_LHS",
            "argumentList",
            "expression",
            "assignment",
            "leftHandSide",
            "assignmentOperator",
            "conditionalExpression",
            "conditionalOrExpression",
            "conditionalAndExpression",
            "equalityExpression",
            "relationalExpression",
            "additiveExpression",
            "multiplicativeExpression",
            "unaryExpression",
            "preIncrementExpression",
            "preDecrementExpression",
            "unaryExpressionNotPlusMinus",
            "postfixExpression",
            "postIncrementExpression",
            "postDecrementExpression",
            "castExpression"
        };
    }

    private static String[] makeSymbolicNames() {
        return new String[] {
            null,
            "BOOLEAN",
            "BREAK",
            "CASE",
            "CHAR",
            "CONTINUE",
            "DEFAULT",
            "DO",
            "DOUBLE",
            "ELSE",
            "EXIT",
            "FINAL",
            "FOR",
            "GOTO",
            "IF",
            "INT",
            "STRING",
            "SWITCH",
            "VOID",
            "WHILE",
            "IntegerLiteral",
            "FloatingPointLiteral",
            "BooleanLiteral",
            "CharacterLiteral",
            "StringLiteral",
            "NullLiteral",
            "Identifier",
            "LPAREN",
            "RPAREN",
            "LBRACE",
            "RBRACE",
            "LBRACK",
            "RBRACK",
            "SEMICOLON",
            "COMMA",
            "DOT",
            "ASSIGN",
            "GT",
            "LT",
            "NOT",
            "QUESTION",
            "COLON",
            "EQUAL",
            "LTE",
            "GTE",
            "NOTEQUAL",
            "AND",
            "OR",
            "INC",
            "DEC",
            "ADD",
            "SUB",
            "MUL",
            "DIV",
            "MOD",
            "ADD_ASSIGN",
            "SUB_ASSIGN",
            "MUL_ASSIGN",
            "DIV_ASSIGN",
            "MOD_ASSIGN",
            "WS",
            "COMMENT",
            "LINE_COMMENT"
        };
    }

    public IkalaScriptParser(TokenStream input) {
        super(input);
        _interp =
                new ParserATNSimulator(
                        this,
                        IkalaScriptParser._ATN,
                        IkalaScriptParser._decisionToDFA,
                        IkalaScriptParser._sharedContextCache);
    }

    public final AdditiveExpressionContext additiveExpression() throws RecognitionException {
        return this.additiveExpression(0);
    }

    private AdditiveExpressionContext additiveExpression(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, _parentState);
        AdditiveExpressionContext _prevctx = _localctx;
        int _startState = 122;
        this.enterRecursionRule(_localctx, 122, IkalaScriptParser.RULE_additiveExpression, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(564);
                    this.multiplicativeExpression(0);
                }
                _ctx.stop = _input.LT(-1);
                setState(574);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 48, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            setState(572);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 47, _ctx)) {
                                case 1:
                                    {
                                        _localctx =
                                                new AdditiveExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_additiveExpression);
                                        setState(566);
                                        if (!(precpred(_ctx, 2))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 2)");
                                        }
                                        setState(567);
                                        match(IkalaScriptParser.ADD);
                                        setState(568);
                                        this.multiplicativeExpression(0);
                                    }
                                    break;
                                case 2:
                                    {
                                        _localctx =
                                                new AdditiveExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_additiveExpression);
                                        setState(569);
                                        if (!(precpred(_ctx, 1))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 1)");
                                        }
                                        setState(570);
                                        match(IkalaScriptParser.SUB);
                                        setState(571);
                                        this.multiplicativeExpression(0);
                                    }
                                    break;
                            }
                        }
                    }
                    setState(576);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 48, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    private boolean additiveExpression_sempred(AdditiveExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 8:
                return precpred(_ctx, 2);
            case 9:
                return precpred(_ctx, 1);
        }
        return true;
    }

    public final ArgumentListContext argumentList() throws RecognitionException {
        ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
        enterRule(_localctx, 102, IkalaScriptParser.RULE_argumentList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(478);
                expression();
                setState(483);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == IkalaScriptParser.COMMA) {
                    {
                        {
                            setState(479);
                            match(IkalaScriptParser.COMMA);
                            setState(480);
                            expression();
                        }
                    }
                    setState(485);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ArrayTypeContext arrayType() throws RecognitionException {
        ArrayTypeContext _localctx = new ArrayTypeContext(_ctx, getState());
        enterRule(_localctx, 10, IkalaScriptParser.RULE_arrayType);
        try {
            setState(172);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(164);
                        primitiveType();
                        setState(165);
                        dims();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(167);
                        classOrInterfaceType();
                        setState(168);
                        dims();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(170);
                        match(IkalaScriptParser.Identifier);
                        setState(171);
                        dims();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AssignmentContext assignment() throws RecognitionException {
        AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
        enterRule(_localctx, 106, IkalaScriptParser.RULE_assignment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(490);
                leftHandSide();
                setState(491);
                assignmentOperator();
                setState(492);
                expression();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AssignmentOperatorContext assignmentOperator() throws RecognitionException {
        AssignmentOperatorContext _localctx = new AssignmentOperatorContext(_ctx, getState());
        enterRule(_localctx, 110, IkalaScriptParser.RULE_assignmentOperator);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(496);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 1116892776307359744L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) {
                        matchedEOF = true;
                    }
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final BlockContext block() throws RecognitionException {
        BlockContext _localctx = new BlockContext(_ctx, getState());
        enterRule(_localctx, 24, IkalaScriptParser.RULE_block);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(212);
                match(IkalaScriptParser.LBRACE);
                setState(214);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 844434325110198L) != 0)) {
                    {
                        setState(213);
                        blockStatements();
                    }
                }

                setState(216);
                match(IkalaScriptParser.RBRACE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final BlockStatementContext blockStatement() throws RecognitionException {
        BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
        enterRule(_localctx, 28, IkalaScriptParser.RULE_blockStatement);
        try {
            setState(226);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 12, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(223);
                        localVariableDeclarationStatement();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(224);
                        statement();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(225);
                        label();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final BlockStatementsContext blockStatements() throws RecognitionException {
        BlockStatementsContext _localctx = new BlockStatementsContext(_ctx, getState());
        enterRule(_localctx, 26, IkalaScriptParser.RULE_blockStatements);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(219);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(218);
                            blockStatement();
                        }
                    }
                    setState(221);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 844434325110198L) != 0));
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final BreakStatementContext breakStatement() throws RecognitionException {
        BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
        enterRule(_localctx, 80, IkalaScriptParser.RULE_breakStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(407);
                match(IkalaScriptParser.BREAK);
                setState(408);
                match(IkalaScriptParser.SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final CastExpressionContext castExpression() throws RecognitionException {
        CastExpressionContext _localctx = new CastExpressionContext(_ctx, getState());
        enterRule(_localctx, 140, IkalaScriptParser.RULE_castExpression);
        try {
            setState(641);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 55, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(631);
                        match(IkalaScriptParser.LPAREN);
                        setState(632);
                        primitiveType();
                        setState(633);
                        match(IkalaScriptParser.RPAREN);
                        setState(634);
                        unaryExpression();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(636);
                        match(IkalaScriptParser.LPAREN);
                        setState(637);
                        referenceType();
                        setState(638);
                        match(IkalaScriptParser.RPAREN);
                        setState(639);
                        unaryExpressionNotPlusMinus();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ClassOrInterfaceTypeContext classOrInterfaceType() throws RecognitionException {
        ClassOrInterfaceTypeContext _localctx = new ClassOrInterfaceTypeContext(_ctx, getState());
        enterRule(_localctx, 8, IkalaScriptParser.RULE_classOrInterfaceType);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(156);
                match(IkalaScriptParser.Identifier);
                setState(161);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == IkalaScriptParser.DOT) {
                    {
                        {
                            setState(157);
                            match(IkalaScriptParser.DOT);
                            setState(158);
                            match(IkalaScriptParser.Identifier);
                        }
                    }
                    setState(163);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final CompilationUnitContext compilationUnit() throws RecognitionException {
        CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
        enterRule(_localctx, 22, IkalaScriptParser.RULE_compilationUnit);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(207);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 844434325110198L) != 0)) {
                    {
                        {
                            setState(204);
                            blockStatement();
                        }
                    }
                    setState(209);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(210);
                match(Recognizer.EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ConditionalAndExpressionContext conditionalAndExpression()
            throws RecognitionException {
        return this.conditionalAndExpression(0);
    }

    private ConditionalAndExpressionContext conditionalAndExpression(int _p)
            throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ConditionalAndExpressionContext _localctx =
                new ConditionalAndExpressionContext(_ctx, _parentState);
        ConditionalAndExpressionContext _prevctx = _localctx;
        int _startState = 116;
        this.enterRecursionRule(
                _localctx, 116, IkalaScriptParser.RULE_conditionalAndExpression, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(519);
                    this.equalityExpression(0);
                }
                _ctx.stop = _input.LT(-1);
                setState(526);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 42, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            {
                                _localctx =
                                        new ConditionalAndExpressionContext(
                                                _parentctx, _parentState);
                                pushNewRecursionContext(
                                        _localctx,
                                        _startState,
                                        IkalaScriptParser.RULE_conditionalAndExpression);
                                setState(521);
                                if (!(precpred(_ctx, 1))) {
                                    throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                }
                                setState(522);
                                match(IkalaScriptParser.AND);
                                setState(523);
                                this.equalityExpression(0);
                            }
                        }
                    }
                    setState(528);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 42, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    private boolean conditionalAndExpression_sempred(
            ConditionalAndExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 1:
                return precpred(_ctx, 1);
        }
        return true;
    }

    public final ConditionalExpressionContext conditionalExpression() throws RecognitionException {
        ConditionalExpressionContext _localctx = new ConditionalExpressionContext(_ctx, getState());
        enterRule(_localctx, 112, IkalaScriptParser.RULE_conditionalExpression);
        try {
            setState(505);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 40, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(498);
                        this.conditionalOrExpression(0);
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(499);
                        this.conditionalOrExpression(0);
                        setState(500);
                        match(IkalaScriptParser.QUESTION);
                        setState(501);
                        expression();
                        setState(502);
                        match(IkalaScriptParser.COLON);
                        setState(503);
                        conditionalExpression();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ConditionalOrExpressionContext conditionalOrExpression()
            throws RecognitionException {
        return this.conditionalOrExpression(0);
    }

    private ConditionalOrExpressionContext conditionalOrExpression(int _p)
            throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ConditionalOrExpressionContext _localctx =
                new ConditionalOrExpressionContext(_ctx, _parentState);
        ConditionalOrExpressionContext _prevctx = _localctx;
        int _startState = 114;
        this.enterRecursionRule(_localctx, 114, IkalaScriptParser.RULE_conditionalOrExpression, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(508);
                    this.conditionalAndExpression(0);
                }
                _ctx.stop = _input.LT(-1);
                setState(515);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 41, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            {
                                _localctx =
                                        new ConditionalOrExpressionContext(
                                                _parentctx, _parentState);
                                pushNewRecursionContext(
                                        _localctx,
                                        _startState,
                                        IkalaScriptParser.RULE_conditionalOrExpression);
                                setState(510);
                                if (!(precpred(_ctx, 1))) {
                                    throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                }
                                setState(511);
                                match(IkalaScriptParser.OR);
                                setState(512);
                                this.conditionalAndExpression(0);
                            }
                        }
                    }
                    setState(517);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 41, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    private boolean conditionalOrExpression_sempred(
            ConditionalOrExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 1);
        }
        return true;
    }

    public final ContinueStatementContext continueStatement() throws RecognitionException {
        ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
        enterRule(_localctx, 82, IkalaScriptParser.RULE_continueStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(410);
                match(IkalaScriptParser.CONTINUE);
                setState(411);
                match(IkalaScriptParser.SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DimsContext dims() throws RecognitionException {
        DimsContext _localctx = new DimsContext(_ctx, getState());
        enterRule(_localctx, 12, IkalaScriptParser.RULE_dims);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(174);
                match(IkalaScriptParser.LBRACK);
                setState(175);
                match(IkalaScriptParser.RBRACK);
                setState(180);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == IkalaScriptParser.LBRACK) {
                    {
                        {
                            setState(176);
                            match(IkalaScriptParser.LBRACK);
                            setState(177);
                            match(IkalaScriptParser.RBRACK);
                        }
                    }
                    setState(182);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DoStatementContext doStatement() throws RecognitionException {
        DoStatementContext _localctx = new DoStatementContext(_ctx, getState());
        enterRule(_localctx, 70, IkalaScriptParser.RULE_doStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(355);
                match(IkalaScriptParser.DO);
                setState(356);
                statement();
                setState(357);
                match(IkalaScriptParser.WHILE);
                setState(358);
                match(IkalaScriptParser.LPAREN);
                setState(359);
                expression();
                setState(360);
                match(IkalaScriptParser.RPAREN);
                setState(361);
                match(IkalaScriptParser.SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final EmptyStatementContext emptyStatement() throws RecognitionException {
        EmptyStatementContext _localctx = new EmptyStatementContext(_ctx, getState());
        enterRule(_localctx, 46, IkalaScriptParser.RULE_emptyStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(272);
                match(IkalaScriptParser.SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final EqualityExpressionContext equalityExpression() throws RecognitionException {
        return this.equalityExpression(0);
    }

    private EqualityExpressionContext equalityExpression(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        EqualityExpressionContext _localctx = new EqualityExpressionContext(_ctx, _parentState);
        EqualityExpressionContext _prevctx = _localctx;
        int _startState = 118;
        this.enterRecursionRule(_localctx, 118, IkalaScriptParser.RULE_equalityExpression, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(530);
                    this.relationalExpression(0);
                }
                _ctx.stop = _input.LT(-1);
                setState(540);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 44, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            setState(538);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 43, _ctx)) {
                                case 1:
                                    {
                                        _localctx =
                                                new EqualityExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_equalityExpression);
                                        setState(532);
                                        if (!(precpred(_ctx, 2))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 2)");
                                        }
                                        setState(533);
                                        match(IkalaScriptParser.EQUAL);
                                        setState(534);
                                        this.relationalExpression(0);
                                    }
                                    break;
                                case 2:
                                    {
                                        _localctx =
                                                new EqualityExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_equalityExpression);
                                        setState(535);
                                        if (!(precpred(_ctx, 1))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 1)");
                                        }
                                        setState(536);
                                        match(IkalaScriptParser.NOTEQUAL);
                                        setState(537);
                                        this.relationalExpression(0);
                                    }
                                    break;
                            }
                        }
                    }
                    setState(542);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 44, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    private boolean equalityExpression_sempred(EqualityExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 2:
                return precpred(_ctx, 2);
            case 3:
                return precpred(_ctx, 1);
        }
        return true;
    }

    public final ExitStatementContext exitStatement() throws RecognitionException {
        ExitStatementContext _localctx = new ExitStatementContext(_ctx, getState());
        enterRule(_localctx, 86, IkalaScriptParser.RULE_exitStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(417);
                match(IkalaScriptParser.EXIT);
                setState(418);
                match(IkalaScriptParser.SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ExpressionContext expression() throws RecognitionException {
        ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
        enterRule(_localctx, 104, IkalaScriptParser.RULE_expression);
        try {
            setState(488);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 39, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(486);
                        conditionalExpression();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(487);
                        assignment();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ExpressionStatementContext expressionStatement() throws RecognitionException {
        ExpressionStatementContext _localctx = new ExpressionStatementContext(_ctx, getState());
        enterRule(_localctx, 48, IkalaScriptParser.RULE_expressionStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(274);
                statementExpression();
                setState(275);
                match(IkalaScriptParser.SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ForInitContext forInit() throws RecognitionException {
        ForInitContext _localctx = new ForInitContext(_ctx, getState());
        enterRule(_localctx, 76, IkalaScriptParser.RULE_forInit);
        try {
            setState(397);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 28, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(395);
                        statementExpressionList();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(396);
                        localVariableDeclaration();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ForStatementContext forStatement() throws RecognitionException {
        ForStatementContext _localctx = new ForStatementContext(_ctx, getState());
        enterRule(_localctx, 72, IkalaScriptParser.RULE_forStatement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(363);
                match(IkalaScriptParser.FOR);
                setState(364);
                match(IkalaScriptParser.LPAREN);
                setState(366);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 844425197619474L) != 0)) {
                    {
                        setState(365);
                        forInit();
                    }
                }

                setState(368);
                match(IkalaScriptParser.SEMICOLON);
                setState(370);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4222674673860608L) != 0)) {
                    {
                        setState(369);
                        expression();
                    }
                }

                setState(372);
                match(IkalaScriptParser.SEMICOLON);
                setState(374);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 844425197518848L) != 0)) {
                    {
                        setState(373);
                        statementExpressionList();
                    }
                }

                setState(376);
                match(IkalaScriptParser.RPAREN);
                setState(377);
                statement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ForStatementNoShortIfContext forStatementNoShortIf() throws RecognitionException {
        ForStatementNoShortIfContext _localctx = new ForStatementNoShortIfContext(_ctx, getState());
        enterRule(_localctx, 74, IkalaScriptParser.RULE_forStatementNoShortIf);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(379);
                match(IkalaScriptParser.FOR);
                setState(380);
                match(IkalaScriptParser.LPAREN);
                setState(382);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 844425197619474L) != 0)) {
                    {
                        setState(381);
                        forInit();
                    }
                }

                setState(384);
                match(IkalaScriptParser.SEMICOLON);
                setState(386);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4222674673860608L) != 0)) {
                    {
                        setState(385);
                        expression();
                    }
                }

                setState(388);
                match(IkalaScriptParser.SEMICOLON);
                setState(390);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 844425197518848L) != 0)) {
                    {
                        setState(389);
                        statementExpressionList();
                    }
                }

                setState(392);
                match(IkalaScriptParser.RPAREN);
                setState(393);
                statementNoShortIf();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
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

    public final GotoStatementContext gotoStatement() throws RecognitionException {
        GotoStatementContext _localctx = new GotoStatementContext(_ctx, getState());
        enterRule(_localctx, 84, IkalaScriptParser.RULE_gotoStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(413);
                match(IkalaScriptParser.GOTO);
                setState(414);
                match(IkalaScriptParser.Identifier);
                setState(415);
                match(IkalaScriptParser.SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IfThenElseStatementContext ifThenElseStatement() throws RecognitionException {
        IfThenElseStatementContext _localctx = new IfThenElseStatementContext(_ctx, getState());
        enterRule(_localctx, 54, IkalaScriptParser.RULE_ifThenElseStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(291);
                match(IkalaScriptParser.IF);
                setState(292);
                match(IkalaScriptParser.LPAREN);
                setState(293);
                expression();
                setState(294);
                match(IkalaScriptParser.RPAREN);
                setState(295);
                statementNoShortIf();
                setState(296);
                match(IkalaScriptParser.ELSE);
                setState(297);
                statement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IfThenElseStatementNoShortIfContext ifThenElseStatementNoShortIf()
            throws RecognitionException {
        IfThenElseStatementNoShortIfContext _localctx =
                new IfThenElseStatementNoShortIfContext(_ctx, getState());
        enterRule(_localctx, 56, IkalaScriptParser.RULE_ifThenElseStatementNoShortIf);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(299);
                match(IkalaScriptParser.IF);
                setState(300);
                match(IkalaScriptParser.LPAREN);
                setState(301);
                expression();
                setState(302);
                match(IkalaScriptParser.RPAREN);
                setState(303);
                statementNoShortIf();
                setState(304);
                match(IkalaScriptParser.ELSE);
                setState(305);
                statementNoShortIf();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IfThenStatementContext ifThenStatement() throws RecognitionException {
        IfThenStatementContext _localctx = new IfThenStatementContext(_ctx, getState());
        enterRule(_localctx, 52, IkalaScriptParser.RULE_ifThenStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(285);
                match(IkalaScriptParser.IF);
                setState(286);
                match(IkalaScriptParser.LPAREN);
                setState(287);
                expression();
                setState(288);
                match(IkalaScriptParser.RPAREN);
                setState(289);
                statement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LabelContext label() throws RecognitionException {
        LabelContext _localctx = new LabelContext(_ctx, getState());
        enterRule(_localctx, 40, IkalaScriptParser.RULE_label);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(263);
                match(IkalaScriptParser.Identifier);
                setState(264);
                match(IkalaScriptParser.COLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LabeledStatementContext labeledStatement() throws RecognitionException {
        LabeledStatementContext _localctx = new LabeledStatementContext(_ctx, getState());
        enterRule(_localctx, 42, IkalaScriptParser.RULE_labeledStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(266);
                label();
                setState(267);
                statement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LabeledStatementNoShortIfContext labeledStatementNoShortIf()
            throws RecognitionException {
        LabeledStatementNoShortIfContext _localctx =
                new LabeledStatementNoShortIfContext(_ctx, getState());
        enterRule(_localctx, 44, IkalaScriptParser.RULE_labeledStatementNoShortIf);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(269);
                label();
                setState(270);
                statementNoShortIf();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LeftHandSideContext leftHandSide() throws RecognitionException {
        LeftHandSideContext _localctx = new LeftHandSideContext(_ctx, getState());
        enterRule(_localctx, 108, IkalaScriptParser.RULE_leftHandSide);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(494);
                match(IkalaScriptParser.Identifier);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LiteralContext literal() throws RecognitionException {
        LiteralContext _localctx = new LiteralContext(_ctx, getState());
        enterRule(_localctx, 0, IkalaScriptParser.RULE_literal);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(142);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 66060288L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) {
                        matchedEOF = true;
                    }
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LocalVariableDeclarationContext localVariableDeclaration()
            throws RecognitionException {
        LocalVariableDeclarationContext _localctx =
                new LocalVariableDeclarationContext(_ctx, getState());
        enterRule(_localctx, 32, IkalaScriptParser.RULE_localVariableDeclaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(232);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == IkalaScriptParser.FINAL) {
                    {
                        setState(231);
                        match(IkalaScriptParser.FINAL);
                    }
                }

                setState(234);
                type();
                setState(235);
                variableDeclaratorList();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LocalVariableDeclarationStatementContext localVariableDeclarationStatement()
            throws RecognitionException {
        LocalVariableDeclarationStatementContext _localctx =
                new LocalVariableDeclarationStatementContext(_ctx, getState());
        enterRule(_localctx, 30, IkalaScriptParser.RULE_localVariableDeclarationStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(228);
                localVariableDeclaration();
                setState(229);
                match(IkalaScriptParser.SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final MethodInvocationContext methodInvocation() throws RecognitionException {
        MethodInvocationContext _localctx = new MethodInvocationContext(_ctx, getState());
        enterRule(_localctx, 96, IkalaScriptParser.RULE_methodInvocation);
        int _la;
        try {
            setState(461);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 35, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(446);
                        match(IkalaScriptParser.Identifier);
                        setState(447);
                        match(IkalaScriptParser.LPAREN);
                        setState(449);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4222674673860608L) != 0)) {
                            {
                                setState(448);
                                argumentList();
                            }
                        }

                        setState(451);
                        match(IkalaScriptParser.RPAREN);
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(452);
                        primary();
                        setState(453);
                        match(IkalaScriptParser.DOT);
                        setState(454);
                        match(IkalaScriptParser.Identifier);
                        setState(455);
                        match(IkalaScriptParser.LPAREN);
                        setState(457);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4222674673860608L) != 0)) {
                            {
                                setState(456);
                                argumentList();
                            }
                        }

                        setState(459);
                        match(IkalaScriptParser.RPAREN);
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final MethodInvocation_extensionContext methodInvocation_extension()
            throws RecognitionException {
        MethodInvocation_extensionContext _localctx =
                new MethodInvocation_extensionContext(_ctx, getState());
        enterRule(_localctx, 98, IkalaScriptParser.RULE_methodInvocation_extension);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(463);
                match(IkalaScriptParser.DOT);
                setState(464);
                match(IkalaScriptParser.Identifier);
                setState(465);
                match(IkalaScriptParser.LPAREN);
                setState(467);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4222674673860608L) != 0)) {
                    {
                        setState(466);
                        argumentList();
                    }
                }

                setState(469);
                match(IkalaScriptParser.RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final MethodInvocation_LHSContext methodInvocation_LHS() throws RecognitionException {
        MethodInvocation_LHSContext _localctx = new MethodInvocation_LHSContext(_ctx, getState());
        enterRule(_localctx, 100, IkalaScriptParser.RULE_methodInvocation_LHS);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(471);
                match(IkalaScriptParser.Identifier);
                setState(472);
                match(IkalaScriptParser.LPAREN);
                setState(474);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4222674673860608L) != 0)) {
                    {
                        setState(473);
                        argumentList();
                    }
                }

                setState(476);
                match(IkalaScriptParser.RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final MultiplicativeExpressionContext multiplicativeExpression()
            throws RecognitionException {
        return this.multiplicativeExpression(0);
    }

    private MultiplicativeExpressionContext multiplicativeExpression(int _p)
            throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        MultiplicativeExpressionContext _localctx =
                new MultiplicativeExpressionContext(_ctx, _parentState);
        MultiplicativeExpressionContext _prevctx = _localctx;
        int _startState = 124;
        this.enterRecursionRule(
                _localctx, 124, IkalaScriptParser.RULE_multiplicativeExpression, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(578);
                    unaryExpression();
                }
                _ctx.stop = _input.LT(-1);
                setState(591);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 50, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            setState(589);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 49, _ctx)) {
                                case 1:
                                    {
                                        _localctx =
                                                new MultiplicativeExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_multiplicativeExpression);
                                        setState(580);
                                        if (!(precpred(_ctx, 3))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 3)");
                                        }
                                        setState(581);
                                        match(IkalaScriptParser.MUL);
                                        setState(582);
                                        unaryExpression();
                                    }
                                    break;
                                case 2:
                                    {
                                        _localctx =
                                                new MultiplicativeExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_multiplicativeExpression);
                                        setState(583);
                                        if (!(precpred(_ctx, 2))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 2)");
                                        }
                                        setState(584);
                                        match(IkalaScriptParser.DIV);
                                        setState(585);
                                        unaryExpression();
                                    }
                                    break;
                                case 3:
                                    {
                                        _localctx =
                                                new MultiplicativeExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_multiplicativeExpression);
                                        setState(586);
                                        if (!(precpred(_ctx, 1))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 1)");
                                        }
                                        setState(587);
                                        match(IkalaScriptParser.MOD);
                                        setState(588);
                                        unaryExpression();
                                    }
                                    break;
                            }
                        }
                    }
                    setState(593);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 50, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    private boolean multiplicativeExpression_sempred(
            MultiplicativeExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 10:
                return precpred(_ctx, 3);
            case 11:
                return precpred(_ctx, 2);
            case 12:
                return precpred(_ctx, 1);
        }
        return true;
    }

    public final NumericTypeContext numericType() throws RecognitionException {
        NumericTypeContext _localctx = new NumericTypeContext(_ctx, getState());
        enterRule(_localctx, 4, IkalaScriptParser.RULE_numericType);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(149);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 33040L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) {
                        matchedEOF = true;
                    }
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final PostDecrementExpressionContext postDecrementExpression()
            throws RecognitionException {
        PostDecrementExpressionContext _localctx =
                new PostDecrementExpressionContext(_ctx, getState());
        enterRule(_localctx, 138, IkalaScriptParser.RULE_postDecrementExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(628);
                postfixExpression();
                setState(629);
                match(IkalaScriptParser.DEC);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final PostfixExpressionContext postfixExpression() throws RecognitionException {
        PostfixExpressionContext _localctx = new PostfixExpressionContext(_ctx, getState());
        enterRule(_localctx, 134, IkalaScriptParser.RULE_postfixExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(617);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 53, _ctx)) {
                    case 1:
                        {
                            setState(615);
                            primary();
                        }
                        break;
                    case 2:
                        {
                            setState(616);
                            match(IkalaScriptParser.Identifier);
                        }
                        break;
                }
                setState(622);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 54, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(619);
                                _la = _input.LA(1);
                                if (!(_la == IkalaScriptParser.INC
                                        || _la == IkalaScriptParser.DEC)) {
                                    _errHandler.recoverInline(this);
                                } else {
                                    if (_input.LA(1) == Token.EOF) {
                                        matchedEOF = true;
                                    }
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                            }
                        }
                    }
                    setState(624);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 54, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final PostIncrementExpressionContext postIncrementExpression()
            throws RecognitionException {
        PostIncrementExpressionContext _localctx =
                new PostIncrementExpressionContext(_ctx, getState());
        enterRule(_localctx, 136, IkalaScriptParser.RULE_postIncrementExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(625);
                postfixExpression();
                setState(626);
                match(IkalaScriptParser.INC);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final PreDecrementExpressionContext preDecrementExpression()
            throws RecognitionException {
        PreDecrementExpressionContext _localctx =
                new PreDecrementExpressionContext(_ctx, getState());
        enterRule(_localctx, 130, IkalaScriptParser.RULE_preDecrementExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(606);
                match(IkalaScriptParser.DEC);
                setState(607);
                unaryExpression();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final PreIncrementExpressionContext preIncrementExpression()
            throws RecognitionException {
        PreIncrementExpressionContext _localctx =
                new PreIncrementExpressionContext(_ctx, getState());
        enterRule(_localctx, 128, IkalaScriptParser.RULE_preIncrementExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(603);
                match(IkalaScriptParser.INC);
                setState(604);
                unaryExpression();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final PrimaryContext primary() throws RecognitionException {
        PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
        enterRule(_localctx, 88, IkalaScriptParser.RULE_primary);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(420);
                    primary_LHS();
                }
                setState(424);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 30, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(421);
                                primary_extension();
                            }
                        }
                    }
                    setState(426);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 30, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Primary_extensionContext primary_extension() throws RecognitionException {
        Primary_extensionContext _localctx = new Primary_extensionContext(_ctx, getState());
        enterRule(_localctx, 90, IkalaScriptParser.RULE_primary_extension);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(427);
                methodInvocation_extension();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Primary_LHSContext primary_LHS() throws RecognitionException {
        Primary_LHSContext _localctx = new Primary_LHSContext(_ctx, getState());
        enterRule(_localctx, 92, IkalaScriptParser.RULE_primary_LHS);
        try {
            setState(436);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 31, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(429);
                        literal();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(430);
                        match(IkalaScriptParser.LPAREN);
                        setState(431);
                        expression();
                        setState(432);
                        match(IkalaScriptParser.RPAREN);
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(434);
                        methodInvocation_LHS();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(435);
                        match(IkalaScriptParser.Identifier);
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Primary_LHS_accessContext primary_LHS_access() throws RecognitionException {
        Primary_LHS_accessContext _localctx = new Primary_LHS_accessContext(_ctx, getState());
        enterRule(_localctx, 94, IkalaScriptParser.RULE_primary_LHS_access);
        try {
            setState(444);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IntegerLiteral:
                case FloatingPointLiteral:
                case BooleanLiteral:
                case CharacterLiteral:
                case StringLiteral:
                case NullLiteral:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(438);
                        literal();
                    }
                    break;
                case LPAREN:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(439);
                        match(IkalaScriptParser.LPAREN);
                        setState(440);
                        expression();
                        setState(441);
                        match(IkalaScriptParser.RPAREN);
                    }
                    break;
                case Identifier:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(443);
                        methodInvocation_LHS();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final PrimitiveTypeContext primitiveType() throws RecognitionException {
        PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
        enterRule(_localctx, 2, IkalaScriptParser.RULE_primitiveType);
        try {
            setState(147);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case CHAR:
                case DOUBLE:
                case INT:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(144);
                        numericType();
                    }
                    break;
                case BOOLEAN:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(145);
                        match(IkalaScriptParser.BOOLEAN);
                    }
                    break;
                case STRING:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(146);
                        match(IkalaScriptParser.STRING);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ReferenceTypeContext referenceType() throws RecognitionException {
        ReferenceTypeContext _localctx = new ReferenceTypeContext(_ctx, getState());
        enterRule(_localctx, 6, IkalaScriptParser.RULE_referenceType);
        try {
            setState(154);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 1, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(151);
                        classOrInterfaceType();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(152);
                        match(IkalaScriptParser.Identifier);
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(153);
                        arrayType();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final RelationalExpressionContext relationalExpression() throws RecognitionException {
        return this.relationalExpression(0);
    }

    private RelationalExpressionContext relationalExpression(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        RelationalExpressionContext _localctx = new RelationalExpressionContext(_ctx, _parentState);
        RelationalExpressionContext _prevctx = _localctx;
        int _startState = 120;
        this.enterRecursionRule(_localctx, 120, IkalaScriptParser.RULE_relationalExpression, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(544);
                    this.additiveExpression(0);
                }
                _ctx.stop = _input.LT(-1);
                setState(560);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 46, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            setState(558);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 45, _ctx)) {
                                case 1:
                                    {
                                        _localctx =
                                                new RelationalExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_relationalExpression);
                                        setState(546);
                                        if (!(precpred(_ctx, 4))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 4)");
                                        }
                                        setState(547);
                                        match(IkalaScriptParser.LT);
                                        setState(548);
                                        this.additiveExpression(0);
                                    }
                                    break;
                                case 2:
                                    {
                                        _localctx =
                                                new RelationalExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_relationalExpression);
                                        setState(549);
                                        if (!(precpred(_ctx, 3))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 3)");
                                        }
                                        setState(550);
                                        match(IkalaScriptParser.GT);
                                        setState(551);
                                        this.additiveExpression(0);
                                    }
                                    break;
                                case 3:
                                    {
                                        _localctx =
                                                new RelationalExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_relationalExpression);
                                        setState(552);
                                        if (!(precpred(_ctx, 2))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 2)");
                                        }
                                        setState(553);
                                        match(IkalaScriptParser.LTE);
                                        setState(554);
                                        this.additiveExpression(0);
                                    }
                                    break;
                                case 4:
                                    {
                                        _localctx =
                                                new RelationalExpressionContext(
                                                        _parentctx, _parentState);
                                        pushNewRecursionContext(
                                                _localctx,
                                                _startState,
                                                IkalaScriptParser.RULE_relationalExpression);
                                        setState(555);
                                        if (!(precpred(_ctx, 1))) {
                                            throw new FailedPredicateException(
                                                    this, "precpred(_ctx, 1)");
                                        }
                                        setState(556);
                                        match(IkalaScriptParser.GTE);
                                        setState(557);
                                        this.additiveExpression(0);
                                    }
                                    break;
                            }
                        }
                    }
                    setState(562);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 46, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    private boolean relationalExpression_sempred(
            RelationalExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 4:
                return precpred(_ctx, 4);
            case 5:
                return precpred(_ctx, 3);
            case 6:
                return precpred(_ctx, 2);
            case 7:
                return precpred(_ctx, 1);
        }
        return true;
    }

    @Override
    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 57:
                return conditionalOrExpression_sempred(
                        (ConditionalOrExpressionContext) _localctx, predIndex);
            case 58:
                return conditionalAndExpression_sempred(
                        (ConditionalAndExpressionContext) _localctx, predIndex);
            case 59:
                return equalityExpression_sempred((EqualityExpressionContext) _localctx, predIndex);
            case 60:
                return relationalExpression_sempred(
                        (RelationalExpressionContext) _localctx, predIndex);
            case 61:
                return additiveExpression_sempred((AdditiveExpressionContext) _localctx, predIndex);
            case 62:
                return multiplicativeExpression_sempred(
                        (MultiplicativeExpressionContext) _localctx, predIndex);
        }
        return true;
    }

    public final StatementContext statement() throws RecognitionException {
        StatementContext _localctx = new StatementContext(_ctx, getState());
        enterRule(_localctx, 34, IkalaScriptParser.RULE_statement);
        try {
            setState(243);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(237);
                        statementWithoutTrailingSubstatement();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(238);
                        labeledStatement();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(239);
                        ifThenStatement();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(240);
                        ifThenElseStatement();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(241);
                        whileStatement();
                    }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                    {
                        setState(242);
                        forStatement();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final StatementExpressionContext statementExpression() throws RecognitionException {
        StatementExpressionContext _localctx = new StatementExpressionContext(_ctx, getState());
        enterRule(_localctx, 50, IkalaScriptParser.RULE_statementExpression);
        try {
            setState(283);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(277);
                        assignment();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(278);
                        preIncrementExpression();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(279);
                        preDecrementExpression();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(280);
                        postIncrementExpression();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(281);
                        postDecrementExpression();
                    }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                    {
                        setState(282);
                        methodInvocation();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final StatementExpressionListContext statementExpressionList()
            throws RecognitionException {
        StatementExpressionListContext _localctx =
                new StatementExpressionListContext(_ctx, getState());
        enterRule(_localctx, 78, IkalaScriptParser.RULE_statementExpressionList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(399);
                statementExpression();
                setState(404);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == IkalaScriptParser.COMMA) {
                    {
                        {
                            setState(400);
                            match(IkalaScriptParser.COMMA);
                            setState(401);
                            statementExpression();
                        }
                    }
                    setState(406);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final StatementNoShortIfContext statementNoShortIf() throws RecognitionException {
        StatementNoShortIfContext _localctx = new StatementNoShortIfContext(_ctx, getState());
        enterRule(_localctx, 36, IkalaScriptParser.RULE_statementNoShortIf);
        try {
            setState(250);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 15, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(245);
                        statementWithoutTrailingSubstatement();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(246);
                        labeledStatementNoShortIf();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(247);
                        ifThenElseStatementNoShortIf();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(248);
                        whileStatementNoShortIf();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(249);
                        forStatementNoShortIf();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final StatementWithoutTrailingSubstatementContext statementWithoutTrailingSubstatement()
            throws RecognitionException {
        StatementWithoutTrailingSubstatementContext _localctx =
                new StatementWithoutTrailingSubstatementContext(_ctx, getState());
        enterRule(_localctx, 38, IkalaScriptParser.RULE_statementWithoutTrailingSubstatement);
        try {
            setState(261);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case LBRACE:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(252);
                        block();
                    }
                    break;
                case SEMICOLON:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(253);
                        emptyStatement();
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
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(254);
                        expressionStatement();
                    }
                    break;
                case SWITCH:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(255);
                        switchStatement();
                    }
                    break;
                case DO:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(256);
                        doStatement();
                    }
                    break;
                case BREAK:
                    enterOuterAlt(_localctx, 6);
                    {
                        setState(257);
                        breakStatement();
                    }
                    break;
                case CONTINUE:
                    enterOuterAlt(_localctx, 7);
                    {
                        setState(258);
                        continueStatement();
                    }
                    break;
                case GOTO:
                    enterOuterAlt(_localctx, 8);
                    {
                        setState(259);
                        gotoStatement();
                    }
                    break;
                case EXIT:
                    enterOuterAlt(_localctx, 9);
                    {
                        setState(260);
                        exitStatement();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SwitchBlockContext switchBlock() throws RecognitionException {
        SwitchBlockContext _localctx = new SwitchBlockContext(_ctx, getState());
        enterRule(_localctx, 60, IkalaScriptParser.RULE_switchBlock);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(313);
                match(IkalaScriptParser.LBRACE);
                setState(317);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 18, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(314);
                                switchBlockStatementGroup();
                            }
                        }
                    }
                    setState(319);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 18, _ctx);
                }
                setState(323);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == IkalaScriptParser.CASE || _la == IkalaScriptParser.DEFAULT) {
                    {
                        {
                            setState(320);
                            switchLabel();
                        }
                    }
                    setState(325);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(326);
                match(IkalaScriptParser.RBRACE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SwitchBlockStatementGroupContext switchBlockStatementGroup()
            throws RecognitionException {
        SwitchBlockStatementGroupContext _localctx =
                new SwitchBlockStatementGroupContext(_ctx, getState());
        enterRule(_localctx, 62, IkalaScriptParser.RULE_switchBlockStatementGroup);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(329);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(328);
                            switchLabel();
                        }
                    }
                    setState(331);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (_la == IkalaScriptParser.CASE || _la == IkalaScriptParser.DEFAULT);
                setState(333);
                blockStatements();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SwitchLabelContext switchLabel() throws RecognitionException {
        SwitchLabelContext _localctx = new SwitchLabelContext(_ctx, getState());
        enterRule(_localctx, 64, IkalaScriptParser.RULE_switchLabel);
        try {
            setState(341);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case CASE:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(335);
                        match(IkalaScriptParser.CASE);
                        setState(336);
                        expression();
                        setState(337);
                        match(IkalaScriptParser.COLON);
                    }
                    break;
                case DEFAULT:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(339);
                        match(IkalaScriptParser.DEFAULT);
                        setState(340);
                        match(IkalaScriptParser.COLON);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SwitchStatementContext switchStatement() throws RecognitionException {
        SwitchStatementContext _localctx = new SwitchStatementContext(_ctx, getState());
        enterRule(_localctx, 58, IkalaScriptParser.RULE_switchStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(307);
                match(IkalaScriptParser.SWITCH);
                setState(308);
                match(IkalaScriptParser.LPAREN);
                setState(309);
                expression();
                setState(310);
                match(IkalaScriptParser.RPAREN);
                setState(311);
                switchBlock();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final TypeContext type() throws RecognitionException {
        TypeContext _localctx = new TypeContext(_ctx, getState());
        enterRule(_localctx, 20, IkalaScriptParser.RULE_type);
        try {
            setState(202);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(200);
                        primitiveType();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(201);
                        referenceType();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final UnaryExpressionContext unaryExpression() throws RecognitionException {
        UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
        enterRule(_localctx, 126, IkalaScriptParser.RULE_unaryExpression);
        try {
            setState(601);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case INC:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(594);
                        preIncrementExpression();
                    }
                    break;
                case DEC:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(595);
                        preDecrementExpression();
                    }
                    break;
                case ADD:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(596);
                        match(IkalaScriptParser.ADD);
                        setState(597);
                        unaryExpression();
                    }
                    break;
                case SUB:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(598);
                        match(IkalaScriptParser.SUB);
                        setState(599);
                        unaryExpression();
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
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(600);
                        unaryExpressionNotPlusMinus();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final UnaryExpressionNotPlusMinusContext unaryExpressionNotPlusMinus()
            throws RecognitionException {
        UnaryExpressionNotPlusMinusContext _localctx =
                new UnaryExpressionNotPlusMinusContext(_ctx, getState());
        enterRule(_localctx, 132, IkalaScriptParser.RULE_unaryExpressionNotPlusMinus);
        try {
            setState(613);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 52, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(609);
                        postfixExpression();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(610);
                        match(IkalaScriptParser.NOT);
                        setState(611);
                        unaryExpression();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(612);
                        castExpression();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final VariableDeclaratorContext variableDeclarator() throws RecognitionException {
        VariableDeclaratorContext _localctx = new VariableDeclaratorContext(_ctx, getState());
        enterRule(_localctx, 16, IkalaScriptParser.RULE_variableDeclarator);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(191);
                variableDeclaratorId();
                setState(194);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == IkalaScriptParser.ASSIGN) {
                    {
                        setState(192);
                        match(IkalaScriptParser.ASSIGN);
                        setState(193);
                        expression();
                    }
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final VariableDeclaratorIdContext variableDeclaratorId() throws RecognitionException {
        VariableDeclaratorIdContext _localctx = new VariableDeclaratorIdContext(_ctx, getState());
        enterRule(_localctx, 18, IkalaScriptParser.RULE_variableDeclaratorId);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(196);
                match(IkalaScriptParser.Identifier);
                setState(198);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == IkalaScriptParser.LBRACK) {
                    {
                        setState(197);
                        dims();
                    }
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final VariableDeclaratorListContext variableDeclaratorList()
            throws RecognitionException {
        VariableDeclaratorListContext _localctx =
                new VariableDeclaratorListContext(_ctx, getState());
        enterRule(_localctx, 14, IkalaScriptParser.RULE_variableDeclaratorList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(183);
                variableDeclarator();
                setState(188);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == IkalaScriptParser.COMMA) {
                    {
                        {
                            setState(184);
                            match(IkalaScriptParser.COMMA);
                            setState(185);
                            variableDeclarator();
                        }
                    }
                    setState(190);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final WhileStatementContext whileStatement() throws RecognitionException {
        WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
        enterRule(_localctx, 66, IkalaScriptParser.RULE_whileStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(343);
                match(IkalaScriptParser.WHILE);
                setState(344);
                match(IkalaScriptParser.LPAREN);
                setState(345);
                expression();
                setState(346);
                match(IkalaScriptParser.RPAREN);
                setState(347);
                statement();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final WhileStatementNoShortIfContext whileStatementNoShortIf()
            throws RecognitionException {
        WhileStatementNoShortIfContext _localctx =
                new WhileStatementNoShortIfContext(_ctx, getState());
        enterRule(_localctx, 68, IkalaScriptParser.RULE_whileStatementNoShortIf);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(349);
                match(IkalaScriptParser.WHILE);
                setState(350);
                match(IkalaScriptParser.LPAREN);
                setState(351);
                expression();
                setState(352);
                match(IkalaScriptParser.RPAREN);
                setState(353);
                statementNoShortIf();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }
}
