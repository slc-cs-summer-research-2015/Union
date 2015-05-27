package main;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;

import parser.UnionLexer;
import parser.UnionParser;
import parser.UnionParser.ProgramContext;
import ast.Ast.Union;
import buildast.BuildAst;

public class ConvertUnion {

	private Union union;

	public ConvertUnion(InputStream is) throws IOException {
		this.union = convert(is);
	}

	private Union convert(InputStream is) throws IOException {
		CharStream charStream = new ANTLRInputStream(is);
		Lexer lexer = new UnionLexer(charStream);

		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		UnionParser parser = new UnionParser(tokenStream);

		ProgramContext programParseTree = parser.program();

		Union union = BuildAst.buildAst(programParseTree);

		return union;
	}
	
	public Union getUnion() {
		return union;
	}
	
}
