grammar GigaLang;

// Лексер
VAR: 'var';
ARR: 'arr';
NEW: 'new';
IF: 'if';
ELSE: 'else';

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

LINE: [\n]+;
WS: [ \t]+ -> skip;

// Парсер
program: statement+;

statement:
    variableAssignation
    | arrayAssignation
    | variableDeclaration
    | arrayDeclaration
    | presetArrayDeclaration
    | ifStatement
    | ifElseStatement
    ;

variableDeclaration: VAR ID ASSIGN expression LINE;

variableAssignation: ID ASSIGN expression LINE;

arrayDeclaration: ARR ID ASSIGN NEW SLPAREN expression SRPAREN LINE;

presetArrayDeclaration: ARR ID ASSIGN SLPAREN list SRPAREN LINE;

arrayAssignation: ID SLPAREN expression SRPAREN ASSIGN expression LINE;

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

ifElseStatement: IF LPAREN booleanExpression RPAREN LBRACE statement+ RBRACE ELSE LBRACE statement+ RBRACE;

list: INT (COM INT)* #ListInt;

expression:
    expression MOD expression             # ModExpression
    | expression POW expression           # PowExpression
    | expression (MULT | DIV) expression # MulDivExpression
    | expression (PLUS | MINUS) expression # AddSubExpression
    | LPAREN expression RPAREN          # ParenExpression
    | INT                               # IntLiteral
    | ID SLPAREN expression SRPAREN            # ReadArray
    | ID                                # Variable
    ;