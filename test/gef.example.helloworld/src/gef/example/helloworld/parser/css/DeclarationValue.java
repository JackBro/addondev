package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;

public class DeclarationValue {
	private String value;
	private List<String> args;
	
//	public DeclarationValue(String name) {
//		super();
//		this.name = name;
//		args = new ArrayList<String>();
//	}
	
	public DeclarationValue() {
		super();
		args = new ArrayList<String>();
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}

	public void addArg(String arg){
		args.add(arg);
	}
	
	public String toCSS(){
		StringBuilder sb = new StringBuilder();
		sb.append(value);
		if(args.size()>0){
			String last = args.get(args.size()-1);
			sb.append("(");
			for (String arg : args) {
				sb.append(arg);
				if(!arg.equals(last)){
					sb.append(",");
				}
			}
			sb.append(")");
		}
		return sb.toString();
	}
}
