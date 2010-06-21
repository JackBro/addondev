package gef.example.helloworld.parser.css;


import java.util.HashMap;
import java.util.Map;

public class Lexer {
	private int tok;
	 private String val;
	 public LexerReader reader;
	 private int offset;

	private static Map<String, Integer> reserved = new HashMap<String, Integer>();

	static {

		reserved.put("@import", new Integer(TokenType.IMPORT));
		reserved.put("@namespace", new Integer(TokenType.NAMESPACE));
	}

	public Lexer(String src) {
		reader = new LexerReader(src);
	}

	public String value() {
		return val;
	}

	public boolean advance() {
		skipWhiteSpace();
		int c = reader.read();
		if (c < 0) {
			return false;
		}
		switch (c) {
		case '{':
		case '}':
		case ':':
		case ';':
		case ',':
	    case '(':
	    case ')':
	    case '.':	    	
	    case '^':
	    case '~':
	    case '[':
	    case ']':
	    case '\\':
	    case '?':
	    //case '$':
	    case '#':
	    //case '!':
	    case '*':
	    case '>':
	    case '<':
	    case '+':
	    //case '-':
	    case '|':
	    case '&':
	    case '%':
	    //case '@':
			tok = c;
			break;
		case '=':
	        c = reader.read();
	        if(c == '='){
	          tok = TokenType.EQ;    // '=='
	        }else{
	          reader.unread(c);
	          tok = '=';             // '='
	        }			
			break;
		case '/':
	        c = reader.read();   // 次の文字が
	        if(c == '*'){  // '*'だったら
	        	c = reader.read(); // /** だったら

		          skipComment();     //   複数行コメントとして読み飛ばし
		          return advance();  //   次のトークンを読みにいく。
	        }
			break;
		case '"':
		case '\'':
			lexString((char)c);
			tok = TokenType.STRING;
			break;
		default:
//			if(Character.isDigit((char)c)){
//		          reader.unread(c);
//		          lexDigit();
//		          tok = TokenType.INT;				 
//			}
//			else
			{
				reader.unread(c);
				lexSymbol(); 
			}
		}
		return true;
	}
	
	  private void lexDigit() {
		    int num = 0;
		    while(true){
		      int c = reader.read();
		      if(c < 0){
		        break;
		      }
		      if(!Character.isDigit((char)c)){
		        reader.unread(c);         // returnする前にunread()する！
		        break;
		      }
		      num = (num * 10) + (c - '0');
		    }
		    val = String.valueOf(num);
		  }	
	
	private void lexString(char ch) {
		StringBuilder buf = new StringBuilder();
		// TODO Auto-generated method stub
		while (true) {
			int c = reader.read();
			if (c < 0) {
				//throw new Exception("文字列中でファイルの終わりに到達しました。");
				break;
			}
			if (c == ch) {
				break;
			} else if (c == '\\') { // (A)
				c = reader.read();
				if (c < 0) {
					//throw new Exception("文字列中でファイルの終わりに到達しました。");
					break;
				}
			}
			buf.append((char) c);
		}
		val = buf.toString();	
	}

	private void skipComment() {
		// TODO Auto-generated method stub
	    int c = '\0';
	    while(true){
	      c = reader.read();
	      if(c < 0){
	        //throw new Exception("コメント中にファイルの終端に到達しました。");
	    	  break;
	      }
	      if(c == '*'){
	        c = reader.read();
	        if(c == '/'){
	          break;
	        }
	      }
	    }		
	}

	private void skipWhiteSpace() {
		// TODO Auto-generated method stub
		int c = reader.read();
		char ch = (char)c;
		//while ((c != -1) && Character.isWhitespace((char) c)) {
		while ((c != -1) && (Character.isWhitespace(ch) || (ch == '\t') || (ch == '\n') || (ch == '\r'))) {
			c = reader.read();
			ch = (char)c;
		}
		reader.unread(c);
	}

	private void lexSymbol() {
		offset = reader.offset();
		
		tok = TokenType.SYMBOL;
		StringBuilder buf = new StringBuilder();
		while (true) {
			int c = reader.read();
			if (c < 0) {
				// throw new Exception("ファイルの終わりに到達しました。");
			}
			if (!isCSS((char) c)) {
				reader.unread(c);
				break;
			}
			buf.append((char) c);
		}
		String s = buf.toString();
		val = s;

		if (reserved.containsKey(s)) { // (A)
			tok = ((Integer) reserved.get(s)).intValue();
		}
	}

	public int token() {
		// TODO Auto-generated method stub
		 return tok;
	}
	
	public int offset()
	{
		offset = reader.offset();
		return offset;
	}
	
//	public void un(int c)
//	{
//		reader.unread(c);
//	}
	
	private boolean isCSS(char c){
		if (!Character.isJavaIdentifierPart(c)) {
			if(c == '-' //|| c == '#' || c == '@' || c == '!' || c == '>' || c == '.'
				|| Character.isDigit(c)){
				return true;
			}
			return false;
		}
		
		return true;
	}
	
}
