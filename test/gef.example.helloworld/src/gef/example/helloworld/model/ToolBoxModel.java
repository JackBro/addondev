package gef.example.helloworld.model;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SimpleRaisedBorder;

public class ToolBoxModel extends BoxModel {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "toolbox";
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		setPropertyValue(BoxModel.ATTR_ORIENT, "vertical");
	}

	@Override
	public void setStyle(IFigure figure) {
		// TODO Auto-generated method stub
		figure.setBorder(new SimpleRaisedBorder());
	}
}
