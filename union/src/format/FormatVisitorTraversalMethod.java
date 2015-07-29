package format;

import java.util.Formatter;
import java.util.List;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Traversal;
import ast.Ast.Variant;
import ast.Type;
import ast.Type.*;
import ast.Ast.*;

public class FormatVisitorTraversalMethod {
	
	private Formatter fmt;
	private Unions unions;
	private Traversal t;
	private String className;

	public FormatVisitorTraversalMethod(Unions unions, Traversal t) {
		this.fmt = new Formatter();
		this.unions = unions;
		this.t = t;
		this.className = Character.toUpperCase(t.getName().charAt(0)) + t.getName().substring(1) + t.getUnionArg(unions);
		fmt.format("public class %s {\n%s}\n", className, formatTraversal());

	}

	private String formatTraversal() {
		Formatter f = new Formatter();
		String methodName = "";
		for (Type arg_type : t.getArg_types()) {
			if (arg_type instanceof ObjectType) {
				ObjectType ot = (ObjectType) arg_type;
				methodName += ot.id;
			} else {
				methodName += arg_type.toString();
			}
		}
		methodName = t.getName() + methodName;
		f.format("\tpublic static %s %s(%s) {\n%s\n\t}\n",
				t.getReturn_type(), methodName, parenArgs(t.getArgs()), formatVariantOfArgs());
		return f.toString();
	}
	
	// should only have single variant in the traversal
	private String formatVariantOfArgs() {
		Formatter f = new Formatter();
		int arg_index = 0;
		for (Type arg_type : t.getArg_types()) {
			if (unions.getNames().contains(arg_type.toString())) {
				String interpreter_name = getInterpreterClassName(t.getReturn_type().toString(), arg_type.toString());
				f.format("\t\t%s %s = new %s();\n", interpreter_name, interpreter_name.toLowerCase(), interpreter_name);
				if (t.getReturn_type().toString().equals("void")) {
					f.format("\t\t%s.accept(%s);\n", t.getParameterName(arg_index), interpreter_name.toLowerCase());
				} else {
					//f.format("\t\treturn %s.visit(%s);\n", interpreter_name.toLowerCase(), t.getParameterName(arg_index));
					f.format("\t\treturn %s.accept(%s);\n", t.getParameterName(arg_index), interpreter_name.toLowerCase());
				}
			}
		arg_index++;
		}
		return f.toString();
	}
	
	private String getInterpreterClassName(String return_type, String union_name) {
		return Character.toUpperCase(t.getName().charAt(0)) + t.getName().substring(1) + union_name + "Visitor";
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
	
	public String getClassName() {
		return className;
	}
	
	public String toString() {
		return fmt.toString();
	}
}
