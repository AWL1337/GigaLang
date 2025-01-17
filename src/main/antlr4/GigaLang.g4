grammar GigaLang;

// Лексер
VAR: 'var';
ARR: 'arr';
NEW: 'new';
IF: 'if';
ELSE: 'else';
WHILE: 'while';
DEF: 'def';
RETURN: 'return';

ID: [a-zA-Z_][a-zA-Z_0-9]*;
INT: [0-9]+;

PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
MOD: '%';
POW: '**';
ASSIGN: '=';

GT: '>';
GQ: '>=';
LT: '<';
LQ: '<=';
EQ: '==';
NE: '!=';

AND: '&&';
OR: '||';
NOT: '!';

LPAREN: '(';
RPAREN: ')';
SLPAREN: '[';
SRPAREN: ']';
LBRACE: '{';
RBRACE: '}';

SEMI: ';';
COM: ',';

WS: [ \t\n]+ -> skip;

// Парсер
program: statement+;

statement:
    variableAssignation
    | arrayAssignation
    | arrayDeclaration
    | presetArrayDeclaration
    | ifStatement
    | ifElseStatement
    | whileStatement
    | booleanExpression
    | relationalExpression
    | functionDefinition
    | returnStatement
    ;

variableAssignation: ID ASSIGN expression SEMI;

arrayDeclaration: ID ASSIGN NEW SLPAREN expression SRPAREN SEMI;

presetArrayDeclaration: ID ASSIGN SLPAREN expressionList SRPAREN SEMI;

arrayAssignation: ID SLPAREN expression SRPAREN ASSIGN expression SEMI;

booleanExpression:
    booleanExpression OR booleanExpression  # OrExpression
    | booleanExpression AND booleanExpression # AndExpression
    | NOT booleanExpression                 # NotExpression
    | relationalExpression                  # RelationalExpr
    | LPAREN booleanExpression RPAREN       # BooleanParenExpression
    ;

relationalExpression:
    expression GT expression  # GtExpression
    | expression GQ expression # GqExpression
    | expression LT expression  # LtExpression
    | expression LQ expression # LqExpression
    | expression EQ expression  # EqExpression
    | expression NE expression  # NqExpression
    ;

ifStatement: IF LPAREN booleanExpression RPAREN LBRACE statement+ RBRACE;

ifElseStatement: IF LPAREN booleanExpression RPAREN LBRACE thenStatement RBRACE ELSE LBRACE elseStatement RBRACE;
thenStatement: statement+;
elseStatement: statement+;

returnStatement: RETURN expression SEMI;

expressionList: expression (COM expression)*;
idList: (ID (COM ID)*) | ID?;

whileStatement: WHILE LPAREN booleanExpression RPAREN LBRACE statement+ RBRACE;

functionDefinition: DEF ID LPAREN idList RPAREN LBRACE statement+ RBRACE;

expression:
    expression MOD expression             # ModExpression
    | expression POW expression           # PowExpression
    | expression (MULT | DIV) expression # MulDivExpression
    | expression (PLUS | MINUS) expression # AddSubExpression
    | LPAREN expression RPAREN          # ParenExpression
    | INT                               # IntLiteral
    | ID SLPAREN expression SRPAREN     # ReadArray
    | ID                                # Variable
    ;