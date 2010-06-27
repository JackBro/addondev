package gef.example.helloworld.parser.xul;


import java.util.ArrayList;
import java.util.List;

public class XULParser {

	private Lexer lex;
	private int token;
	private Element xml, overlay;
	private List<Element> stylesheets = new ArrayList<Element>();
	private List<Doctype> doctypelist = new ArrayList<Doctype>();
	
	public Element getOverlay() {
		return overlay;
	}

	public List<Element> getStylesheets() {
		return stylesheets;
	}

	public List<Doctype> getDoctypelist() {
		return doctypelist;
	}

	private void getToken() {
		if (lex.advance()) {
			token = lex.token();
		} else {
			token = TokenType.EOS;
		}
	}
	
	public void parse(String src){
		lex = new Lexer(src);
		while (token != TokenType.EOS) {
			stmt();
		}
	}
	
	private void stmt(){
		switch (token) {
//		case TokenType.START_COMMENTTAG: //<!--
//			getToken();
//			comment_stmt();
//			break;
		case TokenType.XUL_STYLESHEET: //<?xml-stylesheet
			stylesheets.add(element_stmt());
			break;
		case TokenType.XML:
			//getToken();
			xml = element_stmt();	
			break;
		case TokenType.XUL_OVERLAY:
			//getToken();
			overlay = element_stmt();	
			break;
		case TokenType.DOCTYPE: //<!DOCTYPE
			doctype_stmt();
			break;
		default:
			getToken();
			break;
		}
	}

	private void doctype_stmt() {
		// TODO Auto-generated method stub
		getToken(); //skip <!DOCTYPE
		if(token == TokenType.SYMBOL){
			String target = lex.value();
			getToken(); ///skip symbol
			if(token == TokenType.SYMBOL){
//				String system = lex.value();
//				getToken(); ///skip symbol
//				if(token == TokenType.STRING){
//					String url = lex.value();
//					doctypelist.add(new Doctype(target, system, url));
//				}
				Doctype doctype = new Doctype();
				doctype.setTarget(target);
				//doctype.setSystem(system);
				while(token != '>' && token != TokenType.EOS){
					switch (token) {
					case TokenType.SYMBOL:
						if(lex.value().equals("SYSTEM")){
							doctype.setSystem(lex.value());
						}else{
							doctype.setTarget(lex.value());
						}
						break;
					case TokenType.STRING:
						doctype.setChromeurl(lex.value());
						break;
					default:
						//getToken();
						break;
					}
					getToken();
				}
				doctypelist.add(doctype);
				getToken(); //skip >
			}else if(token == '['){
				Doctype doctype = new Doctype(target, "", "");
				while(token != '>' && token != TokenType.EOS){
					if(token == TokenType.ENTITY){
						Entity entity = new Entity();
						entity_stmt(entity);
						doctype.addEntity(entity);
					}else{
						getToken();
					}
				}
				doctypelist.add(doctype);
				getToken(); //skip >
			}	
		}
	}

	private void entity_stmt(Entity entity) {
		// TODO Auto-generated method stub
		getToken(); //<!ENTITY
		while(token != '>' && token != TokenType.EOS){
			switch (token) {
			case TokenType.SYMBOL:
				if(lex.value().equals("SYSTEM")){
					entity.setSystem(lex.value());
				}else{
					entity.setName(lex.value());
				}
				break;
			case TokenType.STRING:
				entity.setChromeurl(lex.value());
				break;
			default:
				//getToken();
				break;
			}
			getToken();
		}
		getToken(); //skip >
	}

	private Element element_stmt() {
		String name = lex.value();
		Element elem = new Element(name);
		
		getToken();
		attr_stmt(elem);
		
		return elem;
	}
	
	private void attr_stmt(Element elem) {
		// TODO Auto-generated method stub
		while(token != TokenType.END_QTAG && token != TokenType.EOS){
			if(token == TokenType.SYMBOL){
				String id = lex.value();
				getToken();
				if(token == '='){
					getToken(); //=
					if(token == TokenType.STRING){
						String value = lex.value();
						elem.setAttr(id, value);
						//System.out.println("value = " + value);
						//getToken();
					}else{
						//break;
						//getToken();s
					}
				}
			}
			getToken();
		}
	}

//	private void comment_stmt() {
////		while(token != TokenType.END_COMMENTTAG && token != TokenType.EOS){
////			getToken();
////		}
//		lex.skipComment();
//		getToken();
//	}
}
