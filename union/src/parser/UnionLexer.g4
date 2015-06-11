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

COMMA : ',' ;
SEMICOLON : ';' ;
PERIOD : '.' ;

STAR: '*';

// operators
OR : '|';

// reserved words 
UNION : '%union';
VISITORS : '%visitors' ;
TRAVERSAL : '%traversal' ;
PROLOGUE_START : '%prologue' ;
PROLOGUE_END : '%prologue_end';

// identifiers
ID : LETTER ID_CHAR* ;
 
// type name
TYPE_NAME : LETTER TYPE_CHAR*;


// import text
PROLOGUE : PROLOGUE_START IMPORT_TEXT? PROLOGUE_END; 

// helper tokens
fragment NONZERO : [1-9] ;
fragment DIGIT : [0-9] ;
fragment LETTER : [a-zA-Z] ;
fragment ID_CHAR : DIGIT | LETTER | '_' ;
fragment TYPE_CHAR : DIGIT | LETTER | LANGLE | RANGLE | LBRACKET | RBRACKET ;
fragment END_LINE : '\n' | '\r' | '\n\r' ;
fragment NON_END_LINE : ~[\n\r] ;
fragment IMPORT_TEXT : (~[%])+;


// comments and white space
WHITESPACE : [ \n\r\t\f]+ -> skip ;

SINGLE_LINE_COMMENT : '//' NON_END_LINE* END_LINE -> skip ;

// multiline comments make use of ANTLR's mode stack
// assumption is that comments neither begin nor end with ':'
// lest they conflict with type annotations.
MULTILINE_COMMENT_BEGIN : '/*' -> skip , pushMode(MC) ;

mode MC;
COMMENTED_COMMENT_BEGIN : '/*' -> skip , pushMode(MC) ;
COMMENTED_COMMENT_END : '*/' -> skip , popMode ;
COMMENTED_IGNORABLE : . -> skip ;

