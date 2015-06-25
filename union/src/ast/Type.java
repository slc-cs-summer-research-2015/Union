package ast;

import java.util.List;

import ast.Ast.Variant;

public abstract class Type {
	
	public static final NullType NULL_TYPE = new NullType();
	public static class NullType extends Type {
		private NullType() { }
		public String toString() {
			return "null";
		}
	}
	
	public static final BooleanType BOOLEAN_TYPE = new BooleanType();
	public static class BooleanType extends Type {
		private BooleanType() { }
		public String toString() {
			return "boolean";
		}
	}
	
	public static class NumericType extends Type { 
		private String name;
		public NumericType(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
			} 
		}
	
	public static final StringType STRING_TYPE = new StringType();
	public static class StringType extends Type {
		private StringType() { }
		public String toString() {
			return "String";
		}
	}
	
	public static class ObjectType extends Type {
		public final String id;
		public final List<Type> argTypes;
		public ObjectType(String id, List<Type> argTypes) {
			this.id = id;
			this.argTypes = argTypes;
		}
		public String toString() {
			if (argTypes != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(id);
				sb.append("<");
				int i = 0;
				for (Type type : argTypes) {
					sb.append(type.toString());
					if (argTypes.size() != 1 && i != argTypes.size()-1) {
						sb.append(", "); 
						}
					i++;
				}
				sb.append(">");
				return sb.toString();
			} else {
				return id;
			}
		}
	}
	
	public int compareTo(Type t) {
		return this.toString().compareTo(t.toString());
	}
	
	public boolean equals(Type t) {
		if (this.toString().equals(t.toString())) { return true; }
		else { return false; }
	}
	
}
