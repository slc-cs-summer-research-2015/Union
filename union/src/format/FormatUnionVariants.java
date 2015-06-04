package format;

import java.util.Formatter;
import java.util.List;
import java.util.Set;

import ast.Ast.Unions;
import ast.Ast.Variant;

public class FormatUnionVariants {
	private Formatter fmt;
	private Unions unions;
	
	public FormatUnionVariants(Unions unions) {
		this.fmt = new Formatter();
		this.unions = unions;
		
		for (String union_name : unions.getNames()) {
			fmt.format("// TODO Auto-generated union %s stub\n", union_name);
			fmt.format(formatAllVariants(union_name));
		}
	}
	
	public FormatUnionVariants(Unions unions, int unionChoice) {
		this.fmt = new Formatter();
		this.unions = unions;
		String[] union_names = unions.getNames().toArray(new String[0]);

		fmt.format("// TODO Auto-generated union %s stub\n", union_names[unionChoice]);
		fmt.format(formatAllVariants(union_names[unionChoice]));

	}


	private String formatAllVariants(String union_name) {
		Formatter f = new Formatter();
		int i = 0;
		for (Variant v : unions.getVariants(union_name)) {
			if (i == 0) {
				f.format("if (%s instanceof %s) {\n\t%s %s = (%s) %s; \n\t // TODO Auto-generated case match pattern\n} ", 
						union_name.toLowerCase(), v.getName(), v.getName(), v.getName().toLowerCase(), v.getName(), union_name.toLowerCase());
			} else {
				f.format("else if (%s instanceof %s) {\n\t%s %s = (%s) %s; \n\t // TODO Auto-generated case match pattern\n} ", 
						union_name.toLowerCase(), v.getName(), v.getName(), v.getName().toLowerCase(), v.getName(), union_name.toLowerCase());
			}
			i++;
		}
		f.format("else {\n\tthrow new RuntimeException(%s);\n}", '"'+"Unexpected variant"+'"');

		return f.toString();
	}
	
	public String toString() {
		return fmt.toString();
	}

}
