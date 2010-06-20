package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;


public class CSSParser {
	
	private Lexer lex;
	private int token;
	//private List<StyleSheet> fStyleSheets;
	//private List<String> imports;
	//private List<String> namespaces;
	private CSS css;
	
	private void getToken() {
		if (lex.advance()) {
			token = lex.token();
		} else {
			token = TokenType.EOS;
		}
	}
	
	public CSS getCSS() {
		return css;
	}
	
	public CSSParser() {
		//fStyleSheets = new ArrayList<StyleSheet>();
		//imports = new ArrayList<String>();
		//namespaces = new ArrayList<String>();
		css = new CSS();
	}
	public void parse(String src) throws CSSException{
		lex = new Lexer(src);
		while (token != TokenType.EOS) {
			stmt();
		}
	}
	private void stmt() throws CSSException{
		switch (token) {
		case TokenType.SYMBOL:
			StyleSheet stylesheet = new StyleSheet();
			selector_stmt(stylesheet);
			declaration_stmt(stylesheet);
			//fStyleSheets.add(stylesheet);
			css.addStyleSheet(stylesheet);
			getToken();
			break;
//		case '#':
//			
//			break;
//		case '@':
//			
//			break;
		case TokenType.IMPORT:
			import_stmt();
			break;
		case TokenType.NAMESPACE:
			namespace_stmt();
			break;
		default:
			getToken();
		}
		
	}
	
	//@import url(xxx.css);
	//@import "xxx.css";
	private void import_stmt() throws CSSException {
		// TODO Auto-generated method stub
		String sym = lex.value();
		getToken(); //skip symbol

		if(token == TokenType.SYMBOL){
			String url = url_stmt();
			//imports.add(url);
			css.addImport(url);
		}else if(token == TokenType.STRING){
			String url = lex.value();
			//imports.add(url);
			css.addImport(url);
			getToken(); //skip url
			if(token == ';'){
				getToken(); //;
			}else{
				throw new CSSException(lex.offset());
			}
		}else{
			throw new CSSException(lex.offset());
		}
	}
	
	//@namespace "http1";
	//@namespace url("http2");
	//@namespace xul "http3";
	//@namespace xul url("http4");
	private void namespace_stmt() throws CSSException {
		// TODO Auto-generated method stub
		String sym = lex.value();
		NameSpace namespace = new NameSpace();
		
		getToken(); //skip symbol
		
//		while(){
//			
//		}
		
		if(token == TokenType.SYMBOL){
			String m = lex.value();
			if(m.equals("url")){
				String url = url_stmt();
				//namespaces.add(url);
				namespace.setUrl(url);
			}else{
				getToken(); //skip symbol
				if(token == TokenType.STRING){
					//namespaces.add(lex.value());
					namespace.setName(lex.value());
					getToken(); //skip symbol
				}else if(token == TokenType.SYMBOL){
					String u = lex.value();
					if(u.equals("url")){
						String url = url_stmt();
						//namespaces.add(url);
						namespace.setUrl(url);
					}else{
						throw new CSSException(lex.offset());
					}		
				}else{
					throw new CSSException(lex.offset());
				}
				
			}
		}else if(token == TokenType.STRING){
			String value = lex.value();
			//namespaces.add(value);
			namespace.setUrl(value);
			getToken(); //skip url
		}else{
			throw new CSSException(lex.offset());
		}	
		
		css.addNameSpace(namespace);
		
		if(token == ';'){
			getToken(); //;
		}else{
			throw new CSSException(lex.offset());
		}
	}
	
	private String url_stmt() throws CSSException{
		String url = "";
		getToken(); //sympol
		if(token =='('){
			getToken(); //(
			//String value = lex.value();
			url = lex.value();
			getToken(); //skip symbol
			if(token == ')'){
				getToken(); //)
//				if(token == ';'){
//					getToken(); //;
//				}else{
//					throw new CSSException(lex.offset());
//				}
			}else{
				throw new CSSException(lex.offset());
			}
		}else{
			throw new CSSException(lex.offset());
		}	
		return url;
	}
	
	private void selector_stmt(StyleSheet stylesheet) {
		// TODO Auto-generated method stub
		String sym = lex.value();
		Selector selector = new Selector(sym);
		getToken(); //skip symbol
		
		selector_condition_stmt(selector);
		stylesheet.addSelector(selector);
		if(token == ','){
			while(token != '{' && token != TokenType.EOS){
				getToken(); //skip ,
				sym = lex.value();
				Selector selector2 = new Selector(sym);
				getToken(); //skip symbol
				
				selector_condition_stmt(selector2);
				stylesheet.addSelector(selector2);
			}
		}
	}
	
	private void selector2_stmt(StyleSheet stylesheet) {
		
		while(token != ',' && token != TokenType.EOS){
			
		}
	}
	
	private void selector_condition_stmt(Selector selector) {
		if(token == '['){
			getToken(); //skip [
			String cond = lex.value();
			getToken(); //skip symbol
			selector.setCondition(cond);
			getToken(); //skip ]
		}else if(token == ':'){
			getToken(); //skip :
			String def = lex.value();
			selector.setDefinedclass(def);
			getToken(); //skip def
			if(token == '['){
				getToken(); //skip [
				String cond = lex.value();
				getToken(); //skip symbol
				getToken(); //skip =
				String st = lex.value();
				getToken(); //skip string
				selector.setCondition(cond + "=" + st);
				getToken(); //skip ]
			}
		}		
	}
	
	private void declaration_stmt(StyleSheet stylesheet) {
		// TODO Auto-generated method stub
		getToken(); // skip {
		property_stmt(stylesheet);
		getToken(); //skip ;
		
		while(token != '}' && token != TokenType.EOS){
			property_stmt(stylesheet);
			getToken(); //skip ;
		}
	}
	
	private void property_stmt(StyleSheet stylesheet) {
		// TODO Auto-generated method stub
		String property = lex.value();
		DeclarationValue value = new DeclarationValue();
		
		getToken(); //skip symbol
		getToken(); //skip :
		String name = lex.value();
		value.setValue(name);	
		getToken(); //skip symbol
		while(token != ';' && token != TokenType.EOS){
			if(token == '('){
				getToken(); //skip (
				String arg = lex.value();
				value.addArg(arg);
				getToken(); //skip symbol
				if(token == ','){
					while(token != ')' && token != TokenType.EOS){
						if(token == ','){
							getToken(); //skip ,
						}
						arg = lex.value();
						value.addArg(arg);
						getToken(); //skip symbol
					}
				}else{
					while(token != ')' && token != TokenType.EOS){
						arg = lex.value();
						value.addArg(arg);
						getToken(); //skip symbol
					}					
				}
				getToken(); //skip )
			}else{
				String val = lex.value();
				value.setValue( value.getValue() + " " + val);
				getToken(); //skip symbol
			}
			
			//stylesheet.addDeclaration(property, value);
		}
		stylesheet.addDeclaration(property, value);
	}

}
