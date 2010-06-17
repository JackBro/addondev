package gef.example.helloworld.parser.xul;

public class Entity {
	private String name;
	private String system;
	private String chromeurl;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getChromeurl() {
		return chromeurl;
	}

	public void setChromeurl(String chromeurl) {
		this.chromeurl = chromeurl;
	}

	public Entity(){
		
	}
	public Entity(String name, String system, String chromeurl) {
		super();
		this.name = name;
		this.system = system;
		this.chromeurl = chromeurl;
	}
}
