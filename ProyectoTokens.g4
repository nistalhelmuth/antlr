lexer grammar ProyectoTokens;

CHAR : '\''.*?'\''; //arreglar esto

// CHAR ASCII (32-126) \\ \` \t \n

BOOL : 'true'| 'false';

ID : LETTER (LETTER|DIGIT)*;

NUM : DIGIT (DIGIT)*;

ALL : .*?;

LETTER : [a-zA-Z];

DIGIT : [0-9]+;

WHITE : [ \t\n\r]+ -> skip ; // skip spaces, tabs, newlines

LINE_COMMENT : '//' .*? ('\n' | '\r') -> skip ;

//otros

