/*
 * Union parser
 */
 
 
parser grammar UnionParser;


options {
	tokenVocab=UnionLexer;
}


program : union_type;

union_type
	: UNION ID LBRACE union_instance (OR union_instance)* RBRACE;

union_instance
	: ID LPAREN RPAREN;
