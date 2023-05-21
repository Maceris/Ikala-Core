parser grammar IkalaScriptParser;

options {
    tokenVocab=IkalaScriptLexer;
}

literal
	:	IntegerLiteral
	|	FloatingPointLiteral
	|	BooleanLiteral
	|	CharacterLiteral
	|	StringLiteral
	|	NullLiteral
	;

// Types, Values, and Variables

primitiveType
	:	numericType
	|	BOOLEAN
	|	STRING
	;

numericType
	:	INT
	|	CHAR
	|	DOUBLE
	;

referenceType
	:	classOrInterfaceType
	|	Identifier
	|	arrayType
	;

classOrInterfaceType
	:	Identifier (DOT Identifier)*
	;

arrayType
	:	primitiveType dims
	|	classOrInterfaceType dims
	|	Identifier dims
	;

dims
	:	LBRACK RBRACK (LBRACK RBRACK)*
	;

variableDeclaratorList
	:	variableDeclarator (COMMA variableDeclarator)*
	;

variableDeclarator
	:	variableDeclaratorId (ASSIGN expression)?
	;

variableDeclaratorId
	:	Identifier dims?
	;

type
	:	primitiveType
	|	referenceType
	;

// Scripts

compilationUnit
	:	blockStatement* EOF
	;

// Blocks and Statements

block
	:	LBRACE blockStatements? RBRACE
	;

blockStatements
	:	blockStatement+
	;

blockStatement
	:	localVariableDeclarationStatement
	|	statement
	|	label
	;

localVariableDeclarationStatement
	:	localVariableDeclaration SEMICOLON
	;

localVariableDeclaration
	:	FINAL? type variableDeclaratorList
	;

statement
	:	statementWithoutTrailingSubstatement
	|	labeledStatement
	|	ifThenStatement
	|	ifThenElseStatement
	|	whileStatement
	|	forStatement
	;

statementNoShortIf
	:	statementWithoutTrailingSubstatement
	|	labeledStatementNoShortIf
	|	ifThenElseStatementNoShortIf
	|	whileStatementNoShortIf
	|	forStatementNoShortIf
	;

statementWithoutTrailingSubstatement
	:	block
	|	emptyStatement
	|	expressionStatement
	|	switchStatement
	|	doStatement
	|	breakStatement
	|	continueStatement
	|	gotoStatement
	|	exitStatement
	;

label
	: Identifier COLON
	;

labeledStatement
	:	label statement
	;

labeledStatementNoShortIf
	:	label statementNoShortIf
	;

emptyStatement
	: SEMICOLON
	;

expressionStatement
	:	statementExpression SEMICOLON 
	;

statementExpression
	:	assignment
	|	preIncrementExpression
	|	preDecrementExpression
	|	postIncrementExpression
	|	postDecrementExpression
	|	methodInvocation
	;

ifThenStatement
	:	IF LPAREN expression RPAREN statement
	;

ifThenElseStatement
	:	IF LPAREN expression RPAREN statementNoShortIf ELSE statement
	;

ifThenElseStatementNoShortIf
	:	IF LPAREN expression RPAREN statementNoShortIf ELSE statementNoShortIf
	;

switchStatement
	:	SWITCH LPAREN expression RPAREN switchBlock
	;

switchBlock
	:	LBRACE switchBlockStatementGroup* switchLabel* RBRACE
	;

switchBlockStatementGroup
	:	switchLabel+ blockStatements
	;

switchLabel
	:	CASE expression COLON
	|	DEFAULT COLON
	;

whileStatement
	:	WHILE LPAREN expression RPAREN statement
	;

whileStatementNoShortIf
	:	WHILE LPAREN expression RPAREN statementNoShortIf
	;

doStatement
	:	DO statement WHILE LPAREN expression RPAREN SEMICOLON
	;

forStatement
	:	FOR LPAREN forInit? SEMICOLON expression? SEMICOLON statementExpressionList? RPAREN statement
	;

forStatementNoShortIf
	:	FOR LPAREN forInit? SEMICOLON expression? SEMICOLON statementExpressionList? RPAREN statementNoShortIf
	;

forInit
	:	statementExpressionList
	|	localVariableDeclaration
	;

statementExpressionList
	:	statementExpression (COMMA statementExpression)*
	;

breakStatement
	:	BREAK SEMICOLON
	;

continueStatement
	:	CONTINUE SEMICOLON
	;

gotoStatement
	:	GOTO Identifier SEMICOLON
	;

exitStatement
	:	EXIT SEMICOLON
	;

// Expressions

primary
	:	(primary_LHS) (primary_extension)*
	;

primary_extension
	:	methodInvocation_extension
	;

primary_LHS
	:	literal
	|	LPAREN expression RPAREN
	|	arrayAccess
	|	methodInvocation_LHS
	|	Identifier
	;

primary_LHS_access
	:	literal
	|	LPAREN expression RPAREN
	|	methodInvocation_LHS
	;

arrayAccess
	:	Identifier (LBRACK expression RBRACK)+
	;

methodInvocation
	:	Identifier LPAREN argumentList? RPAREN
	|	primary DOT Identifier LPAREN argumentList? RPAREN
	;

methodInvocation_extension
	:	DOT Identifier LPAREN argumentList? RPAREN
	;

methodInvocation_LHS
	:	Identifier LPAREN argumentList? RPAREN
	;

argumentList
	:	expression (COMMA expression)*
	;

expression
	:	conditionalExpression
	|	assignment
	;

assignment
	:	leftHandSide assignmentOperator expression
	;

leftHandSide
	:	Identifier
	|	arrayAccess
	;

assignmentOperator
	:	ASSIGN
	|	MUL_ASSIGN
	|	DIV_ASSIGN
	|	MOD_ASSIGN
	|	ADD_ASSIGN
	|	SUB_ASSIGN
	;

conditionalExpression
	:	conditionalOrExpression
	|	conditionalOrExpression QUESTION expression COLON conditionalExpression
	;

conditionalOrExpression
	:	conditionalAndExpression
	|	conditionalOrExpression OR conditionalAndExpression
	;

conditionalAndExpression
	:	equalityExpression
	|	conditionalAndExpression AND equalityExpression
	;

equalityExpression
	:	relationalExpression
	|	equalityExpression EQUAL relationalExpression
	|	equalityExpression NOTEQUAL relationalExpression
	;

relationalExpression
	:	additiveExpression
	|	relationalExpression LT additiveExpression
	|	relationalExpression GT additiveExpression
	|	relationalExpression LTE additiveExpression
	|	relationalExpression GTE additiveExpression
	;

additiveExpression
	:	multiplicativeExpression
	|	additiveExpression ADD multiplicativeExpression
	|	additiveExpression SUB multiplicativeExpression
	;

multiplicativeExpression
	:	unaryExpression
	|	multiplicativeExpression MUL unaryExpression
	|	multiplicativeExpression DIV unaryExpression
	|	multiplicativeExpression MOD unaryExpression
	;

unaryExpression
	:	preIncrementExpression
	|	preDecrementExpression
	|	ADD unaryExpression
	|	SUB unaryExpression
	|	unaryExpressionNotPlusMinus
	;

preIncrementExpression
	:	INC unaryExpression
	;

preDecrementExpression
	:	DEC unaryExpression
	;

unaryExpressionNotPlusMinus
	:	postfixExpression
	|	NOT unaryExpression
	|	castExpression
	;

postfixExpression
	:	(primary | Identifier) (INC | DEC)*
	;

postIncrementExpression
	:	postfixExpression INC
	;

postDecrementExpression
	:	postfixExpression DEC
	;

castExpression
	:	LPAREN primitiveType RPAREN unaryExpression
	|	LPAREN referenceType RPAREN unaryExpressionNotPlusMinus
	;
