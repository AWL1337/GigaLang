grammar MyLanguage;

@header {
    package org.itmo.antlr;
}

// Пример грамматики
prog:   statement+ ;
statement: 'print' STRING ;
STRING: '"' ~["]* '"' ;
WS: [ \t\r\n]+ -> skip ;