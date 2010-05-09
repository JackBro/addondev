package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
public abstract class BoxModel extends ContentsModel {

	public static final String ATTR_ORIENT = "orient";
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddProperty(ATTR_ORIENT, 
				new ComboBoxPropertyDescriptor(ATTR_ORIENT, ATTR_ORIENT, new String[] { "horizontal","vertical" }),
				"vertical");
	}


}
