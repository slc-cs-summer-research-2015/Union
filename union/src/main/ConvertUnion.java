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
import ast.Ast.Unions;
import ast.BuildAst;

public class ConvertUnion {

	private Unions unions;

	public ConvertUnion(InputStream is) throws IOException {
		this.unions = convert(is);
	}

	private Unions convert(InputStream is) throws IOException {
		CharStream charStream = new ANTLRInputStream(is);
		Lexer lexer = new UnionLexer(charStream);

		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		UnionParser parser = new UnionParser(tokenStream);

		ProgramContext programParseTree = parser.program();

		Unions union = BuildAst.buildAst(programParseTree);

		return union;
	}
	
	public Unions getUnion() {
		return unions;
	}

}
