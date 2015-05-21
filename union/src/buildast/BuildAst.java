package buildast;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.TerminalNode;

import ast.Ast.Union;
import ast.Ast.Variant;
import parser.UnionParser.Union_argContext;
import parser.UnionParser.Union_nameContext;
import parser.UnionParser.Union_variantContext;
import static parser.UnionParser.*;
import static ast.Ast.*;

public class BuildAst {
	
	public static Union buildAst(ProgramContext programContext) {
		String union_name = programContext.union_name().ID().getText();
		Set<Variant> variants = convertVariants(programContext.union_name());
		return new Union(union_name, variants);
	}

	private static List<Pair<String, String>> convertArgs(Union_variantContext uv, Union_nameContext union_name) {
		List<Pair<String, String>> args = new ArrayList<Pair<String, String>>();

		for (Union_argContext ua : uv.union_args().union_arg()) {
			Pair<String, String> arg = new Pair<String, String>(ua.ID().get(0).getText(), ua.ID().get(1).getText());
			args.add(arg);
		}

		return args;
	}

	private static Set<Variant> convertVariants(Union_nameContext union_name) {
		Set<Variant> variants= new TreeSet<Variant>();
		for (Union_variantContext uv: union_name.union_variant()) {
			String name = uv.ID().getText();
			List<Pair<String, String>> args = convertArgs(uv, union_name);
			variants.add(new Variant(name, args));
		}
		return variants;
	}


}