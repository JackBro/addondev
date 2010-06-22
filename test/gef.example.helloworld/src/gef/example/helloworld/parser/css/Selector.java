package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;

public class Selector {
//	private String name;
//	private String definedclass;
//	private String condition;
	private List<SimpleSelector> simpleselectors = new ArrayList<SimpleSelector>();
	
	public List<SimpleSelector> getSimpleSelectors() {
		return simpleselectors;
	}

	public void setSimpleSelectors(List<SimpleSelector> simpleselectors) {
		this.simpleselectors = simpleselectors;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getDefinedclass() {
//		return definedclass;
//	}
//
//	public void setDefinedclass(String definedclass) {
//		this.definedclass = definedclass;
//	}
//
//	public String getCondition() {
//		return condition;
//	}
//
//	public void setCondition(String condition) {
//		this.condition = condition;
//	}
//
//	public Selector(String name) {
//		this.name = name;
//	}
	
	public String toCSS(){
		StringBuilder sb = new StringBuilder();
//		sb.append(name);
//		if(definedclass != null){
//			sb.append(":");
//			sb.append(definedclass);
//		}
//		if(condition != null){
//			sb.append("[");
//			sb.append(condition);
//			sb.append("]");
//		}
		
		return sb.toString();
	}
}
