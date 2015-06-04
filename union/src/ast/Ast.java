package ast;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;



public abstract class Ast {

	public static final class Unions {
		public Map<String, Set<Variant>> union;
		
		public Unions(Map<String, Set<Variant>> union) {
			this.union = union;
		}
		
		public Set<String> getNames() {
			return union.keySet();
		}
		
		public Set<Variant> getVariants(String union_name) {
			return union.get(union_name);
		}
		
		public String getName() {
			String name = "";
			for (String s : union.keySet()) {
				name += s;
			}
			return name;
		}
		
		public String toString() {
			return getName();
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
