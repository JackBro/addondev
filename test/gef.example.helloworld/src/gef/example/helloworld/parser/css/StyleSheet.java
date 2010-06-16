package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class StyleSheet {
	private List<Selector> fSelectors;
	private Map<String, DeclarationValue> fDeclaration;

	public StyleSheet() {
		fSelectors = new ArrayList<Selector>();
		fDeclaration = new HashMap<String,DeclarationValue>();
	}
	
	public void addSelector(Selector selector){
		fSelectors.add(selector);
	}
	
	public void addDeclaration(String property, DeclarationValue value){
		fDeclaration.put(property, value);
	}
	
	public List<Selector> getSelector(String name){
		List<Selector> list = new ArrayList<Selector>();
		for (Selector sel : fSelectors) {
			if(sel.getName().equals(name)){
				list.add(sel);
			}
		}
		
		return list;
	}
	
	public DeclarationValue getDeclarationrValue(String property){
		return fDeclaration.get(property);
	}
	
	public String toCSS(){
		StringBuilder sb = new StringBuilder();
		Selector lastsel = fSelectors.get(fSelectors.size()-1);
		for (Selector selector : fSelectors) {
			sb.append(selector.toCSS());
			if(!selector.equals(lastsel)){
				sb.append(",");
			}
		}
		sb.append("{\n");
		
		for (Entry<String, DeclarationValue> declaration : fDeclaration.entrySet()) {
			String name = declaration.getKey();
			String value = declaration.getValue().toCSS();
			sb.append(name);
			sb.append(":");
			sb.append(value);
			sb.append(";\n");
		}
		
		sb.append("}\n");
		
		return sb.toString();
	}
}
