package format;

import java.util.Formatter;
import java.util.List;
import java.util.Set;

import ast.Ast.Union;
import ast.Ast.Variant;

public class FormatUnionVariants {
	private Formatter fmt;
	private Union union;
	private String arbitraryUnionName;
	private String arbitraryUnionsName;
	
	public FormatUnionVariants(Union union) {
		this.fmt = new Formatter();
		this.union = union;
		this.arbitraryUnionName = union.getName().toLowerCase();
		this.arbitraryUnionsName = union.getName().toLowerCase()+"s";
		
		fmt.format("// TODO Auto-generated union %s stub\n", union.getName());
//		fmt.format("public void convertVariants(List<%s> %s) {\n\tfor (%s %s : %s) {\n%s\n\t}\n}",
//				union.getName(), arbitraryUnionsName, union.getName(), arbitraryUnionName, arbitraryUnionsName, formatAllVariants());
		fmt.format(formatAllVariants());
	}


	private String formatAllVariants() {
		Formatter f = new Formatter();
		int i = 0;
		for (Variant v : union.variants) {
			if (i == 0) {
				f.format("if (%s instanceof %s) {\n\t%s %s = (%s) %s; \n\t // TODO Auto-generated case match pattern\n} ", 
						arbitraryUnionName, v.getName(), v.getName(), v.getName().toLowerCase(), union.getName(), arbitraryUnionName);
			} else {
				f.format("else if (%s instanceof %s) {\n\t%s %s = (%s) %s; \n\t // TODO Auto-generated case match pattern\n} ", 
						arbitraryUnionName, v.getName(), v.getName(), v.getName().toLowerCase(), union.getName(), arbitraryUnionName);
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
