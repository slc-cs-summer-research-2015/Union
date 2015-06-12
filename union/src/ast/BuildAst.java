package ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Traversal;
import ast.Ast.Unions;
import ast.Ast.Variant;
import parser.UnionParser.PrologueContext;
import parser.UnionParser.TraversalContext;
import parser.UnionParser.TraversalsContext;
import parser.UnionParser.Type_nameContext;
import parser.UnionParser.Union_argContext;
import parser.UnionParser.Union_nameContext;
import parser.UnionParser.Union_variantContext;
import static parser.UnionParser.*;

public class BuildAst {
	
	public static Unions buildAst(ProgramContext programContext) {
		String importText = convetPrologue(programContext.prologue());
		Map<String, Set<Variant>> unions = new TreeMap<String, Set<Variant>>();
		for (Union_nameContext unc: programContext.union_name()) {
			String union_name = unc.ID().getText();
			Set<Variant> variants = convertVariants(union_name, unc.union_variant());
			unions.put(union_name, variants);
		}
		List<Traversal> traversals = new ArrayList<Traversal>();
		traversals = convertTraversals(programContext.traversals());
		return new Unions(importText, programContext.VISITORS() != null, unions, traversals);
	}

	private static String convetPrologue(PrologueContext prologue) {
		String p = prologue.getText();
		return p.substring(9, p.length()-13);
	}

	private static List<Traversal> convertTraversals(TraversalsContext traversalsContext) {
		List<Traversal> traversals = new ArrayList<Ast.Traversal>();
		for (TraversalContext t : traversalsContext.traversal()) {
			String name = t.ID().getText();
			Type return_type = convertTypes(t.type_name());
			List<Pair<Type, String>> args = convertArgs(t);
			traversals.add(new Traversal(name, return_type, args));
		}
		return traversals;
	}
	
	private static Type convertTypes(Type_nameContext type_name) {
		if (type_name.primitive_type() != null) {
			if (type_name.primitive_type().BOOLEAN_TYPE() != null) {
				return Type.BOOLEAN_TYPE;
			} else {
				return new Type.NumericType(type_name.primitive_type().NUMERIC_TYPE().getText());
			}
		} else {
			if (type_name.reference_type().type_args() == null) {
				return new Type.ObjectType(type_name.reference_type().ID().getText(), null);
			} else {
				List<Type> argTypes = new ArrayList<Type>();
				for (Type_argContext type_arg : type_name.reference_type().type_args().type_arg()) {
					argTypes.add(convertTypes(type_arg.type_name()));
				}
				return new Type.ObjectType(type_name.reference_type().ID().getText(), argTypes);
			}
		}
	}

	private static List<Pair<Type, String>> convertArgs(TraversalContext tc) {
		List<Pair<Type, String>> args = new ArrayList<Pair<Type, String>>();
		if (tc.union_args() != null) {
			for (Union_argContext uac : tc.union_args().union_arg()) {
				Type type = convertTypes(uac.type_name());
				Pair<Type, String> arg = new Pair<Type, String>(type, uac.ID().getText());
				args.add(arg);
			}
			return args;
		} else {
			return null;
		}
	}

	private static List<Pair<Type, String>> convertArgs(Union_variantContext uvc) {
		List<Pair<Type, String>> args = new ArrayList<Pair<Type, String>>();
		if (uvc.union_args() != null) {
			for (Union_argContext uac : uvc.union_args().union_arg()) {
				Type type = convertTypes(uac.type_name());
				Pair<Type, String> arg = new Pair<Type, String>(type, uac.ID().getText());
				args.add(arg);
			}
			return args;
		} else { return null; }
	}

	private static Set<Variant> convertVariants(String union_name, List<Union_variantContext> uvc) {
		Set<Variant> variants = new TreeSet<Variant>();
		if (uvc != null) {
			for (Union_variantContext uv : uvc) {
				String name = uv.ID().getText() + union_name;
				List<Pair<Type, String>> args = convertArgs(uv);
				variants.add(new Variant(name, args));
			}
			return variants;
		} else {
			return null;
		}
	}


}