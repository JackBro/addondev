package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.figure.BoxFigure;
import gef.example.helloworld.figure.ElementFigure;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.GridModel;
import gef.example.helloworld.model.HBoxModel;
import gef.example.helloworld.model.VBoxModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPolicy;

public class HBoxEditPart extends EditPartWithListener {

	private Figure dummy;
	@Override
	protected IFigure createFigure() {
//		figure.setBorder(new LineBorder(ColorConstants.black,1, Graphics.LINE_DOT));
		ElementModel model = (ElementModel)getModel();

		BoxFigure figure = new BoxFigure();
		
		ToolbarLayout tl = new ToolbarLayout();
		tl.setSpacing(5);
		tl.setVertical(false);
		figure.setLayoutManager(tl);

		if(model.getChildren().size() == 0){
			dummy  = new Figure();
			dummy.setPreferredSize(10, 20);		
			figure.add(dummy);
		}
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new BoxLayoutEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILDREN))
		{
			ContentsModel elm = (ContentsModel)getModel();
			if(getFigure().getChildren().size() > 0){
				getFigure().getChildren().remove(dummy);
				
			}
			refreshChildren();
		}else if(evt.getPropertyName().equals(VBoxModel.P_REMOVE_CHILDREN)){
			
			
			ContentsModel elm = (ContentsModel)getModel();
			if(elm.getChildren().size() == 0){
				getFigure().add(dummy);
				
			}			
			refreshChildren();
		}else if(evt.getPropertyName().equals(VBoxModel.ATTR_FLEX)){
			//resizeWidth();
	    	EditPartWithListener ep = (EditPartWithListener)getParent();
	    	ep.resizeWidth();
		}
	}

	@Override
	public void resizeChildren() {
		// TODO Auto-generated method stub
		super.resizeChildren();
		
		int w = getFigure().getSize().width;
		double sumflex=0;
		double sumzerofilexw=0;
		
		List children = getChildren();
		for (Object object : children) {
			ElementModel elem = (ElementModel)((EditPartWithListener)object).getModel();
			int flex = Integer.parseInt(elem.getPropertyValue(ElementModel.ATTR_FLEX).toString());
			sumflex += flex;
			if(flex==0){
				ElementFigure figuer = (ElementFigure)((EditPartWithListener)object).getFigure();
				//figuer.setSize(figuer.getDefaultWidth(), figuer.getDefaultHeight());
				figuer.setPreferredSize(figuer.getDefaultWidth(), figuer.getDefaultHeight());
				sumzerofilexw += figuer.getSize().width;
			}
		}
		w -= sumzerofilexw;
		for (Object object : children) {
			ElementModel elem = (ElementModel)((EditPartWithListener)object).getModel();
			int flex = Integer.parseInt(elem.getPropertyValue(ElementModel.ATTR_FLEX).toString());
			if(flex>0){
				int newwidth = (int) (flex/sumflex*w);
				IFigure figuer = ((EditPartWithListener)object).getFigure();
				figuer.setPreferredSize(new Dimension(newwidth, figuer.getSize().height));
			}
		}
	}

}
