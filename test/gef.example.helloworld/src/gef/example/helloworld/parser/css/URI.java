package gef.example.helloworld.parser.css;

public class URI {
	private boolean paren;
	private QuotedStringTerm url;

	public boolean isParen() {
		return paren;
	}

	public void setParen(boolean paren) {
		this.paren = paren;
	}

	public QuotedStringTerm getUrl() {
		return url;
	}

	public void setUrl(QuotedStringTerm url) {
		this.url = url;
	}

	public URI(QuotedStringTerm url) {
		super();
		this.url = url;
	}

	public URI() {
		super();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(paren){
			return "url(" + url.toString() + ")";
		}else{
			return url.toString();
		}
	}
}
