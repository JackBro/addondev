package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;

public class CSS {
	private List<StyleSheet> stylesheets;
	private List<String> imports;
	private List<NameSpace> namespaces;
	
	public CSS() {
		stylesheets = new ArrayList<StyleSheet>();
		imports = new ArrayList<String>();
		namespaces = new ArrayList<NameSpace>();
	}

	public void addStyleSheet(StyleSheet stylesheet){
		stylesheets.add(stylesheet);
	}
	
	public void addStyleSheet(String name, String definedclass, String condition, String property, String value, List<String> args){
		//List<Selector> sels=null;
		boolean isfind=false;
		for (StyleSheet stylesheet : stylesheets) {
			List<Selector> sels = stylesheet.getSelector(name);
			
			boolean def = false;
			if(definedclass != null){
				for (Selector selector : sels) {
					if(selector.getDefinedclass().equals(definedclass)){
						def = true;
						break;
					}
				}
			}else{
				def = true;
			}
			
			boolean cond = false;
			if(condition != null){
				for (Selector selector : sels) {
					if(selector.getCondition().equals(condition)){
						cond = true;
						break;	
					}
				}
			}else{
				cond = true;
			}
			
			if(def && cond){
				isfind = true;
				DeclarationValue decvalue = new DeclarationValue();
				decvalue.setValue(value);
				decvalue.setArgs(args);
				stylesheet.addDeclaration(property, decvalue);
			}
		}

		if(!isfind){
			
			Selector selector = new Selector(name);
			selector.setCondition(condition);
			selector.setDefinedclass(definedclass);
			
			DeclarationValue decvalue = new DeclarationValue();
			decvalue.setValue(value);
			decvalue.setArgs(args);	
			
			StyleSheet stylesheet = new StyleSheet();
			stylesheet.addSelector(selector);
			stylesheet.addDeclaration(property, decvalue);
			
			stylesheets.add(stylesheet);
		}
	}
	
	public void addImport(String url){
		imports.add(url);
	}
	
	public void addNameSpace(NameSpace namespace){
		namespaces.add(namespace);
	}
	
	public String toCSS(){
		StringBuilder sb = new StringBuilder();
		
		for (NameSpace namespace : namespaces) {
			sb.append(namespace.toCSS() + "\n");
		}
		
		for (String imp : imports) {
			sb.append("@import \"" + imp + "\";\n");
		}
		
		for (StyleSheet styleSheet : stylesheets) {
			sb.append(styleSheet.toCSS() + "\n");
		}
		
		return sb.toString();
	}
}
