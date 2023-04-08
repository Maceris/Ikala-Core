lexer grammar IkalaScriptLexer;

// Keywords

BOOLEAN : 'boolean';
BREAK : 'break';
CASE : 'case';
CHAR : 'char';
CONTINUE : 'continue';
DEFAULT : 'default';
DO : 'do';
DOUBLE : 'double';
ELSE : 'else';
EXIT : 'exit';
FINAL : 'final';
FOR : 'for';
GOTO : 'goto';
IF : 'if';
INT : 'int';
STRING : 'string';
SWITCH : 'switch';
VOID : 'void';
WHILE : 'while';

// Integer Literals

IntegerLiteral
	: [0-9]+
	;

FloatingPointLiteral
	: [0-9]+ '.' [0-9]* ([Ee][-+]?[0-9]+)?
	;

BooleanLiteral
	: 'true'
	| 'false'
	;

CharacterLiteral
	: '\'' ( ~['\r\n] ) '\'' 
	;

StringLiteral
	: '"' ( ~[\r\n\\"] )* '"' 
	;

NullLiteral
	: 'null'
	;

Identifier
	: [a-zA-Z][a-zA-Z_0-9]*
	;

// Separators

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMICOLON : ';';
COMMA : ',';
DOT : '.';

// Operators

ASSIGN : '=';
GT : '>';
LT : '<';
NOT : '!';
QUESTION : '?';
COLON : ':';
EQUAL : '==';
LTE : '<=';
GTE : '>=';
NOTEQUAL : '!=';
AND : '&&';
OR : '||';
INC : '++';
DEC : '--';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
MOD : '%';

ADD_ASSIGN : '+=';
SUB_ASSIGN : '-=';
MUL_ASSIGN : '*=';
DIV_ASSIGN : '/=';
MOD_ASSIGN : '%=';

// Whitespace and comments

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;
