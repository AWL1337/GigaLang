grammar MyLanguage;

@header {
    package org.itmo.antlr;
}

prog: statement+ ;
statement: printStatement
         | varDeclaration
         | assignment
         | expressionStatement
         | ifStatement
         | forStatement
         | funcDeclaration
         ;

printStatement: 'print' expr ';' ;
varDeclaration: 'var' ID arrayType? '=' expr ';' ;
assignment: ID ('[' expr ']')? '=' expr ';' ;
expressionStatement: expr ';' ;
ifStatement: 'if' '(' expr ')' '{' statement+ '}' ;
forStatement: 'for' varDeclaration expr ';' expr '{' statement+ '}' ;
funcDeclaration: 'func' ID '(' params? ')' returnType? '{' statement+ '}' ;
params: param (',' param)* ;
param: ID ':' typeName ;
returnType: ':' typeName ;
typeName: 'int' | 'bool' | 'string' | 'void' ;
arrayType: '[]' ;
expr: baseExpr (('+' | '-' | '*' | '/') baseExpr)* ;
baseExpr: atom (('+' | '-' | '*' | '/') atom)* ;

atom: STRING
    | ID ('[' expr ']')?
    | INT
    | '(' expr ')'
    | arrayCreation
    ;

arrayCreation: '[' (expr (',' expr)*)? ']' ;
methodCall: baseExpr '.' ID '(' expr? ')' ;
methodCallExpr: methodCall | baseExpr;

STRING: '"' (~["\\] | '\\' .)* '"' ;
INT: [0-9]+ ;
ID: [a-zA-Z_][a-zA-Z_0-9]* ;
WS: [ \t\r\n]+ -> skip ;
COMMENT: '//' ~[\r\n]* -> skip ;
