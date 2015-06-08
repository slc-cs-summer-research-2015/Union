package format;

import java.util.Formatter;

import ast.Ast.*;

public class FormatVisitorInterpreter {
	private Formatter fmt;
	private Unions unions;
	
	public FormatVisitorInterpreter(Unions unions) {
		this.fmt = new Formatter();
		this.unions = unions;
		
		for (String union_name : unions.getNames()) {
			fmt.format("// TODO Auto-generated FormatVisitorInterpreter %s stub\n", union_name);
			fmt.format("public class %s implements %s {\n%s}\n", union_name+"Interpreter", union_name+"Visitor", formatVisitor(union_name));
		}
	}

	public FormatVisitorInterpreter(Unions unions, String union_name) {
		this.fmt = new Formatter();
		this.unions = unions;

		fmt.format("// TODO Auto-generated FormatVisitorInterface %s stub\n", union_name);
		fmt.format("public class %s implements %s {%s}\n", union_name+"Interpreter", union_name+"Visitor", formatVisitor(union_name));

	}
	
	private String formatVisitor(String union_name) {
		Formatter f = new Formatter();
		for (Variant v : unions.getVariants(union_name)) {
			f.format("\tpublic void visit(%s %s) { }\n", v.getName(), v.getName().toLowerCase());
		}
		return f.toString();
	}
	
	public String toString() {
		return fmt.toString();
	}

}
