package format;

import java.util.Formatter;
import java.util.List;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Unions;
import ast.Ast.Variant;
import ast.Type;

public class FormatUnionClass {
	private Formatter fmt;
	private Unions unions;
	
	public FormatUnionClass(Unions unions, String className) {
		this.unions = unions;
		this.fmt = new Formatter();
		fmt.format(unions.importText);
		fmt.format("public class %s {\n%s\n}", className, formatUnion());
	}
	
	private String formatUnion() {
		Formatter f = new Formatter();
		if (unions.hasVisitors()) {
			for (String union_name : unions.getNames()) {
				f.format("\tpublic static abstract class %s {\n\t\tpublic abstract void accept(%sVisitor v);\n\t}\n",
						union_name, union_name);
				f.format(formatVariants(union_name));
			}
		} else {
			for (String union_name : unions.getNames()) {
				f.format("\tpublic static abstract class %s { }\n", union_name,
						union_name);
				f.format(formatVariants(union_name));
			}
		}
		return f.toString();
	}

	private String formatVariants(String union_name) {
		Formatter f = new Formatter();
		if (unions.hasVisitors()) {
		for (Variant variant : unions.getVariants(union_name)) {
			if (variant.args != null) {
				f.format(
						"\tpublic static final class %s extends %s {\n%s\n\t\tpublic %s(%s) {\n%s\n\t\t}\n%s\n\t}\n",
						variant.name, union_name, declearArgs(variant.args), variant.name, 
						parenArgs(variant.args), setArgs(variant.args), setAcceptMethod(union_name));
			} else {
				f.format(
						"\tpublic static final class %s extends %s {\n\t\tpublic %s() { }\n%s\t}\n",
						variant.name, union_name, variant.name, setAcceptMethod(union_name));
			}
		}
		} else {
			for (Variant variant : unions.getVariants(union_name)) {
				if (variant.args != null) {
					f.format(
							"\tpublic static final class %s extends %s {\n%s\n\t\tpublic %s(%s) {\n%s\n\t\t}\n\t}\n",
							variant.name, union_name, declearArgs(variant.args), variant.name, 
							parenArgs(variant.args), setArgs(variant.args));
				} else {
					f.format(
							"\tpublic static final class %s extends %s {\n\t\tpublic %s() { }\n\t}\n",
							variant.name, union_name, variant.name);
				}
			}
		}
		return f.toString();
	}
	
	private String setAcceptMethod(String union_name) {
		Formatter f = new Formatter();
		f.format("\t\tpublic void accept(%sVisitor v) {\n\t\t\tv.visit(this);\n\t\t}", union_name);
		return f.toString();
	}

	private String setArgs(List<Pair<Type, String>> args) {
		Formatter f = new Formatter();
		for (Pair<Type, String> arg : args) {
			Type type = arg.a;
			String arg_name = arg.b;
			f.format("\t\t\tthis.%s = %s;\n", arg_name, arg_name);
		}
		return f.toString();
	}
	
	private String parenArgs(List<Pair<Type, String>> args) {
		Formatter f = new Formatter();
		for (Pair<Type, String> arg : args) {
			Type type = arg.a;
			String arg_name = arg.b;
			f.format("%s %s, ", type, arg_name);
		}
		String str = f.toString();
		return str.substring(0, str.length()-2);
	}

	private String declearArgs(List<Pair<Type, String>> args) {
		Formatter f = new Formatter();
		for (Pair<Type, String> arg : args) {
			Type type = arg.a;
			String arg_name = arg.b;
			f.format("\t\tpublic %s %s;\n", type, arg_name);
		}
		return f.toString();
	}

	public String toString() {
		return fmt.toString();
	}

}
