package gef.example.helloworld.parser.css;


public class CSSParser {
	
	private Lexer lex;
	private int token;
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
		css = new CSS();
	}
	public void parse(String src) throws CSSException{
		lex = new Lexer(src);
		getToken();
		while (token != TokenType.EOS) {
			switch (token) {
			case '@':
				getToken();//@
				
				break;

			default:
				ruleset_stmt();
				break;
			}
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
	
	
	private void ruleset_stmt(){
		RuleSet ruleset = new RuleSet();
		css.addRuleSet(ruleset);
		selector_stmt(ruleset);
		if(token == ','){
			//getToken(); //,
			while(token == ',' && token != TokenType.EOS){
				getToken(); //,
				selector_stmt(ruleset);
			}
		}
		if(token == '{'){
			getToken(); //{
			while(token != '}' && token != TokenType.EOS){
				declaration_stmt(ruleset);
			}
			getToken(); //}
		}else{
			getToken();
		}
	}
	
	private void declaration_stmt(RuleSet ruleset) {
		// TODO Auto-generated method stub
		String sym = "";
		if(token =='#'){
			sym = "#";
			getToken(); //#
		}
		if(token == TokenType.SYMBOL){
			sym = sym + lex.value();
			getToken();
			if(token == ':'){
				Declaration declaration = new Declaration();
				declaration.setName(sym);
				ruleset.getDeclarations().add(declaration);
				Expr expr = new Expr();
				declaration.setExpr(expr);
				getToken(); //:
				while(token != ';' && token != TokenType.EOS){
					term_stm(declaration, expr);
				}
				getToken(); //;
			}else{
				getToken();
			}
			
		}else{
			getToken();
		}
	}

	private void term_stm(Declaration declaration, Expr expr) {
		switch (token) {
		case TokenType.STRING:
			String qssym = lex.value();
			getToken();
			QuotedStringTerm qsterm = new QuotedStringTerm();
			qsterm.setValue(qssym);
			expr.getTerms().add(qsterm);				
			break;
		case TokenType.SYMBOL:
			String sym = lex.value();
			getToken();
			if(token == '('){
				FunctionTerm trem = new FunctionTerm();
				trem.setValue(sym);
				Expr funcexpr = new Expr();
				trem.setExpr(funcexpr);
				while(token != ')' && token != TokenType.EOS){
					term_stm(null, funcexpr);
				}
				getToken(); //)
				expr.getTerms().add(trem);
			}else{
				SymbolTerm trem = new SymbolTerm(sym);
				//trem.setValue(sym);
				expr.getTerms().add(trem);		
			}
			break;
		case '#':
			getToken(); //#
			String hex = "#" + lex.value();
			getToken(); //hex
			SymbolTerm trem = new SymbolTerm(hex);
			//trem.setValue(hex);
			expr.getTerms().add(trem);			
			break;
		case TokenType.IMPORTANT:
			if(declaration != null) declaration.setImportant(true);
			getToken(); //!importaint
			break;
		default:
			getToken();
			break;
		}
	}
	
	private void selector_stmt(RuleSet ruleset) {
		String sym = lex.value();
		Selector selector = new Selector();
		simpleselector_stmt(selector);
		//getToken(); //skip symbol
		
		//while(token != '{' && token != TokenType.EOS){
		while(token != TokenType.EOS){
			switch (token) {
			case '+':
			case '>':
			case '~':
				//getToken();
				simpleselector_stmt(selector);
				break;
			default:
				simpleselector_stmt(selector);
				ruleset.selctors.add(selector);
				return;
				//break;
			}
		}
	}
	
	private void simpleselector_stmt(Selector selector) {
		
		if(token == TokenType.SYMBOL || token == '.' || token == '#'){
			SimpleSelector simpleselector = new SimpleSelector();
			if(token == TokenType.SYMBOL ){
				String elemnt = lex.value();
				simpleselector.setElemnt(elemnt);
			}else if(token == '.' ){
				getToken();
				String elemnt = lex.value();
				simpleselector.set_class(elemnt);
			}
			else if(token == '#'){
				getToken();
				String elemnt = lex.value();
				simpleselector.setId(elemnt);
			}
			selector.getSimpleSelectors().add(simpleselector);
			int offset = lex.offset();
			getToken(); //SYMBOL
			
			SimpleSelector parent = simpleselector;
			while(token != TokenType.EOS){
				switch (token) {
				case TokenType.SYMBOL:
					String sym = lex.value();
					getToken(); //SYMBOL
					SimpleSelector child = new SimpleSelector();
					selector.getSimpleSelectors().add(child);
					child.setElemnt(sym);
					parent.setChild(child);
					parent = child;
					break;
				case '.': //class
					int offset2 = lex.offset();
					getToken(); //.
					String cls = lex.value();
					getToken(); //SYMBOL
					SimpleSelector child2 = new SimpleSelector();
					child2.set_class(cls);
					if(offset != offset2){
						selector.getSimpleSelectors().add(child2);
					}else{
						//selector.getSimpleSelectors().add(child2);
						parent.setChild(child2);
						parent = child2;
					}
					break;
				case '#': //id
					getToken(); //#
					String id = lex.value();
					getToken(); //SYMBOL
					SimpleSelector child3 = new SimpleSelector();
					selector.getSimpleSelectors().add(child3);
					child3.setId(id);
					parent.setChild(child3);
					parent = child3;
					break;	
				case '[':
					offset = attr_stmt(parent);
					//offset = lex.offset();
					break;	
				case ':':
					offset = pseudo_stmt(parent);
					//offset = lex.offset();
					break;
				default:
					//getToken();
					return;
					//break;
				}	
			}
		}else{
			//getToken();
		}
	}
	
	private int attr_stmt(SimpleSelector parent) {
		// TODO Auto-generated method stub
		getToken(); //[
		if(token == TokenType.SYMBOL){
			String sym = lex.value();
			Attr attr = new Attr();
			attr.setName(sym);
			
			//parent.setAttr(attr);
			parent.getAttrs().add(attr);
			
			getToken(); //symbol
			if(token == TokenType.EQ || 
					token == TokenType.NOTEQ ||
					token == TokenType.TILDEEQ ||
					token == TokenType.DOLEQ ||
					token == TokenType.ASTERISKEQ ||
					token == TokenType.OREQ
					){
				attr.setOperator(lex.value());
				getToken();
				
				if(token == TokenType.SYMBOL){
					String vale = lex.value();
					getToken();
					attr.setValue(vale);
				}else if(token == TokenType.STRING){
					String vale = lex.value();
					getToken();
					attr.setValue(vale);
				}
			}
		}
		while(token != ']' && token != TokenType.EOS){
			getToken();
		}
		int offset = lex.offset()+1;
		getToken(); //]
		
		return offset;
	}
	
	private int pseudo_stmt(SimpleSelector parent) {
		// TODO Auto-generated method stub
		int offset=lex.offset()+1;
		getToken(); //:
		if(token ==':') {
			offset=lex.offset()+1;
			getToken();
		}
		if(token == TokenType.SYMBOL){
			String value = lex.value();
			//parent.setPseudo(value);
			parent.getPseudos().add(value);
			offset=lex.offset()+value.length();
			getToken(); //symbol
		}
		
		return offset;
	}
	
	public Declaration declaration_stmt(String src) {
		Declaration declaration = null;
		lex = new Lexer(src);
		getToken();

		if(token == TokenType.SYMBOL){
			String sym = lex.value();
			getToken();
			if(token == ':'){
				declaration = new Declaration();
				declaration.setName(sym);
				Expr expr = new Expr();
				declaration.setExpr(expr);
				getToken(); //:
				while(token != ';' && token != TokenType.EOS){
					term_stm(declaration, expr);
				}
				getToken(); //;
			}else{
				getToken();
			}
			
		}else{
			getToken();
		}
		
		return declaration;
	}
}
