package ast;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;



public abstract class Ast {

	public static final class Union {
		public String name;
		public Set<Variant> variants;
		
		public Union(String name, Set<Variant> variants) {
			this.name = name;
			this.variants = variants;
		}
		
		public String getName() {
			return name;
		}
		
		public Set<Variant> getVariants() {
			return variants;
		}
	}

	public static final class Variant implements Comparable<Variant> {
		public String name;
		public List<Pair<String, String>> args;
	
		public Variant(String name, List<Pair<String, String>> args) {
			this.name = name;
			this.args = args;
		}

		public int compareTo(Variant v) {
			return this.name.compareToIgnoreCase(v.name);
		}
		
		public String getName() {
			return name;
		}
		
		public List<Pair<String, String>> getArgs() {
			return args;
		}
	}
	
}
