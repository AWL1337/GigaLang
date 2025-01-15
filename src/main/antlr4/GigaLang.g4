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

WS: [ \t\r\n]+ -> skip;

// Парсер
program: statement+;

statement:
    variableDeclaration
    | arrayDeclaration
    | presetArrayDeclaration
    | variableAssignation
    | relationalExpression
    | booleanExpression
    ;

variableDeclaration: VAR ID ASSIGN expression;

variableAssignation: ID ASSIGN expression;

arrayDeclaration: ARR ID ASSIGN NEW SLPAREN expression SRPAREN;

presetArrayDeclaration: ARR ID ASSIGN SLPAREN list SRPAREN;

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

ifStatement: IF ;

list: INT (COM INT)* #ListInt;

expression:
    expression MOD expression             # ModExpression
    | expression POW expression           # PowExpression
    | expression (MULT | DIV) expression # MulDivExpression
    | expression (PLUS | MINUS) expression # AddSubExpression
    | LPAREN expression RPAREN          # ParenExpression
    | INT                               # IntLiteral
    | ID SLPAREN INT SRPAREN            # ReadArray
    | ID                                # Variable
    ;