package format;

import java.util.Formatter;
import java.util.List;
import java.util.Set;

import ast.Ast.Union;
import ast.Ast.Variant;

public class FormatUnionVariants {
	private Formatter fmt;
	private Union union;
	
	public FormatUnionVariants(Union union) {
		this.fmt = new Formatter();
		this.union = union;
		
		for (String union_name : union.getNames()) {
			fmt.format("// TODO Auto-generated union %s stub\n", union_name);
			fmt.format(formatAllVariants(union_name));
		}
	}


	private String formatAllVariants(String union_name) {
		Formatter f = new Formatter();
		int i = 0;
		for (Variant v : union.getVariants(union_name)) {
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
