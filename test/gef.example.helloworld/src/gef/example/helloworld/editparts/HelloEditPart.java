package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.HelloModel;
import gef.example.helloworld.model.VBoxModel;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class HelloEditPart extends EditPartWithListener {
	Figure rf;
	Label label;
	/* (非 Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		HelloModel model = (HelloModel) getModel();

		label = new Label();
		label.setText(model.getText());

		// 外枠とマージンの設定
		label.setBorder(
			new CompoundBorder(new LineBorder(), new MarginBorder(3)));

		// 背景色をオレンジに
		label.setBackgroundColor(ColorConstants.orange);
		// 背景色を不透明に
		label.setOpaque(true);
		
		//label.setSize(150, 50);
		//label.setPreferredSize(150, 30);
		
		rf=new Figure();
		rf.setBorder(
				new CompoundBorder(new LineBorder(), new MarginBorder(3)));		
		//rf.setSize(label.getPreferredSize());
		//rf.setPreferredSize(label.getPreferredSize());	
		//rf.setFill(true);
		ToolbarLayout tl = new ToolbarLayout();
		tl.setVertical(true);
		tl.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		tl.setStretchMinorAxis(false);
		rf.setLayoutManager(tl);
		rf.add(label);

		return rf;
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
		if (evt.getPropertyName().equals(HelloModel.P_CONSTRAINT))
			refreshVisuals(); // ビューを更新する
	    else if (evt.getPropertyName().equals(HelloModel.P_TEXT)) {
	        // モデルのテキストが変更されたのでビューに表示するテキストを更新
	        //Label label = (Label) getFigure();
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
}
