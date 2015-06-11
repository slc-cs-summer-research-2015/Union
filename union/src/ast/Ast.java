package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;



public abstract class Ast {

	public static final class Unions {
		private Map<String, Set<Variant>> unions;
		private List<Traversal> traversals;
		public static boolean hasVisitors;
		public String importText;
		
		public Unions(String importText, boolean hasVisitors, Map<String, Set<Variant>> unions, List<Traversal> traversals) {
			Unions.hasVisitors = hasVisitors;
			this.traversals = traversals;
			this.unions = unions;
			this.importText = importText;
		}
		
		public Set<String> getNames() {
			return unions.keySet();
		}
		
		public Set<Variant> getVariants(String union_name) {
			return unions.get(union_name);
		}
		
		public List<Traversal> getTraversals() {
			return traversals;
		}
		
		public Traversal getTraversal(String traversal_name) {
			for (Traversal t : traversals) {
				if (t.name.equals(traversal_name)) {
					return t;
				}
			}
			return null;
		}
		
		public String getName() {
			String name = "";
			for (String s : unions.keySet()) {
				name += s;
			}
			return name;
		}
		
		public String toString() {
			return getName();
		}
		
		public boolean hasVisitors() {
			return hasVisitors;
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
	
	public static final class Traversal {
		public String name;
		public String return_type;
		public List<String> arg_types;
		public List<Pair<String, String>> args;
		public List<String> arg_names;
		
		public Traversal(String name, String return_type, List<Pair<String, String>> args) {
			this.name = name;
			this.return_type = return_type;
			this.args = args;
			List<String> arg_types = new ArrayList<String>();
			List<String> arg_names = new ArrayList<String>();
			for (Pair<String, String> p : args) {
				arg_types.add(p.a);
				arg_names.add(p.b);
			}
			this.arg_types = arg_types;
			this.arg_names = arg_names;
		}
		
		public String getParameterName(int arg_index) {
			return arg_names.get(arg_index);
		}
		
		public String toString() {
			return name;
		}
	}
	
}
