/*
 * Union lexer
 */


lexer grammar UnionLexer;

// punctuation
LPAREN : '(' ;
RPAREN : ')' ;

LBRACE : '{' ;
RBRACE : '}' ;

LANGLE : '<' ;
RANGLE : '>' ;

LBRACKET : '[' ;
RBRACKET : ']' ;

COMMA : ',';

// operators
OR : '|';

// reserved words 
UNION : '%union';
VISITORS : '%visitors' ;
TRAVERSAL : '%traversal' ;
PROLOGUE : '%prologue' ;

// identifiers
ID : LETTER ID_CHAR* ;
 
// type name
TYPE_NAME : LETTER TYPE_CHAR*;


// helper tokens
fragment NONZERO : [1-9] ;
fragment DIGIT : [0-9] ;
fragment LETTER : [a-zA-Z] ;
fragment ID_CHAR : DIGIT | LETTER | '_' ;
fragment TYPE_CHAR : DIGIT | LETTER | LANGLE | RANGLE | LBRACKET | RBRACKET ;
fragment END_LINE : '\n' | '\r' | '\n\r' ;
fragment NON_END_LINE : ~[\n\r] ;


// comments and white space
WHITESPACE : [ \n\r\t\f]+ -> skip ;

SINGLE_LINE_COMMENT : '//' NON_END_LINE* END_LINE -> skip ;

// multiline comments make use of ANTLR's mode stack
// assumption is that comments neither begin nor end with ':'
// lest they conflict with type annotations.
MULTILINE_COMMENT_BEGIN : '/*' -> skip , pushMode(MC) ;

mode MC;
COMMENTED_TYPE_BEGIN : '/*:' -> skip ;
COMMENTED_TYPE_END : ':*/' -> skip ;

COMMENTED_COMMENT_BEGIN : '/*' -> skip , pushMode(MC) ;
COMMENTED_COMMENT_END : '*/' -> skip , popMode ;
COMMENTED_IGNORABLE : . -> skip ;
