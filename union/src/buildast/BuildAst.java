package buildast;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.TerminalNode;

import ast.Ast.Union;
import parser.UnionParser.Union_argContext;
import parser.UnionParser.Union_nameContext;
import parser.UnionParser.Union_variantContext;
import static parser.UnionParser.*;
import static ast.Ast.*;

public class BuildAst {
	
	public static Union buildAst(ProgramContext programContext) {
		String name = convertName(programContext.union_name());
		List<String> variants = new ArrayList<String>();
		List<List<Pair<String, String>>> args = new ArrayList<List<Pair<String, String>>>();
		variants = convertVariants(programContext.union_name());
		args = convertArgs(programContext.union_name());
		return new Union(name, variants, args);
	}

	private static List<List<Pair<String, String>>> convertArgs(
			Union_nameContext union_name) {
		List<List<Pair<String, String>>> totalArgs= new ArrayList<List<Pair<String, String>>>();
		
		for (Union_variantContext uc: union_name.union_variant()) {
			List<Pair<String, String>> args = new ArrayList<Pair<String,String>>();
			for (Union_argContext ua: uc.union_args().union_arg()) {
				Pair arg = new Pair<String, String>(ua.ID().get(0).getText(), ua.ID().get(1).getText());
				args.add(arg);
			}
			totalArgs.add(args);
		}
		return totalArgs;
	}

	private static List<String> convertVariants(Union_nameContext union_name) {
		List<String> variants= new ArrayList<String>();
		for (Union_variantContext uc: union_name.union_variant()) {
			variants.add(uc.ID().getText());
		}
		return variants;
	}

	private static String convertName(Union_nameContext union_name) {
		TerminalNode id = union_name.ID();
		return id.getText();
	}

}