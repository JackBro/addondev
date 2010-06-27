package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;

public class Selector {

	private List<SimpleSelector> simpleselectors = new ArrayList<SimpleSelector>();
	
	public List<SimpleSelector> getSimpleSelectors() {
		return simpleselectors;
	}

	public void setSimpleSelectors(List<SimpleSelector> simpleselectors) {
		this.simpleselectors = simpleselectors;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < simpleselectors.size(); i++) {
			SimpleSelector simpleselector = simpleselectors.get(i);
			if(i!=0) sb.append(" ");
			sb.append(simpleselector.toString());
		}
		
		return sb.toString();
	}
	
}
