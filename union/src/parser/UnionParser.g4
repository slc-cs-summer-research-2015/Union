/*
 * Union parser
 */
 
 
parser grammar UnionParser;


options {
	tokenVocab=UnionLexer;
}


program : prologue? VISITORS? union_name+ traversals;

prologue : PROLOGUE;

union_name
	: UNION ID LBRACE union_variant (OR union_variant)* RBRACE;

union_variant
	: ID LPAREN union_args? RPAREN;
	
traversals
	: traversal+;

traversal
	:TRAVERSAL type_name ID LPAREN union_args RPAREN ;
	
// helper rules
type_name
	: ID | TYPE_NAME;
	
union_args
	: union_arg (COMMA union_arg)*;
	
union_arg
	: type_name ID;
