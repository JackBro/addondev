package gef.example.helloworld.parser.css;

public class SimpleSelector {
	private String id;
	private String _class;
	private String elemnt;
	private String exp;
	
	private SimpleSelector child;
	
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
		_class = class1;
		this.elemnt = elemnt;
	}
	
	
}
