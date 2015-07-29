package ast;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Traversal;
import ast.Ast.Variant;
import ast.Ast.*;

public class CompareAst {
	
	public static class CompareUnions {
		private Unions afterU;
		private Unions beforeU;
		public Map<String, CompareVariants> compareVariants_Unions;
		//...
		
		public CompareUnions(Unions afterU, Unions beforeU) {
			this.afterU = afterU;
			this.beforeU = beforeU;
			this.compareVariants_Unions = compareVariantsInUnions();
		}

		private Map<String, CompareVariants> compareVariantsInUnions() {
			Map<String, CompareVariants> compareVariants_Unions = new TreeMap<String, CompareVariants>();
			for (String union_name : afterU.getNames()) {
				if (beforeU.getVariants(union_name) == null) {
					// an union is added
				} else {
					CompareVariants compareVariants = new CompareVariants(
							afterU.getVariants(union_name), beforeU.getVariants(union_name));
					compareVariants_Unions.put(union_name, compareVariants);
				}
			}
			return compareVariants_Unions;
		}
		
		// for instanceof mode
		public List<Variant> getTraversalInstaces(Traversal t, int mode) {
			final int insert = 0;
			final int delete = 1;
			final int modified = 2;
			List<Variant> traversalInstaces = new ArrayList<Variant>();

			for (Type union_type : t.getArg_types()) {
				// pick the argument types that are union types
				if (afterU.getNames().contains(union_type.toString())) {
					// found additional union types
					if (!beforeU.getNames().contains(union_type.toString())) {
						// add this additional union with all the corresponding variants, but what if it's only renamed?
					} else {
						CompareVariants compareVariants = compareVariants_Unions.get(union_type.toString());
						if (mode == insert) {
							traversalInstaces = compareVariants.getInsertions();
						} else if (mode == delete) {
							traversalInstaces = compareVariants.getDeletions();
						} else if (mode == modified) {
							traversalInstaces.addAll(compareVariants.getModified());
						}
					}
				}
			}
			return traversalInstaces;
		}
		
		// get the message describing argument change
		public String getVariantModifyMessage(Traversal t, Variant v) {
			CompareVariants compareVariants = compareVariants_Unions.get(t.getUnionArg(afterU).toString());
			return compareVariants.printoutModifyMessage(v);
		}
		
		public String getVariantModifyMessage(String union_name, Variant v) {
			CompareVariants compareVariants = compareVariants_Unions.get(union_name);
			return compareVariants.printoutModifyMessage(v);

		}
		
		// get the union that the traversal takes
		public Pair<Type, String> getUnionTypeInTraversal(Traversal t) {
			for (int i = 0; i < t.getArg_types().size(); i++) {
				if (afterU.getNames().contains(t.getArg_types().get(i).toString())) {
					// there will only be 1 argument that is union typed
					return t.getArgs().get(i);
				}
			}
			System.out.printf("Union type not found in Traversal %s\n", t.getName());
			return null;
		}
		
		// for VisitorTraversalMethod
		public Type getReturnTypeChangeInTraversal(Traversal t) {
			Traversal before = beforeU.getTraversal(t.getName());
			if (before.getReturn_type() != t.getReturn_type()) {
				return t.getReturn_type();
			} else {
				return null;
			}
		}

	
}

	
	public static class CompareVariants {
		private List<Variant> insertions;
		private List<Variant> deletions;
		private List<Variant> present;
		private Map<Variant, CompareArgs> compareArgs_Variants;
		
		public CompareVariants(Set<Variant> afterVs, Set<Variant> beforeVs) {
			this.insertions = new ArrayList<Variant>();
			this.deletions = new ArrayList<Variant>();
			this.present = new ArrayList<Variant>();
			this.compareArgs_Variants = new TreeMap<Variant, CompareArgs>();
			compareVariants(afterVs, beforeVs);
		}
		
		
		private void compareVariants(Set<Variant> afterVs, Set<Variant> beforeVs) {
			for (Variant afterV : afterVs) {
				if (beforeVs.contains(afterV)) {
					System.out.printf("Variant %s was present in the previous edit\n", afterV.getName());
					present.add(afterV);
					// compare args
					for (Variant beforeV : beforeVs) {
						if (beforeV.getName().equals(afterV.getName())) {
							CompareArgs compareArgs = new CompareArgs();
							compareArgs.addedArgs(afterV, beforeV);
							if (compareArgs.hasChanged()) {
								compareArgs_Variants.put(afterV, compareArgs);
							}
						}
					}
				} else {
					System.out.printf("Variant %s was added\n", afterV.getName());
					insertions.add(afterV);
				}
			}
			// check if variants were removed
			for (Variant beforeV : beforeVs) {
				if (!afterVs.contains(beforeV)) {
					System.out.printf("Variant %s was removed\n", beforeV.getName());
					deletions.add(beforeV);
				}
				// compare args
				for (Variant afterV : afterVs) {
					if (afterV.getName().equals(beforeV.getName())) {
						if (compareArgs_Variants.containsKey(beforeV)) {
							CompareArgs compareArgs = compareArgs_Variants.get(beforeV);
							compareArgs.removedArgs(afterV, beforeV);
							if (compareArgs.hasChanged()) {
								compareArgs_Variants.put(beforeV, compareArgs);
							}
						} else {
							CompareArgs compareArgs = new CompareArgs();
							compareArgs.removedArgs(afterV, beforeV);
							if (compareArgs.hasChanged()) {
								compareArgs_Variants.put(beforeV, compareArgs);
							}
						}
					}
				}
			}
		}
		
		public List<Variant> getInsertions() {
			return insertions;
		}

		public List<Variant> getDeletions() {
			return deletions;
		}

		public List<Variant> getPresent() {
			return present;
		}
		
		public Set<Variant> getModified() {
			return compareArgs_Variants.keySet();
		}
		
		public boolean isVariantModified(Variant v) {
			if (compareArgs_Variants.containsKey(v)) {
				return compareArgs_Variants.get(v).hasChanged();
			} else {
				return false;
			}
		}
		
		public String printoutModifyMessage(Variant v) {
			Formatter f = new Formatter();
			if (isVariantModified(v)) {
				CompareArgs compareArgs = compareArgs_Variants.get(v);
				if (compareArgs.hasAdded()) {
					f.format("//Argument %s was added in variant %s\n",
							compareArgs.insertions.toString(), v.getName());
				}
				if (compareArgs.hasRemoved()) {
					f.format("//Argument %s was removed in variant %s\n",
							compareArgs.deletions.toString(), v.getName());
				}
			} else {
				f.format("//Variant %s has not been modified\n", v.getName());
			}
			return f.toString();
		}
		
		
	}
	
	public static class CompareArgs {
		public List<Pair<Type, String>> insertions;
		public List<Pair<Type, String>> deletions;

		public CompareArgs() {
			this.insertions = null;
			this.deletions = null;
		}
		
		public boolean hasChanged() {
			if (hasAdded() || hasRemoved()) {
				return true;
			}
			return false;
		}
		
		public boolean hasAdded() {
			if (insertions != null) {
				if (!insertions.isEmpty()) {
					return true;
				}
			}
			return false;
		}
		
		public boolean hasRemoved() {
			if (deletions != null) {
				if (!deletions.isEmpty()) {
					return true;
				}
			}
			return false;
		}

		private void addedArgs(Variant afterV, Variant beforeV) {
			insertions = new ArrayList<Pair<Type, String>>();
			if (afterV.getArgs() != null) {
				for (Pair<Type, String> arg : afterV.getArgs()) {
					if (!beforeV.containsArg(arg)) {
						System.out.printf("Argument %s %s was added in variant %s\n",
								arg.a, arg.b, afterV.getName());
						insertions.add(arg);
					}
				}
			}
		}
		
		private void removedArgs(Variant afterV, Variant beforeV) {
			deletions = new ArrayList<Pair<Type, String>>();
			if (beforeV.getArgs() != null) {
				for (Pair<Type, String> arg : beforeV.getArgs()) {
					if (!afterV.containsArg(arg)) {
						System.out.printf("Argument %s %s was removed in variant %s\n",
										arg.a, arg.b, beforeV.getName());
						deletions.add(arg);
					}
				}
			}
		}

	}
}

