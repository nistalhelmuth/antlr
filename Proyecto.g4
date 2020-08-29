grammar Proyecto;

import ProyectoTokens;

// program : classDeclaration+;

program : 'class' 'Program' '{' (declaration)* '}';

declaration : structDeclaration      
  | varDeclaration                   
  | methodDeclaration                
  ;

varDeclaration : varType ID ';'       # commonVarDeclaration
  | varType ID '[' NUM ']' ';'        # arrayVarDeclaration
  ;

structDeclaration : 'struct' ID '{' (varDeclaration)* '}';

varType : type=('int'
  | 'char'
  | 'boolean'
  | 'void')
  | 'struct' ID                         
  | structDeclaration                   
  ;

methodDeclaration : methodType ID '(' (parameter)? (',' parameter)* ')' block;

methodType : type=('int'
  | 'char'
  | 'boolean'
  | 'void')
  ;

parameter : parameterType ID            # commonParameter
  | parameterType ID '[' ']'            # arrayParameter
  ;

parameterType : type=('int'
  | 'char'
  | 'boolean')
  ;

block : '{' (varDeclaration)* (statement)* '}';

statement : 'if' '(' expression ')' block ('else' block)?   # ifStatement
  | 'while' '(' expression ')' block                        # whileStatement
  | 'return' (expression)? ';'                              # returnStatement
  | methodCall ';'                                          # methodCallStatement
  | block                                                   # blockStatement
  | location '=' expression                                 # locationStatement
  | (expression)? ';'                                       # expressionStatement
  ;

location : (ID | ID '[' expression ']') ('.' location)?;

expression : location                                       # expressionLocation
  | methodCall                                              # expressionMethodCall
  | literal                                                 # expressionLiteral
  |<assoc=right> expression op expression                   # expressionCommon
  | '-' expression                                          # expressionNegative
  | '!' expression                                          # expressionNot
  | '(' expression ')'                                      # expressionGroup
  ;

methodCall : ID '(' (expression)? (',' expression)* ')';

op : arithOp                     
  | relOp                   
  | eqOp                    
  | condOp
  ;

arithOp : simbol=('+'
  | '-'
  | '*'
  | '/'
  | '%')
  ;

relOp : simbol=('<'
  | '>'
  | '<='
  | '>=')
  ;

eqOp : simbol=('=='
  | '!=')
  ;

condOp : 'simbol=(&&'
  | '||'
  ;

literal : NUM     # intLiteral        
  | CHAR          # charLiteral   
  | BOOL          # boolLiteral
  ;