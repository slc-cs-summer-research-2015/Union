package ast;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.antlr.v4.runtime.misc.Pair;

import ast.Ast.Variant;
import ast.Ast.*;

public class CompareAst {
	
	public static class CompareUnions {
		public Map<String, CompareVariants> compareVariants_Unions;
		//...
		
		public CompareUnions(Unions afterU, Unions beforeU) {
			this.compareVariants_Unions = compareVariantsInUnions(afterU, beforeU);
		}

		private Map<String, CompareVariants> compareVariantsInUnions(Unions afterU, Unions beforeU) {
			Map<String, CompareVariants> compareVariants_Unions = new TreeMap<String, CompareVariants>();
			for (String union_name : afterU.getNames()) {
				CompareVariants compareVariants = new CompareVariants(
						afterU.getVariants(union_name), beforeU.getVariants(union_name));
				compareVariants_Unions.put(union_name, compareVariants);
			}
			return compareVariants_Unions;
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
		
		
		public void compareVariants(Set<Variant> afterVs, Set<Variant> beforeVs) {
			for (Variant afterV : afterVs) {
				if (beforeVs.contains(afterV)) {
					System.out.printf("Variant %s was present in the previous edit\n", afterV.getName());
					present.add(afterV);
					// compare args
					for (Variant beforeV : beforeVs) {
						if (beforeV.getName().equals(afterV.getName())) {
							CompareArgs compareArgs = new CompareArgs();
							compareArgs.addedArgs(afterV, beforeV);
							compareArgs_Variants.put(afterV, compareArgs);
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
							compareArgs_Variants.put(beforeV, compareArgs);
						} else {
							CompareArgs compareArgs = new CompareArgs();
							compareArgs.removedArgs(afterV, beforeV);
							compareArgs_Variants.put(beforeV, compareArgs);
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
		
		public boolean isVariantModified(Variant v) {
			if (compareArgs_Variants.containsKey(v)) {
				return true;
			} else {
				return false;
			}
		}
		
		public String printoutModifyMessage(Variant v) {
			Formatter f = new Formatter();
			if (isVariantModified(v)) {
				CompareArgs compareArgs = compareArgs_Variants.get(v);
				if (compareArgs.insertions != null) {
					f.format("Argument %s was added in variant %s\n",
							compareArgs.insertions.toString(), v.getName());
				}
				if (compareArgs.deletions != null) {
					f.format("Argument %s was removed in variant %s\n",
							compareArgs.deletions.toString(), v.getName());
				}
			} else {
				f.format("Variant %s has not been modified\n", v.getName());
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

