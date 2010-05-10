package gef.example.helloworld.model;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class LabelModel extends ElementModel {
	// 変更の種類を識別するための文字列
	public static final String P_CONSTRAINT = "_constraint";
	public static final String P_TEXT = "value";

	private String text = "Hello World";
	//private Rectangle constraint; // 制約

	public String getText() {
		return getPropertyValue(P_TEXT).toString();
	}
	
	public LabelModel() {
		super();
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddProperty(P_TEXT, new TextPropertyDescriptor(P_TEXT, "あいさつ"), "Hello World");
		//AddProperty("P_TEXT", new ListPropertyDescriptor("P_TEXT", "あいさつ"), "Hello World");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "label";
	}
}
