grammar GigaLang;

// Лексер
VAR: 'var';
ARR: 'arr';
NEW: 'new';

ID: [a-zA-Z_][a-zA-Z_0-9]*;
INT: [0-9]+;

PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
POW: '**';
ASSIGN: '=';

LPAREN: '(';
RPAREN: ')';
SLPAREN: '[';
SRPAREN: ']';

SEMI: ';';
COM: ',';

WS: [ \t\r\n]+ -> skip;

// Парсер
program: statement+;

statement:
    variableDeclaration
    | arrayDeclaration
    | presetArrayDeclaration
    | variableAssignation
    ;

variableDeclaration: VAR ID ASSIGN expression;

variableAssignation: ID ASSIGN expression;

arrayDeclaration: ARR ID ASSIGN NEW SLPAREN expression SRPAREN;

presetArrayDeclaration: ARR ID ASSIGN SLPAREN list SRPAREN;

list: INT (COM INT)* #ListInt;

expression:
    expression POW expression           # PowExpression
    | expression (MULT | DIV) expression # MulDivExpression
    | expression (PLUS | MINUS) expression # AddSubExpression
    | LPAREN expression RPAREN          # ParenExpression
    | INT                               # IntLiteral
    | ID                                # Variable
    ;