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

type_name
	: primitive_type | reference_type ;	
	
// helper rules
primitive_type : BOOLEAN_TYPE |	NUMERIC_TYPE ; 

reference_type :  ID (type_args)? ;

type_args : LANGLE type_arg (COMMA type_arg)* RANGLE ;

type_arg : type_name ;
	
union_args
	: union_arg (COMMA union_arg)*;
	
union_arg
	: type_name ID;
