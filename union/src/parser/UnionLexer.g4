/*
 * Union lexer
 */


lexer grammar UnionLexer;

// punctuation
LPAREN : '(' ;
RPAREN : ')' ;

LBRACE : '{' ;
RBRACE : '}' ;

// operators
OR : '|';

// reserved words 
UNION : 'union';

	

// identifiers
ID : LETTER ID_CHAR* ;


// helper tokens
fragment NONZERO : [1-9] ;
fragment DIGIT : [0-9] ;
fragment LETTER : [a-zA-Z] ;
fragment ID_CHAR : DIGIT | LETTER | '_' ;
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
