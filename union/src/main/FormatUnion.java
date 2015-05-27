package main;

import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Union;
import ast.Ast.Variant;

public class FormatUnion {
	private Formatter fmt;
	
	public FormatUnion(Union union, String className) {
		fmt = new Formatter();
		fmt.format("public class %s {\n%s%s\n}", className, formatUnion(union.name), formatVariants(union.name, union.variants));
	}
	
	private String formatUnion(String name) {
		Formatter f = new Formatter();
		f.format("\tpublic static abstract class %s { }\n", name);
		return f.toString();
	}

	private String formatVariants(String union_name, Set<Variant> variants) {
		Formatter f = new Formatter();
		for (Variant variant : variants) {
			f.format("\tpublic static final class %s extends %s {\n%s\n\t\tpublic %s(%s) {\n%s}\n\t}\n",
					variant.name, union_name, declearArgs(variant.args), variant.name, parenArgs(variant.args), setArgs(variant.args));
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
