grammar Proyecto;

program : classDeclaration+;

classDeclaration : 'class' 'Program' '{' (declaration)* '}';

declaration : structDeclaration
  | varDeclaration
  | methodDeclaration
  ;

varDeclaration : varType ID ';' 
  | varType ID '[' NUM ']' ';'
  ;

structDeclaration : 'struct' ID '{' (varDeclaration)* '}';

varType : 'int'
  | 'char'
  | 'boolean'
  | 'struct' ID
  | structDeclaration
  | 'void'
  ;

methodDeclaration : methodType ID '(' (parameter)? (',' parameter)* ')' block;

methodType : 'int'
  | 'char'
  | 'boolean'
  | 'void'
  ;

parameter : parameterType ID
  | parameterType ID '[' ']'
  ;

parameterType : 'int'
  | 'char'
  | 'boolean'
  ;

block : '{' (varDeclaration)* (statement)* '}';

statement : 'if' '(' expression ')' block ('else' block)?
  | 'while' '(' expression ')' block
  | 'return' (expression)? ';'
  | methodCall ';'
  | block
  | location '=' expression
  | (expression)? ';'
  ;

location : (ID | ID '[' expression ']') ('.' location)?;

expression : location
  | methodCall
  | literal
  |<assoc=right> expression op expression
  | '-' expression
  | '!' expression
  | '(' expression ')'
  ;

methodCall : ID '(' (expression)? (',' expression)* ')';

op : arithOp
  | relOp
  | eqOp
  | condOp
  ;

arithOp : '+'
  | '-'
  | '*'
  | '/'
  | '%'
  ;

relOp : '<'
  | '>'
  | '<='
  | '>='
  ;

eqOp : '=='
  | '!='
  ;

condOp : '&&'
  | '||'
  ;

literal : NUM
  | BOOL
  | CHAR
  ;

//intLiteral : NUM;

CHAR : '\''.*?'\''; //arreglar esto

BOOL : 'true'| 'false';

ID : LETTER (LETTER|DIGIT)*;

NUM : DIGIT (DIGIT)*;

ALL : .*?;

LETTER : [a-zA-Z];

DIGIT : [0-9]+;

WHITE : [ \t\n]+ -> skip ; // skip spaces, tabs, newlines

LINE_COMMENT : '//' .*? '\n' -> skip ;