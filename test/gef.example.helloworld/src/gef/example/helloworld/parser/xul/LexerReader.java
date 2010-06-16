package gef.example.helloworld.parser.xul;

public class LexerReader {
	private boolean unget_p = false;
	private int ch;
	private int cnt;
	private String src;

	public LexerReader(String src) {
		this.src = src;
		cnt = 0;
	}

	public int read() {
		if (unget_p) {
			unget_p = false;
		} else {
			if (cnt > src.length()-1) {
				ch = -1;
			} else {
				ch = src.charAt(cnt);
				cnt++;
			}
		}
		return ch;
	}

	public void unread(int c) {
		unget_p = true;
	}
	
	public int offset()
	{
		return cnt;
	}
}
