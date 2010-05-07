package gef.example.helloworld.parser;

import gef.example.helloworld.model.ElementModel;

public class DefaultXULParser extends AbstractXULParser {

	private Class<? extends ElementModel> modelClass;
	
	public DefaultXULParser(Class<? extends ElementModel> modelClass) {
		super();
		this.modelClass = modelClass;
	}

	@Override
	protected ElementModel createModel() {
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
