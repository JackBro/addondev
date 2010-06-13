package gef.example.helloworld.parser.css;

public class CSSParser {
	
	private Lexer lex;
	private int token;
	
	public void parse(String src){
		lex = new Lexer(src);
		while (token != TokenType.EOS) {
			stmt();
		}
	}
	private void stmt(){
		switch (token) {
		case TokenType.SYMBOL:
			
			break;
		case '{':
			
			break;
		}
		
	}
}
