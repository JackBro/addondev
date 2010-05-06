package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.figure.LabelFigure;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.LabelModel;
import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPolicy;

public class LabelEditPart extends EditPartWithListener {
	/* (非 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		LabelModel model = (LabelModel) getModel();

		LabelFigure label = new LabelFigure();
		label.setText(model.getText());
		
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

//	/* (非 Javadoc)
//	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
//	 */
//	protected void refreshVisuals() {
//		// 制約の取得
////		Rectangle constraint = ((HelloModel) getModel()).getConstraint();
////
////		// Rectangleオブジェクトを制約としてビューに設定する
////		// setLayoutConstraintメソッドは親EditPartから呼び出す
////		((GraphicalEditPart) getParent()).setLayoutConstraint(
////			this,
////			getFigure(),
////			constraint);
//		
//		((GraphicalEditPart) getParent()).setLayoutConstraint(
//	}

	/* (非 Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		// 変更の型がモデルの位置情報の変更を示すものかどうか
		if (evt.getPropertyName().equals(LabelModel.P_CONSTRAINT))
			refreshVisuals(); // ビューを更新する
	    else if (evt.getPropertyName().equals(LabelModel.P_TEXT)) {
	        // モデルのテキストが変更されたのでビューに表示するテキストを更新
	    	LabelFigure label = (LabelFigure) getFigure();
	        label.setText((String) evt.getNewValue());
	        //ToolbarLayout tl = new ToolbarLayout();
	        //tl.setStretchMinorAxis(false);
	        //Rectangle rg = label.getBounds();
	        //rg.width += 30; 
//	        label.setBounds(rg);
//	        refreshVisuals(); // ビューを更新する
	        //label.setSize( 300, rg.height);
	        //refreshVisuals();
	        
	    	//AbstractGraphicalEditPart ep = (AbstractGraphicalEditPart)getParent();

	        
	        //rf.setSize(label.getSize());
	        //rf.setPreferredSize(label.getSize());

	        //((EditPartWithListener)getParent()).propertyChange(new PropertyChangeEvent(this, VBoxModel.P_CHILDREN, null, null));
	        //rf.repaint();
	        //getFigure().setBounds(rg);
	        //label.setSize(label.getSize().width+20 , label.getSize().height);
	    }
	    else if (evt.getPropertyName().equals(ElementModel.ATTR_FLEX)) {
	    	EditPartWithListener ep = (EditPartWithListener)getParent();
	    	ep.resizeChildren();
	    }
	}

	@Override
	protected void refreshVisuals() {
		// TODO Auto-generated method stub
		super.refreshVisuals();

	}

	@Override
	public void resizeWidth() {
		// TODO Auto-generated method stub
		super.resizeWidth();
	}
	
	
}
