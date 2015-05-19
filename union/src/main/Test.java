package main;


import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import parser.UnionLexer;
import parser.UnionParser;
import parser.UnionPrettyPrinter;
import parser.UnionParser.ProgramContext;
import ast.Ast.Union;
import buildast.*;
import main.*;

public class Test {
	public static void main(String[] args) throws IOException {
		CharStream charStream = new ANTLRInputStream(System.in);
		Lexer lexer = new UnionLexer(charStream);

		CommonTokenStream tokenStream = new CommonTokenStream(lexer); 
		UnionParser parser = new UnionParser(tokenStream);
		
		ProgramContext programParseTree = parser.program();
        
        Union union = BuildAst.buildAst(programParseTree);
        
        FormatAst fa = new FormatAst(union, "Union");
        
        System.out.println(fa);
        System.out.println("test1");
   	}
}
