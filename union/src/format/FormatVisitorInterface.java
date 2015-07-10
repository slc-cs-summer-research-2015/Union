package format;

import java.util.Formatter;

import ast.Type;
import ast.Ast.*;

public class FormatVisitorInterface {
	private Formatter fmt;
	private Unions unions;

//  loop through all the unions in the file	
//	public FormatVisitorInterface(Unions unions) {
//		this.fmt = new Formatter();
//		this.unions = unions;
//		
//		for (String union_name : unions.getNames()) {
//			fmt.format("// TODO Auto-generated FormatVisitorInterface %s stub\n", union_name);
//			fmt.format("public interface %s {\n%s\n}\n", union_name+"Visitor", formatVisitor(union_name));
//		}
//	}

	public FormatVisitorInterface(Unions unions, String union_name, Type return_type) {
		this.fmt = new Formatter();
		this.unions = unions;

		fmt.format("public interface %s {\n%s\n}", union_name+"Visitor", formatVisitor(union_name, return_type));
	}
	
//	// without return type name, return void
//	private String formatVisitor(String union_name) {
//		Formatter f = new Formatter();
//		for (Variant v : unions.getVariants(union_name)) {
//			f.format("\tvoid visit(%s %s);\n", v.getName(), v.getName().toLowerCase());
//		}
//		return f.toString();
//	}
	
	// with return type name
	private String formatVisitor(String union_name, Type return_type) {
		Formatter f = new Formatter();
		for (Variant v : unions.getVariants(union_name)) {
			f.format("\t%s visit(%s %s);\n", return_type.toString(), v.getName(), v.getName().toLowerCase());
		}
		return f.toString();
	}
	
	public String toString() {
		return fmt.toString();
	}

}
