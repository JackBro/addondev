package gef.example.helloworld.parser.css;

public class NameSpace {
	private String name;
	private String url;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public NameSpace() {
	}

	public NameSpace(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public String toCSS(){
		if(name != null)
			return "@namespace " + name + " url(" + "\"" + url + "\"" + ");";
		else
			return "@namespace url(" + "\"" + url + "\"" + ");";
	}
}
