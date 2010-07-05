package gef.example.helloworld.parser.css;

public class Import {
	private QuotedStringTerm url;
	private URI uri;
	public QuotedStringTerm getUrl() {
		return url;
	}
	public void setUrl(QuotedStringTerm url) {
		this.url = url;
		this.uri = null;
	}
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.url = null;
		this.uri = uri;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(url != null){
			return "@import " + url.toString() + ";"; 
		}else{
			return "@import " + uri.toString() + ";"; 
		}
	}
	
}
