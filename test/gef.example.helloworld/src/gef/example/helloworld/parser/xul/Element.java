package gef.example.helloworld.parser.xul;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Element {
	private String name;
	private Map<String, String> attr = new LinkedHashMap<String, String>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getAttr() {
		return attr;
	}
	
	public Element(String name){
		this.name = name;
	}
	
	public void setAttr(String id, String value){
		attr.put(id, value);
	}
	
	public String getAttr(String id){
		return attr.get(id);
	}
}
