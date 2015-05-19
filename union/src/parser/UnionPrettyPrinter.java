
package parser;

import org.antlr.v4.runtime.tree.TerminalNode;


public class UnionPrettyPrinter extends UnionParserBaseListener {
	private StringBuilder sb;
	
	public UnionPrettyPrinter() {
		sb = new StringBuilder();
	}
	
	
	
	
	
	public void visitTerminal(TerminalNode tn) {
		sb.append(' ');
		sb.append(tn.getText());
	}
	
	
	public String toString() {
		return sb.toString();
	}
	
	
	private void newline() {
		sb.append('\n');
	}
}
