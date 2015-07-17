package format;

import java.util.Formatter;

import ast.Type;
import ast.Ast.*;
import ast.Type.BooleanType;
import ast.Type.NumericType;

public class FormatVisitorFunction {
	private Formatter fmt;
	private Unions unions;
	private String className;
	

	public FormatVisitorFunction(Unions unions, String union_name, Traversal t) {
		this.fmt = new Formatter();
		this.unions = unions;
		//this.className = Character.toUpperCase(return_type.toString().charAt(0)) + return_type.toString().substring(1) + union_name + "Interpreter";
		this.className = Character.toUpperCase(t.getName().charAt(0)) + t.getName().substring(1) + union_name + "Visitor";
		//String interpreterClassName = Character.toUpperCase(t.getReturn_type().toString().charAt(0)) + t.getReturn_type().toString().substring(1) + union_name + "Visitor";
		String interpreterClassName = "I" + Character.toUpperCase(t.getReturn_type().toString().charAt(0)) +t.getReturn_type().toString().substring(1) +  union_name + "Visitor";
		fmt.format("public class %s implements %s {\n%s}\n", className, interpreterClassName, formatVisitor(union_name, t.getReturn_type()));

	}
	
	private String formatVisitor(String union_name, Type return_type) {
		Formatter f = new Formatter();
		for (Variant v : unions.getVariants(union_name)) {
			f.format("\tpublic %s visit(%s %s) {\n\t\t// TODO Auto-generated %s stub\n\t%s\t}\n", return_type.toString(), v.getName(), v.getName().toLowerCase(), className, formatReturn(return_type));
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
