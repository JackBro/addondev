package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;

public class SimpleSelector {
	private String id;
	private String _class;
	private String elemnt;
	private String exp;
	
	private List<Attr> attrs = new ArrayList<Attr>();
	private List<String> pseudos = new ArrayList<String>();
	
	private SimpleSelector child;
	
	
	public List<Attr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<Attr> attrs) {
		this.attrs = attrs;
	}

	public List<String> getPseudos() {
		return pseudos;
	}

	public void setPseudos(List<String> pseudos) {
		this.pseudos = pseudos;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public SimpleSelector getChild() {
		return child;
	}

	public void setChild(SimpleSelector child) {
		this.child = child;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String class1) {
		_class = class1;
	}

	public String getElemnt() {
		return elemnt;
	}

	public void setElemnt(String elemnt) {
		this.elemnt = elemnt;
	}

	public SimpleSelector() {
		super();
	}

	public SimpleSelector(String id, String class1, String elemnt) {
		super();
		this.id = id;
		this._class = class1;
		this.elemnt = elemnt;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		if(id != null) sb.append("#" + id);
		if(_class != null) sb.append("." + _class);
		if(elemnt != null) sb.append(elemnt);
		for (Attr attr : attrs) {
			sb.append(attr.toString());
		}
		for (String pseudo : pseudos) {
			sb.append(":" + pseudo);
		}		
		
		//if(child != null) sb.append(child.getExp() + child.toString());
		if(child != null) sb.append(" " + child.toString());
		
		return sb.toString();
	}
}
