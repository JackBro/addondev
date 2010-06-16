package gef.example.helloworld.parser.xul;

public class TokenType {
	public static final int EOS = -1; // 文の終わりを表す。

	public static final int SYMBOL = 258;
	public static final int STRING = 259;
	
	public static final int XML = 281;
	public static final int XUL_STYLESHEET = 282;
	public static final int DOCTYPE = 283;
	public static final int ENTITY = 284;
	public static final int XUL_OVERLAY = 285;
}
