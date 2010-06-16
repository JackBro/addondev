package gef.example.helloworld.parser.css;

public class CSSException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7316138301615916764L;

	private int offset;

	public CSSException(int offset) {
		super();
		this.offset = offset;
		
	}
	
	
}
