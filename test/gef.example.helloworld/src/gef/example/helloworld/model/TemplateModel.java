package gef.example.helloworld.model;

public class TemplateModel extends AbstractDataModel {

	public static final String TEXT = "text";
	
	private String text;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "template";
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		setPropertyValue(TEXT, text);
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		//super.installModelProperty();
		text="";
		AddMultiLineTextProperty(TEXT, "template", text);	
	}

	
}
