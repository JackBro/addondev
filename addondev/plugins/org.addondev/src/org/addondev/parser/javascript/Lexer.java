package org.addondev.parser.javascript;


import java.util.HashMap;
import java.util.Map;

public class Lexer {
	private int tok;
	 private String val;
	 public LexerReader reader;
	 private int offset;

	private static Map<String, Integer> reserved = new HashMap<String, Integer>(); // 予約語を保持する

	static { // 予約語を登録

		//reserved.put("if", new Integer(TokenType.IF));
		//reserved.put("else", new Integer(TokenType.ELSE));
		//reserved.put("while", new Integer(TokenType.WHILE)); // while
		reserved.put("function", new Integer(TokenType.FUNCTION));
		reserved.put("var", new Integer(TokenType.VAR));
		reserved.put("const", new Integer(TokenType.CONST));
		reserved.put("new", new Integer(TokenType.NEW));
		//reserved.put("true", new Integer(TokenType.TRUE));
		//reserved.put("false", new Integer(TokenType.FALSE));
		
		reserved.put("try", new Integer(TokenType.TRY));
		reserved.put("catch", new Integer(TokenType.CATCH));
		reserved.put("finally", new Integer(TokenType.FINALLY));
		
		reserved.put("let", new Integer(TokenType.LET));
	}

	public Lexer(String src) {
		reader = new LexerReader(src); // readerにはLexerReaderをセットする！
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
	    case '!':
	    case '*':
	    case '>':
	    case '<':
	    case '+':
	    case '-':
	    case '|':
	    case '&':
	    case '%':
	    case '@':
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
	        if(c == '/'){        // '/'だったら
	          skipLineComment(); //   １行コメントとして読み飛ばし
	          return advance();  //   次のトークンを読みにいく。
	        }else if(c == '*'){  // '*'だったら
	        	c = reader.read(); // /** だったら
	        	if(c == '*')
	        	{
	        		tok = TokenType.JSDOC;
	        		jsDoc();
	        		//return advance();  //   次のトークンを読みにいく。
	        	}
	        	else
	        	{
		          skipComment();     //   複数行コメントとして読み飛ばし
		          return advance();  //   次のトークンを読みにいく。
	        	}
	        }else{               // それ以外なら
	          reader.unread(c);  // １文字戻しておいて
	          tok = '/';         // 普通に演算子'/'として処理する。
	        }
			break;
		case '"':
		case '\'':
			//buf = new StringBuffer();
			lexString((char)c);
			tok = TokenType.STRING;
			break;
		default:
			if(Character.isDigit((char)c)){
		          reader.unread(c);
		          lexDigit();
		          tok = TokenType.INT;				 
			}
			else
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
	
	//private StringBuffer buf = new StringBuffer();
	private void lexString(char ch) {
		//StringBuffer buf = new StringBuffer();
		StringBuilder buf = new StringBuilder();
		// TODO Auto-generated method stub
		while (true) {
			int c = reader.read();
			if (c < 0) {
				//throw new Exception("文字列中でファイルの終わりに到達しました。");
				break;
			}
			//if (c == '"') {
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

	private void skipLineComment() {
		// TODO Auto-generated method stub
	    int c;
	    while((c = reader.read()) != '\n'){
	      if(c < 0){
	        //throw new Exception("コメント中にファイルの終端に到達しました。");
	    	  break;
	      }
	    }
	    reader.unread(c);		
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
			if (!Character.isJavaIdentifierPart((char) c)) {
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
	
	private void jsDoc() {
		// TODO Auto-generated method stub
		StringBuilder buf = new StringBuilder();
		buf.append("/**");
	    int c = '\0';
	    while(true){
	      c = reader.read();
	      if(c < 0){
	        //throw new Exception("コメント中にファイルの終端に到達しました。");
	    	  break;
	      }
	      buf.append((char) c);
	      if(c == '*'){
	        c = reader.read();
	        if(c == '/'){
	        	buf.append("/");
	    		String s = buf.toString();
	    		val = s;
	          break;
	        }
	      }
	    }		
	}
	
	public void jsRex() {
		// TODO Auto-generated method stub
		int d = 0;
	    int c = '\0';
	    while(true){
	    	d = c;
	      c = reader.read();
	      if(c < 0){
	        //throw new Exception("コメント中にファイルの終端に到達しました。");
	    	  break;
	      }
	      
	      //if(c != '\\'){
	        //c = reader.read();
	        if(d != '\\' && c == '/'){
	        //if(c == '/'){
	        	
	          break;
	        }
	      //}
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
	
	public void un(int c)
	{
		reader.unread(c);
	}
}
