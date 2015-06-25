package ast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import ast.Type;


public abstract class Ast {

	public static final class Unions {
		public Map<String, Set<Variant>> unions;
		public List<Traversal> traversals;
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
		
		public Set<Type> getReturnTypes() {
			Set<Type> return_types = new HashSet<Type>();
			for (Traversal t : traversals) {
				return_types.add(t.return_type);
			}
			return return_types;
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
		public List<Pair<Type, String>> args;
	
		public Variant(String name, List<Pair<Type, String>> args) {
			this.name = name;
			this.args = args;
		}

		public int compareTo(Variant v) {
			return this.name.compareToIgnoreCase(v.name);
		}
		
		public String getName() {
			return name;
		}
		
		public String toString() {
			return name;
		}
		
		public List<Pair<Type, String>> getArgs() {
			return args;
		}
		
		public boolean containsArg(Pair<Type, String> Otherarg) {
			if (this.args != null) {
				for (Pair<Type, String> arg : this.args) {
					if (arg.a.equals(Otherarg.a) && arg.b.equals(Otherarg.b)) {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	public static final class Traversal {
		public String name;
		public Type return_type;
		public List<Type> arg_types;
		public List<Pair<Type, String>> args;
		public List<String> arg_names;
		
		public Traversal(String name, Type return_type, List<Pair<Type, String>> args) {
			this.name = name;
			this.return_type = return_type;
			this.args = args;
			List<Type> arg_types = new ArrayList<Type>();
			List<String> arg_names = new ArrayList<String>();
			for (Pair<Type, String> p : args) {
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
