package ast;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Union;
import ast.Ast.Variant;
import parser.UnionParser.Union_argContext;
import parser.UnionParser.Union_nameContext;
import parser.UnionParser.Union_variantContext;
import static parser.UnionParser.*;

public class BuildAst {
	
	public static Union buildAst(ProgramContext programContext) {
		Map<String, Set<Variant>> union = new TreeMap<String, Set<Variant>>();
		for (Union_nameContext unc: programContext.union_name()) {
			String union_name = unc.ID().getText();
			Set<Variant> variants = convertVariants(union_name, unc.union_variant());
			union.put(union_name, variants);
		}
		
		return new Union(union);
	}

	private static List<Pair<String, String>> convertArgs(Union_variantContext uvc) {
		List<Pair<String, String>> args = new ArrayList<Pair<String, String>>();
		if (uvc.union_args() != null) {
			for (Union_argContext uac : uvc.union_args().union_arg()) {
				String type_name = null;
				if (uac.type_name().ID() == null) { type_name = uac.type_name().TYPE_NAME().getText(); }
				else {type_name = uac.type_name().ID().getText(); }
				Pair<String, String> arg = new Pair<String, String>(type_name, uac.ID().getText());
				args.add(arg);
			}
			return args;
		} else {
			return null;
		}
	}

	private static Set<Variant> convertVariants(String union_name, List<Union_variantContext> uvc) {
		Set<Variant> variants = new TreeSet<Variant>();
		if (uvc != null) {
			for (Union_variantContext uv : uvc) {
				String name = uv.ID().getText() + union_name;
				List<Pair<String, String>> args = convertArgs(uv);
				variants.add(new Variant(name, args));
			}
			return variants;
		} else {
			return null;
		}
	}


}