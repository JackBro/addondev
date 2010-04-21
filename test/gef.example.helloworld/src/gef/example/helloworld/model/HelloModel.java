package gef.example.helloworld.model;

import org.eclipse.draw2d.geometry.Rectangle;

public class HelloModel extends ElementModel {
	// 変更の種類を識別するための文字列
	public static final String P_CONSTRAINT = "_constraint";

	private String text = "Hello World";
	private Rectangle constraint; // 制約

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setConstraint(Rectangle rect) {
		constraint = rect;
		// 変更の通知
		firePropertyChange(P_CONSTRAINT, null, constraint);
	}
}
