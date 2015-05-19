package ast;

import java.util.List;
import java.util.SortedMap;
import java.util.Set;



public abstract class Ast {

	public static final class Union {
		public String name;
		public List<String> variants;

		public Union(String name, List<String> variants) {
			this.name = name;
			this.variants = variants;
		}
	}
}
