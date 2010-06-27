package gef.example.helloworld.parser.css;

public abstract class Term {
	private String value;
	public void setValue(String value){
		this.value = value;
	}
	public String getValue(){
		return value;
	}
	public Term(String value) {
		super();
		this.value = value;
	}
	public Term() {
		super();
	}
	
}
