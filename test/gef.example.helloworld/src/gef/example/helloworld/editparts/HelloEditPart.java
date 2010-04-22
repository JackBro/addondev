package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.model.HelloModel;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class HelloEditPart extends EditPartWithListener {

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		HelloModel model = (HelloModel) getModel();

		Label label = new Label();
		label.setText(model.getText());

		// 外枠とマージンの設定
		label.setBorder(
			new CompoundBorder(new LineBorder(), new MarginBorder(3)));

		// 背景色をオレンジに
		label.setBackgroundColor(ColorConstants.orange);
		// 背景色を不透明に
		label.setOpaque(true);
		
		label.setSize(-1, -1);
		
		return label;
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(
			EditPolicy.COMPONENT_ROLE,
			new MyComponentEditPolicy());
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		// 制約の取得
//		Rectangle constraint = ((HelloModel) getModel()).getConstraint();
//
//		// Rectangleオブジェクトを制約としてビューに設定する
//		// setLayoutConstraintメソッドは親EditPartから呼び出す
//		((GraphicalEditPart) getParent()).setLayoutConstraint(
//			this,
//			getFigure(),
//			constraint);
	}

	/* (非 Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		// 変更の型がモデルの位置情報の変更を示すものかどうか
		if (evt.getPropertyName().equals(HelloModel.P_CONSTRAINT))
			refreshVisuals(); // ビューを更新する
	}

}
