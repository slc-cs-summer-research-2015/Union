package format;

import java.util.Formatter;

import ast.Type;
import ast.Ast.*;

public class FormatVisitorInterface {
	private Formatter fmt;
	private Unions unions;
	private String className;


	public FormatVisitorInterface(Unions unions, String union_name, Traversal t) {
		this.fmt = new Formatter();
		this.unions = unions;
		this.className = "I" + Character.toUpperCase(t.getName().charAt(0)) + t.getName().substring(1) +  union_name + "Visitor";
		fmt.format("public interface %s {\n%s\n}", className, formatVisitor(union_name, t.getReturn_type()));
	}
	
	
	// with return type name
	private String formatVisitor(String union_name, Type return_type) {
		Formatter f = new Formatter();
		for (Variant v : unions.getVariants(union_name)) {
			f.format("\t%s visit(%s %s);\n", return_type.toString(), v.getName(), v.getName().toLowerCase());
		}
		return f.toString();
	}
	
	public String getClassName() {
		return className;
	}
	
	public String toString() {
		return fmt.toString();
	}

}
