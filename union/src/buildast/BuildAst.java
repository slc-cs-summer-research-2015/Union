package buildast;

import java.util.List;
import java.util.ArrayList;

import org.antlr.v4.runtime.tree.TerminalNode;

import ast.Ast.Union;
import parser.UnionParser.Union_typeContext;
import parser.UnionParser.Union_instanceContext;
import static parser.UnionParser.*;
import static ast.Ast.*;

public class BuildAst {
	
	public static Union buildAst(ProgramContext programContext) {
		String name = convertName(programContext.union_type());
		List<String> variants= new ArrayList<String>();
		variants = convertVariants(programContext.union_type());
		return new Union(name, variants);
	}

	private static List<String> convertVariants(Union_typeContext union_type) {
		List<String> variants= new ArrayList<String>();
		for (Union_instanceContext uc: union_type.union_instance()) {
			variants.add(uc.ID().getText());
		}
		return variants;
	}

	private static String convertName(Union_typeContext union_type) {
		TerminalNode id = union_type.ID();
		return id.getText();
	}

}