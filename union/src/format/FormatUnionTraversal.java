package format;

import java.util.Formatter;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Traversal;
import ast.Ast.*;

public class FormatUnionTraversal {
	private Formatter fmt;
	private Unions unions;
	
	public FormatUnionTraversal(Unions unions) {
		this.fmt = new Formatter();
		this.unions = unions;
		
		for (Traversal t : unions.getTraversals()) {
			String className = Character.toUpperCase(t.name.charAt(0)) + t.name.substring(1) + unions.getName();
			fmt.format("public class %s {\n%s}\n", className, formatTraversals(t));
		}
	}
	
	public FormatUnionTraversal(Unions unions, String traversal_name) {
		this.fmt = new Formatter();
		this.unions = unions;
		Traversal t = unions.getTraversal(traversal_name);
		String className = Character.toUpperCase(t.name.charAt(0)) + t.name.substring(1) + unions.getName();
		fmt.format("public class %s {\n%s}\n", className, formatTraversals(t));

	}
	
	
	private String formatTraversals(Traversal t) {
		Formatter f = new Formatter();
		String methodName = "";
		for (String arg_type : t.arg_types) {
			methodName += arg_type;
		}
		methodName = t.name + methodName;
		
		f.format("\tpublic static %s %s(%s) {\n%s\n\t}\n",
				t.return_type, methodName, parenArgs(t.args), formatAllVariantsOfArgs(t));

		return f.toString();
	}

	private String formatAllVariantsOfArgs(Traversal t) {
		Formatter f = new Formatter();
		int arg_index = 0;
		for (String arg_type : t.arg_types) {
			if (unions.getNames().contains(arg_type)) {
				int i = 0;
				for (Variant v : unions.getVariants(arg_type)) {
					if (i == 0) {
						f.format("\t\tif (%s instanceof %s) {\n\t\t\t%s %s = (%s) %s; \n\t\t\t // TODO Auto-generated case match pattern\n\t\t} ", 
								t.getParameterName(arg_index), v.getName(), v.getName(), v.getName().toLowerCase(), v.getName(), t.getParameterName(arg_index));
					} else {
						f.format("else if (%s instanceof %s) {\n\t\t\t%s %s = (%s) %s; \n\t\t\t // TODO Auto-generated case match pattern\n\t\t} ", 
								t.getParameterName(arg_index), v.getName(), v.getName(), v.getName().toLowerCase(), v.getName(), t.getParameterName(arg_index));
					}
					i++;
				}
				f.format("else {\n\t\t\tthrow new RuntimeException(%s);\n\t\t}\n", '"'+"Unexpected variant"+'"');
			}
		arg_index++;
		}
		return f.toString();
	}


	private String formatAllVariants(String union_name) {
		Formatter f = new Formatter();
		int i = 0;
		for (Variant v : unions.getVariants(union_name)) {
			if (i == 0) {
				f.format("\t\tif (%s instanceof %s) {\n\t\t\t%s %s = (%s) %s; \n\t\t\t // TODO Auto-generated case match pattern\n\t\t} ", 
						union_name.toLowerCase(), v.getName(), v.getName(), v.getName().toLowerCase(), v.getName(), union_name.toLowerCase());
			} else {
				f.format("else if (%s instanceof %s) {\n\t\t\t%s %s = (%s) %s; \n\t\t\t // TODO Auto-generated case match pattern\n\t\t} ", 
						union_name.toLowerCase(), v.getName(), v.getName(), v.getName().toLowerCase(), v.getName(), union_name.toLowerCase());
			}
			i++;
		}
		f.format("else {\n\t\t\tthrow new RuntimeException(%s);\n\t\t}\n", '"'+"Unexpected variant"+'"');

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
	
	public String toString() {
		return fmt.toString();
	}

}
