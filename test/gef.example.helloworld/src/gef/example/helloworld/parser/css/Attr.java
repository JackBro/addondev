package gef.example.helloworld.parser.css;

public class Attr {
	private String name;
	private String operator;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(name);
		sb.append(operator);
		sb.append("\"" + value + "\"");
		sb.append("]");
		return sb.toString();
	}
}
