package gef.example.helloworld.parser.css;

public class NameSpace {
	private String name;
	private QuotedStringTerm url;
	private URI uri;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QuotedStringTerm getUrl() {
		return url;
	}

	public void setUrl(QuotedStringTerm url) {
		this.url = url;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public NameSpace() {
	}

	public NameSpace(String name, QuotedStringTerm url) {
		this.name = name;
		this.url = url;
	}
	
	public NameSpace(String name, URI uri) {
		this.name = name;
		this.uri = uri;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("@namespace ");
		if(name != null)
			sb.append(name + " ");
		
		if(uri != null)
			sb.append(uri.toString() + ";");
		else
			sb.append(url.toString() + ";");
		
		return sb.toString();
	}
	
}
