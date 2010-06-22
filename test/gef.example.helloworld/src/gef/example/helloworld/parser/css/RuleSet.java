package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;

public class RuleSet {
	List<Selector> selctors = new ArrayList<Selector>();
	List<Declaration> declarations = new ArrayList<Declaration>();
	public List<Selector> getSelctors() {
		return selctors;
	}
	public void setSelctors(List<Selector> selctors) {
		this.selctors = selctors;
	}
	public List<Declaration> getDeclarations() {
		return declarations;
	}
	public void setDeclarations(List<Declaration> declarations) {
		this.declarations = declarations;
	}
	
	
}
