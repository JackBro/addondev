package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.figure.LabelFigure;
import gef.example.helloworld.model.AbstractElementModel;
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

public class LabelEditPart extends AbstractElementEditPart {

	protected IFigure createFigure() {
		LabelModel model = (LabelModel) getModel();

		LabelFigure label = new LabelFigure();
		label.setText(model.getText());
		
		return label;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	    if (evt.getPropertyName().equals(LabelModel.VALUE)) {
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
	    super.propertyChange(evt);
	}

	@Override
	protected void refreshVisuals() {
		// TODO Auto-generated method stub
		super.refreshVisuals();
    	LabelFigure label = (LabelFigure) getFigure();
    	LabelModel model = (LabelModel) getModel();
    	label.setText(model.getText());
	}
}
