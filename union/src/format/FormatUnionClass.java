package format;

import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Union;
import ast.Ast.Variant;

public class FormatUnionClass {
	private Formatter fmt;
	private Union union;
	
	public FormatUnionClass(Union union, String className) {
		this.union = union;
		this.fmt = new Formatter();
		fmt.format("public class %s {\n%s\n}", className, formatUnion());
	}
	
	private String formatUnion() {
		Formatter f = new Formatter();
		for (String union_name : union.getNames()) {
			f.format("\tpublic static abstract class %s { }\n", union_name);
			f.format(formatVariants(union_name));
		}
		return f.toString();
	}

	private String formatVariants(String union_name) {
		Formatter f = new Formatter();
		for (Variant variant : union.getVariants(union_name)) {
			if (variant.args != null) {
				f.format(
						"\tpublic static final class %s extends %s {\n%s\n\t\tpublic %s(%s) {\n%s}\n\t}\n",
						variant.name, union_name, declearArgs(variant.args), variant.name, 
						parenArgs(variant.args), setArgs(variant.args));
			} else {
				f.format(
						"\tpublic static final class %s extends %s {\n\t\tpublic %s() { }\n\t}\n",
						variant.name, union_name, variant.name);
			}
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
