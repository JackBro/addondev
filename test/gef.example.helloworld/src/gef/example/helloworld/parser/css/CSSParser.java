package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;


public class CSSParser {
	
	private Lexer lex;
	private int token;
	private List<StyleSheet> fStyleSheets;
	
	
	public List<StyleSheet> getStyleSheets() {
		return fStyleSheets;
	}
	public CSSParser() {
		fStyleSheets = new ArrayList<StyleSheet>();
	}
	public void parse(String src){
		lex = new Lexer(src);
		while (token != TokenType.EOS) {
			stmt();
		}
	}
	private void stmt(){
		switch (token) {
		case TokenType.SYMBOL:
			StyleSheet stylesheet = new StyleSheet();
			selector_stmt(stylesheet);
			declaration_stmt(stylesheet);
			fStyleSheets.add(stylesheet);
			getToken();
			break;
		case '{':
			
			break;
		default:
			getToken();
		}
		
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
		value.setName(name);	
		getToken(); //skip symbol
		while(token != ';' && token != TokenType.EOS){
			//String property = lex.value();
//			getToken(); //skip symbol
//			getToken(); //skip :
//			String name = lex.value();
//			value.setName(name);
			//DeclarationValue value = new DeclarationValue(name);
			//getToken(); //skip symbol
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
				value.setName( value.getName() + " " + val);
				getToken(); //skip symbol
			}
			
			//stylesheet.addDeclaration(property, value);
		}
		stylesheet.addDeclaration(property, value);
	}
	private void getToken() {
		if (lex.advance()) {
			token = lex.token();
		} else {
			token = TokenType.EOS;
		}
	}
}
