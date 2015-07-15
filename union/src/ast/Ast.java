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
		private Map<String, Set<Variant>> unions;
		private List<Traversal> traversals;
		private static boolean hasVisitors;
		private String importText;
		
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
		
		public String getImportText() {
			if (importText == null) {
				return "";
			}
			return importText;
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
			// implement printout of unions if needed in the future
//			StringBuilder sb = new StringBuilder();
//			if (importText != null) {
//				sb.append("%prologue\n");
//				sb.append(getImportText());
//				sb.append("%prologue_end\n");
//			}
//			if (hasVisitors) {
//				sb.append("%visitors\n");
//			}
//			for (String union_name : unions.keySet()) {
//				sb.append("%union ");
//				sb.append(union_name + " {\n");
//				boolean first = true;
//				for (Variant v : unions.get(union_name)) {
//					if (first) {
//						
//						first = false;
//					} else {
//						
//					}
//				}
//			}
//			return sb.toString();
		}
		
		public boolean hasVisitors() {
			return hasVisitors;
		}
	}

	public static final class Variant implements Comparable<Variant> {
		private String name;
		private List<Pair<Type, String>> args;
	
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
		private String name;
		private Type return_type;
		private List<Pair<Type, String>> args;
		private List<Type> arg_types;
		private List<String> arg_names;
		
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
		
		
		public Type getReturn_type() {
			return return_type;
		}
		
		public List<Pair<Type, String>> getArgs() {
			return args;
		}
		
		public String getName() {
			return name;
		}
		
		public List<Type> getArg_types() {
			return arg_types;
		}
		
		public List<String> getArg_names() {
			return arg_names;
		}
		
		public String getParameterName(int arg_index) {
			return arg_names.get(arg_index);
		}
		
		public Type getUnionArg(Unions unions) {
			for (String union_name : unions.getNames()) {
				for (Type t : arg_types) {
					if (t.toString().equals(union_name)) {
						return t;
					}
				}
			}
			throw new RuntimeException("Union type not found in the traversal input");
		}
		
		public String toString() {
			return name;
		}
	}
	
}
