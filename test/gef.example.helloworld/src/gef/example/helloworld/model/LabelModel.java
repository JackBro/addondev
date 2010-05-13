package gef.example.helloworld.model;

public class LabelModel extends ElementModel {
	// 変更の種類を識別するための文字列
	public static final String P_CONSTRAINT = "_constraint";
	public static final String P_TEXT = "value";

	private String text = "Hello World";
	//private Rectangle constraint; // 制約

	public String getText() {
		return getPropertyValue(P_TEXT).toString();
	}
	
	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddTextProperty(P_TEXT, P_TEXT, "Hello World");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "label";
	}
}
