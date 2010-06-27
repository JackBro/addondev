package gef.example.helloworld.parser.css;

public abstract class AbstractElement {
	private int offset;
	private int len;
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	
}
