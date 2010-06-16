package gef.example.helloworld.parser.xul;


import java.util.regex.Pattern;

public class XULParser {
	private static Pattern doctypePattern = Pattern.compile("<!DOCTYPE\\s+([^\\s]+)\\s+SYSTEM\\s+\"(([^\"]+))\"\\s*>", Pattern.MULTILINE);
	private static Pattern doctypelistPattern = Pattern.compile("<!DOCTYPE\\s+([^\\s]+)\\s*\\[(.+)\\]\\s*>", Pattern.MULTILINE | Pattern.DOTALL);
	private static Pattern entityPattern = Pattern.compile("<!ENTITY\\s+%\\s+([^\\s]+)\\s+SYSTEM\\s+\"([^\"]+)\"\\s*>\\s+%(.+);", Pattern.MULTILINE);
	
	private static Pattern attrPattern = Pattern.compile("([^\\s]+)\\s*=\\s*\"(([^\"]+))\"", Pattern.MULTILINE);
	private static Pattern declarePattern = Pattern.compile("<\\?([^\\s]+)\\s+((.*))\\?>", Pattern.MULTILINE);

	private Lexer lex;
	private int token;
	
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
		case TokenType.ETAG:
			getToken();
			if(token == '-'){
				getToken(); //-
				if(token == '-'){
					
				}
			}else{
				if(token == TokenType.XML){
					xml_stmt();
				}
			}
			break;
		case TokenType.QTAG:
			break;
		}
	}

	private void xml_stmt() {
		// TODO Auto-generated method stub
		while(token != TokenType.END_QTAG && token != TokenType.EOS){
			
		}
	}
	
}
