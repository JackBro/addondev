package gef.example.helloworld.model;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class LabelModel extends ElementModel {
	// 変更の種類を識別するための文字列
	public static final String P_CONSTRAINT = "_constraint";
	public static final String P_TEXT = "value";

	private String text = "Hello World";
	private Rectangle constraint; // 制約

	public String getText() {
		return text;
	}

	// public void setText(String text) {
	// this.text = text;
	// }
//	public void setText(String text) {
//		this.text = text;
//		// テキストの変更をEditPartに通知する
//		firePropertyChange(P_TEXT, null, text); // ②
//	}
//	public Rectangle getConstraint() {
//		return constraint;
//	}
//
//	public void setConstraint(Rectangle rect) {
//		constraint = rect;
//		// 変更の通知
//		firePropertyChange(P_CONSTRAINT, null, constraint);
//	}
	
	public LabelModel() {
		super();
		// TODO Auto-generated constructor stub
		AddProperty(P_TEXT, new TextPropertyDescriptor(P_TEXT, "あいさつ"), text);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "label";
	}
	
//	@Override
//	public IPropertyDescriptor[] getPropertyDescriptors() {
//	    IPropertyDescriptor[] descriptors =
//	        new IPropertyDescriptor[] {
//	           new TextPropertyDescriptor(P_TEXT, "あいさつ")};
//	      return descriptors;
//	}
//
//
//
//	@Override
//	public Object getPropertyValue(Object id) {
//	    if (id.equals(P_TEXT)) {
//	        // プロパティ・ビューに表示するデータを返す
//	        return text;
//	      }
//	      return null;
//	}
//
//	@Override
//	public boolean isPropertySet(Object id) {
//	    if (id.equals(P_TEXT))
//	        return true;
//	      else
//	        return false;
//	}
//
//	@Override
//	public void setPropertyValue(Object id, Object value) {
//	    if (id.equals(P_TEXT)) {
//	        // テキストを変更する
//	        setText((String) value);
//	      }
//	}
}
