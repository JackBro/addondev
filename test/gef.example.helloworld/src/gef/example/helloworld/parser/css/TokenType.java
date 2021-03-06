package gef.example.helloworld.parser.css;

public class TokenType {
	public static final int EOS = -1; // 文の終わりを表す。

	public static final int INT = 257;
	public static final int SYMBOL = 258;
	public static final int STRING = 259; // トークンが文字列であることを表す定数
	public static final int TRUE = 260; // 「真」を表す
	public static final int FALSE = 261; // 「偽」を表す
	
	public static final int EQ = 262; // '='
	public static final int NOTEQ = 263; // '^='
	public static final int TILDEEQ = 264; // '~='
	public static final int DOLEQ = 265; // '$='
	public static final int ASTERISKEQ = 266; // '*='
	public static final int OREQ = 267; // '|='
	
	public static final int IF = 268;
	public static final int ELSE = 269;
	public static final int WHILE = 270;
	public static final int FUNCTION = 271; // 'fun'
	public static final int VAR = 272; // 'def'
	public static final int CONST = 273; // 'const'
	public static final int JSDOC = 274; //	
	public static final int NEW = 275; //

	public static final int TRY = 276; //
	public static final int CATCH = 277; //
	public static final int FINALLY = 278; //

	public static final int LET = 279; //
	public static final int ARRAY = 280;
	
	public static final int IMPORT = 281;
	public static final int NAMESPACE = 282;
	
	public static final int IMPORTANT = 283;
}
