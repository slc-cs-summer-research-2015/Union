package main;

import java.util.Formatter;
import java.util.List;

import ast.Ast.Union;

public class FormatAst {
	private Formatter fmt;
	
	public FormatAst(Union union, String className) {
		fmt = new Formatter();
		fmt.format("public class %s {\n%s%s\n}", className, formatName(union.name), formatUnion(union.name, union.variants));
	}
	
	private String formatName(String name) {
		Formatter f = new Formatter();
		f.format("\tpublic static abstract class %s { }\n", name);
		return f.toString();
	}

	private String formatUnion(String name, List<String> variants) {
		Formatter f = new Formatter();
		for (String variant : variants) {
			f.format("\tpublic static final class %s extends %s {\n\t\tpublic %s() { }\n\t}\n",
					variant, name, variant);
		}
		return f.toString();
	}
	
	
	public String toString() {
		return fmt.toString();
	}

}
