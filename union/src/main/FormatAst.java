package main;

import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Union;

public class FormatAst {
	private Formatter fmt;
	
	public FormatAst(Union union, String className) {
		fmt = new Formatter();
		fmt.format("public class %s {\n%s%s\n}", className, formatName(union.name), formatVariants(union.name, union.variants, union.args));
	}
	
	private String formatName(String name) {
		Formatter f = new Formatter();
		f.format("\tpublic static abstract class %s { }\n", name);
		return f.toString();
	}

	private String formatVariants(String name, List<String> variants, List<List<Pair<String, String>>> args) {
		Formatter f = new Formatter();
		int i = 0;
		for (String variant : variants) {
			f.format("\tpublic static final class %s extends %s {\n%s\n\t\tpublic %s(%s) {\n%s}\n\t}\n",
					variant, name, declearArgs(args.get(i)), variant, parenArgs(args.get(i)), setArgs(args.get(i)));
		i++;
		}
		return f.toString();
	}
	
	private String setArgs(List<Pair<String, String>> args) {
		Formatter f = new Formatter();
		for (Pair<String, String> arg : args) {
			String type = arg.a;
			String arg_name = arg.b;
			f.format("\t\t\tthis.%s = %s;\n", arg_name, arg_name);
		}
		return f.toString();
	}
	
	private String parenArgs(List<Pair<String, String>> args) {
		Formatter f = new Formatter();
		for (Pair<String, String> arg : args) {
			String type = arg.a;
			String arg_name = arg.b;
			f.format("%s %s, ", type, arg_name);
		}
		String str = f.toString();
		return str.substring(0, str.length()-2);
	}

	private String declearArgs(List<Pair<String, String>> args) {
		Formatter f = new Formatter();
		for (Pair<String, String> arg : args) {
			String type = arg.a;
			String arg_name = arg.b;
			f.format("\t\tpublic %s %s;\n", type, arg_name);
		}
		return f.toString();
	}

	public String toString() {
		return fmt.toString();
	}

}
