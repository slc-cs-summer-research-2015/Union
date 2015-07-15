package format;

import java.util.Formatter;

import ast.Type;
import ast.Ast.*;
import ast.Type.BooleanType;
import ast.Type.NumericType;

public class FormatVisitorInterpreter {
	private Formatter fmt;
	private Unions unions;
	private String className;
	

	public FormatVisitorInterpreter(Unions unions, String union_name, Type return_type) {
		this.fmt = new Formatter();
		this.unions = unions;
		this.className = Character.toUpperCase(return_type.toString().charAt(0)) + return_type.toString().substring(1) + union_name + "Interpreter";

		String interpreterClassName = Character.toUpperCase(return_type.toString().charAt(0)) + return_type.toString().substring(1) + union_name + "Visitor";
		fmt.format("// TODO Auto-generated FormatVisitorInterface %s stub\n", union_name);
		fmt.format("public class %s implements %s {\n%s}\n", className, interpreterClassName, formatVisitor(union_name, return_type));

	}
	
	private String formatVisitor(String union_name, Type return_type) {
		Formatter f = new Formatter();
		for (Variant v : unions.getVariants(union_name)) {
			f.format("\tpublic %s visit(%s %s) {\n%s\t}\n", return_type.toString(), v.getName(), v.getName().toLowerCase(), formatReturn(return_type));
		}
		return f.toString();
	}
	
	private String formatReturn(Type return_type) {
		if (return_type.toString().equals("void")) {
			return "";
		} else if (return_type instanceof NumericType) {
			Formatter f = new Formatter();
			f.format("\t\treturn %s;\n", 0);
			return f.toString();
		} else if (return_type instanceof BooleanType) {
			Formatter f = new Formatter();
			f.format("\t\treturn %s;\n", "false");
			return f.toString();
		} else {
			Formatter f = new Formatter();
			f.format("\t\treturn %s;\n", "null");
			return f.toString();
		}
	}
	
	public String getClassName() {
		return className;
	}
	
	public String toString() {
		return fmt.toString();
	}

}
