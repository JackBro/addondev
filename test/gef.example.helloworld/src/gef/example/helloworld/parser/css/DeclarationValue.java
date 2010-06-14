package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;

public class DeclarationValue {
	private String name;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addArg(String arg){
		args.add(arg);
	}
	
	public String toCSS(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
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
