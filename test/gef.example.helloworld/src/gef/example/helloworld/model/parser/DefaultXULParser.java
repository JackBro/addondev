package gef.example.helloworld.model.parser;

import gef.example.helloworld.model.AbstractElementModel;

public class DefaultXULParser extends AbstractXULParser {

	private Class<? extends AbstractElementModel> modelClass;
	
	public DefaultXULParser(Class<? extends AbstractElementModel> modelClass) {
		super();
		this.modelClass = modelClass;
	}

	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		try {
			return modelClass.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
