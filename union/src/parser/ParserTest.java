package parser;


import java.io.IOException;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import parser.UnionParser.ProgramContext;


public class ParserTest {
	public static void main(String[] args) throws IOException {
		CharStream charStream = new ANTLRInputStream(System.in);
		Lexer lexer = new UnionLexer(charStream);

		CommonTokenStream tokenStream = new CommonTokenStream(lexer); 
		UnionParser parser = new UnionParser(tokenStream);
		
		ProgramContext programParseTree = parser.program();
		
		ParseTreeWalker walker = new ParseTreeWalker();
        UnionPrettyPrinter prettyPrinter = new UnionPrettyPrinter();
        walker.walk(prettyPrinter, programParseTree);
        System.out.println(prettyPrinter.toString());
        
        // uncomment to generate graphical parse tree
         programParseTree.inspect(parser);
   	}
}
