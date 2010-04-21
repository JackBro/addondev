package gef.example.helloworld.model;

public abstract class ElementModel extends AbstractModel {
	private ContentsModel parent;

	public ContentsModel getParent() {
		return parent;
	}

	public void setParent(ContentsModel parent) {
		this.parent = parent;
	}
	
}
