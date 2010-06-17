package gef.example.helloworld.parser.xul;

import java.util.ArrayList;
import java.util.List;

public class Doctype {
	private String target;
	private String system;
	private String chromeurl;
	
	private List<Entity> entitylist;
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public void addEntity(Entity entity){
		entitylist.add(entity);
	}
	
	public List<Entity> getEntitylist() {
		return entitylist;
	}
	public Doctype(){
		
	}
	public Doctype(String target, String system, String chromeurl) {
		super();
		this.target = target;
		this.system = system;
		this.chromeurl = chromeurl;
		entitylist = new ArrayList<Entity>();
	}
}
