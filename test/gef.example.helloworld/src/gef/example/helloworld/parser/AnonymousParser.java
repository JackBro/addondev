package gef.example.helloworld.parser;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.AnonymousModel;

public class AnonymousParser extends AbstractXULParser {

	private String name;
	
	public AnonymousParser(String name){
		this.name = name;	
	}
	
	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new AnonymousModel(this.name);
	}
}
