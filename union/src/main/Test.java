package main;

import java.io.IOException;
import java.util.ArrayList;
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
import ast.*;
import ast.Ast.Unions;
import main.*;
import format.*;

public class Test {
	public static void main(String[] args) throws IOException {
		CharStream charStream = new ANTLRInputStream(System.in);
		Lexer lexer = new UnionLexer(charStream);

		CommonTokenStream tokenStream = new CommonTokenStream(lexer); 
		UnionParser parser = new UnionParser(tokenStream);
		
		ProgramContext programParseTree = parser.program();
        
        Unions unions = BuildAst.buildAst(programParseTree);
        
        //FormatUnionClass f = new FormatUnionClass(unions, "Union");
        
        FormatUnionInstance f = new FormatUnionInstance(unions);
        
        //FormatVisitorInterface f = new FormatVisitorInterface(unions);
        
        //FormatVisitorInterpreter f = new FormatVisitorInterpreter(unions);
        
        System.out.println(f);

   	}
}
