package gef.example.helloworld.model;

import org.eclipse.draw2d.geometry.Rectangle;

public abstract class ElementModel extends AbstractModel {
	private ContentsModel parent;
	private Rectangle constraint;

	public ContentsModel getParent() {
		return parent;
	}

	public void setParent(ContentsModel parent) {
		this.parent = parent;
	}
	
	public void setConstraint(Rectangle rectangle)
	{
		constraint = rectangle;
		firePropertyChange("resize", null, rectangle);
	}
	
}
