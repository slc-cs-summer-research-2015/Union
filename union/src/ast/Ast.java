package ast;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;



public abstract class Ast {

	public static final class Union {
		public String name;
		public List<String> variants;
		public List<List<Pair<String, String>>> args;

		
		public Union(String name, List<String> variants, List<List<Pair<String, String>>> args) {
			this.name = name;
			this.variants = variants;
			this.args= args;
		}
		
	}
}
