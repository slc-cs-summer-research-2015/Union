package format;

import java.util.Formatter;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Traversal;
import ast.Type;
import ast.Type.NumericType;
import ast.Type.ObjectType;
import ast.Ast.*;

public class FormatUnionInstance {
	private Formatter fmt;
	private Unions unions;
	
	public FormatUnionInstance(Unions unions) {
		this.fmt = new Formatter();
		this.unions = unions;
		
		for (Traversal t : unions.getTraversals()) {
			String className = Character.toUpperCase(t.getName().charAt(0)) + t.getName().substring(1) + unions.getName();
			fmt.format("public class %s {\n%s}\n", className, formatTraversals(t));
		}
	}
	
	public FormatUnionInstance(Unions unions, String traversal_name) {
		this.fmt = new Formatter();
		this.unions = unions;
		Traversal t = unions.getTraversal(traversal_name);
		String className = Character.toUpperCase(t.getName().charAt(0)) + t.getName().substring(1) + unions.getName();
		fmt.format("public class %s {\n%s}\n", className, formatTraversals(t));

	}
	
	
	private String formatTraversals(Traversal t) {
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
				t.getReturn_type(), methodName, parenArgs(t.getArgs()), formatAllVariantsOfArgs(t));
		return f.toString();
	}

	private String formatAllVariantsOfArgs(Traversal t) {
		Formatter f = new Formatter();
		int arg_index = 0;
		for (Type arg_type : t.getArg_types()) {
			if (unions.getNames().contains(arg_type.toString())) {
				int i = 0;
				for (Variant v : unions.getVariants(arg_type.toString())) {
					if (i == 0) {
						f.format("\t\tif (%s instanceof %s) {\n\t\t\t%s %s = (%s) %s; \n%s\t\t\t// TODO Auto-generated case match pattern\n\t\t} ", 
								t.getParameterName(arg_index), v.getName(), v.getName(), v.getName().toLowerCase(), v.getName(), t.getParameterName(arg_index), formatReturn(t.getReturn_type()));
					} else {
						f.format("else if (%s instanceof %s) {\n\t\t\t%s %s = (%s) %s; \n%s\t\t\t// TODO Auto-generated case match pattern\n\t\t} ", 
								t.getParameterName(arg_index), v.getName(), v.getName(), v.getName().toLowerCase(), v.getName(), t.getParameterName(arg_index), formatReturn(t.getReturn_type()));
					}
					i++;
				}
				f.format("else {\n\t\t\tthrow new RuntimeException(%s);\n\t\t}\n", '"'+"Unexpected variant"+'"');
			}
		arg_index++;
		}
		return f.toString();
	}


	private String formatReturn(Type arg_type) {
		if (arg_type.toString().equals("void")) {
			return "";
		} else if (arg_type instanceof NumericType) {
			Formatter f = new Formatter();
			f.format("\t\t\treturn %s;\n", 0);
			return f.toString();
		} else {
			Formatter f = new Formatter();
			f.format("\t\t\treturn %s;\n", "null");
			return f.toString();
		}
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
	
	public String toString() {
		return fmt.toString();
	}

}
