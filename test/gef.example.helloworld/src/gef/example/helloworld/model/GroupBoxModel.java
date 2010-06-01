package gef.example.helloworld.model;

public class GroupBoxModel extends BoxModel {

	public static final String CAPTION = "caption";
	public static final String CHECKBOX = "checkbox";
	
	private boolean hascaption;
	
	public boolean isHascaption() {
		return hascaption;
	}

	public void setHascaption(boolean hascaption) {
		this.hascaption = hascaption;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "groupbox";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddTextProperty(CAPTION, CAPTION, "");
		AddBoolProperty(CHECKBOX, CHECKBOX, false);
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stubW
		return super.toXML();
	}

}
