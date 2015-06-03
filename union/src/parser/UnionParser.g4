/*
 * Union parser
 */
 
 
parser grammar UnionParser;


options {
	tokenVocab=UnionLexer;
}


program : union_name+;

union_name
	: UNION ID LBRACE union_variant (OR union_variant)* RBRACE;

union_variant
	: ID LPAREN union_args? RPAREN;
	
union_args
	: union_arg (COMMA union_arg)*;
	
union_arg
	: type_name ID;

type_name
	: ID | TYPE_NAME;
	

