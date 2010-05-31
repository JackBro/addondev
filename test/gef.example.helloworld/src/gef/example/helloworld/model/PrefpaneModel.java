package gef.example.helloworld.model;

public class PrefpaneModel extends BoxModel {

	public static final String ATTR_LABEL_TEXT = "label";
	public static final String ATTR_preference = "tabs";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "prefpane";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrTextProperty(ATTR_LABEL_TEXT, ATTR_LABEL_TEXT, "OK");
		PreferencesModel prefs = new PreferencesModel();
		prefs.setParent(getParent());
		//AddListProperty(prefs.getName(), prefs.getName(), prefs);
	}

	public String getTabLabel(){
		return (String)getPropertyValue(ATTR_LABEL_TEXT);
	}

	@Override
	public AbstractElementModel clone() {
		// TODO Auto-generated method stub
		//return super.clone();
		try {
			AbstractElementModel model = this.getClass().newInstance();
			for (Object id : fPropertyMap.keySet()) {
				ModelProperty prop = fPropertyMap.get(id);
				prop.clone();
				Object obj = fAttributeMap.get(id);
				model.AddProperty((String)id, prop.getPropertyDescriptor(), obj);
				//model.AddAttrTextProperty((String) id, prop.getPropertyDescriptor().getDisplayName(), obj);
			}
			model.setChildren(getChildren());
			
			return model;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
}
