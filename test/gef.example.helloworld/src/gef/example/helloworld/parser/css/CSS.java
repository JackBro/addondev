package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CSS {
	
	private class AttrComparator implements Comparator{
		  public int compare(Object o1, Object o2){
		    return ((Attr)o2).toString().compareTo(((Attr)o1).toString());
		  }
	}
	
	private List<RuleSet> rulesets;
	private List<String> imports;
	private List<NameSpace> namespaces;
	
	public CSS() {
		rulesets = new ArrayList<RuleSet>();
		imports = new ArrayList<String>();
		namespaces = new ArrayList<NameSpace>();
	}

	public void addRuleSet(RuleSet ruleset){
		rulesets.add(ruleset);
	}
	
//	public void addDeclaration(String id, Attr attr, String pseudo, Declaration declaration){
//		addDeclaration(id, 
//				new ArrayList<Attr>(Arrays.asList(attr)), 
//				new ArrayList<String>(Arrays.asList(pseudo)),
//				declaration);
//	}
	
	public void addDeclaration(String id, List<Attr> attrs, List<String> pseudos, Declaration declaration){
		boolean isfind = false;
		for (RuleSet ruleset : rulesets) {
			if (ruleset.selctors.size() == 1) {
				Selector selctor = ruleset.selctors.get(0);
				if(selctor.getSimpleSelectors().size() == 1 && selctor.getSimpleSelectors().get(0).getChild() == null){
					SimpleSelector simpleselector = selctor.getSimpleSelectors().get(0);
					if(id.equals(simpleselector.getId())){
						
						boolean isattr = false;
						if(attrs != null && (attrs.size() == simpleselector.getAttrs().size())){
							Object[] oa = attrs.toArray();  
							Arrays.sort(oa, new AttrComparator());
							
							Object[] ia = simpleselector.getAttrs().toArray();  
							Arrays.sort(ia, new AttrComparator());	
							
							isattr = true;
							for (int i = 0; i < ia.length; i++) {
								if(!ia[i].equals(oa)){
									isattr = false;
									break;
								}
							}
						}else if(attrs == null && (simpleselector.getAttrs().size() == 0)){
							isattr = true;
						}
						
						boolean ispseudo = false;
						if(pseudos !=null && (pseudos.size() == simpleselector.getPseudos().size())){
							Collections.sort(pseudos);
							Collections.sort(simpleselector.getPseudos());
							
							ispseudo = true;
							for (int i = 0; i < pseudos.size(); i++) {
								if(!pseudos.get(i).equals(simpleselector.getPseudos().get(i))){
									ispseudo = false;
									break;
								}
							}
							
						}else if(pseudos ==null && (simpleselector.getPseudos().size() == 0)){
							ispseudo = true;
						}
						
						if(isattr && ispseudo){
							isfind = true;
							Declaration eqdecl = null;
							for (Declaration decl : ruleset.getDeclarations()) {
								if(declaration.equals(decl)){
									eqdecl = decl;
									break;
								}
							}
							if(eqdecl != null){
								ruleset.getDeclarations().remove(eqdecl);
							}
							ruleset.getDeclarations().add(declaration);
						}
					}
				}
			} 
		}
		
		if(!isfind){
			SimpleSelector simpleselector = new SimpleSelector();
			simpleselector.setId(id);
			if(attrs != null) simpleselector.setAttrs(attrs);
			if(pseudos != null) simpleselector.setPseudos(pseudos);
			
			Selector selector = new Selector();
			selector.getSimpleSelectors().add(simpleselector);
			
			RuleSet ruleset = new RuleSet();
			ruleset.selctors.add(selector);
			ruleset.getDeclarations().add(declaration);
			
			rulesets.add(ruleset);
		}
	}
	
//	public void addStyleSheet(String name, String definedclass, String condition, String property, String value, List<String> args){
//		//List<Selector> sels=null;
//		boolean isfind=false;
//		for (StyleSheet stylesheet : stylesheets) {
//			List<Selector> sels = stylesheet.getSelector(name);
//			
//			boolean def = false;
//			if(definedclass != null){
//				for (Selector selector : sels) {
//					if(selector.getDefinedclass().equals(definedclass)){
//						def = true;
//						break;
//					}
//				}
//			}else{
//				def = true;
//			}
//			
//			boolean cond = false;
//			if(condition != null){
//				for (Selector selector : sels) {
//					if(selector.getCondition().equals(condition)){
//						cond = true;
//						break;	
//					}
//				}
//			}else{
//				cond = true;
//			}
//			
//			if(def && cond){
//				isfind = true;
//				DeclarationValue decvalue = new DeclarationValue();
//				decvalue.setValue(value);
//				decvalue.setArgs(args);
//				stylesheet.addDeclaration(property, decvalue);
//			}
//		}
//
//		if(!isfind){
//			
//			Selector selector = new Selector(name);
//			selector.setCondition(condition);
//			selector.setDefinedclass(definedclass);
//			
//			DeclarationValue decvalue = new DeclarationValue();
//			decvalue.setValue(value);
//			decvalue.setArgs(args);	
//			
//			StyleSheet stylesheet = new StyleSheet();
//			stylesheet.addSelector(selector);
//			stylesheet.addDeclaration(property, decvalue);
//			
//			stylesheets.add(stylesheet);
//		}
//	}
	
	public void addImport(String url){
		imports.add(url);
	}
	
	public void addNameSpace(NameSpace namespace){
		namespaces.add(namespace);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (NameSpace namespace : namespaces) {
			sb.append(namespace.toCSS() + "\n");
		}
		
		for (String imp : imports) {
			sb.append("@import \"" + imp + "\";\n");
		}
		
		for (RuleSet ruleset : rulesets) {
			sb.append(ruleset.toString() + "\n");
		}
		
		return sb.toString();
	}
}
