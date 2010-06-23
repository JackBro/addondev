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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < selctors.size(); i++) {
			Selector selector = selctors.get(i);
			if(i!=0) sb.append(",");
			sb.append(selector.toString());
		}
		sb.append("{\n");
		for (Declaration declaration : declarations) {
			sb.append(declaration.toString() + ";\n");
		}
		sb.append("\n}");
		return sb.toString();
	}
}
