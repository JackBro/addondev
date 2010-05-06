package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.VBoxLayoutEditPolicy;
import gef.example.helloworld.figure.ElementFigure;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.ElementModel;
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

		
		Figure figure = new Figure();
		//Layer figure = new Layer();
		LineBorder lb = new LineBorder();
		lb.setColor(ColorConstants.blue);
		lb.setWidth(3);
		figure.setBorder(new LineBorder(ColorConstants.black,1, Graphics.LINE_DOT));
		//figure.setOpaque(true);
//		//figure.setPreferredSize(300, 100);
//		figure.setBackgroundColor(ColorConstants.blue);
//
//		
//		//figure.setOpaque(opaque)
//		FlowLayout fl = new FlowLayout();
//		fl.setStretchMinorAxis(true);
//		fl.setHorizontal(true);
//		figure.setLayoutManager(fl);
//		
//		return figure;
		Label label = new Label();
		label.setText("H");

		// 外枠とマージンの設定
		label.setBorder(
			new CompoundBorder(new LineBorder(), new MarginBorder(3)));

		// 背景色をオレンジに
		label.setBackgroundColor(ColorConstants.orange);
		// 背景色を不透明に
		label.setOpaque(false);
		
		FlowLayout fl = new FlowLayout();
		ToolbarLayout tl = new ToolbarLayout();
		tl.setSpacing(10);
		tl.setVertical(false);
		
		Insets padding = new Insets(5, 5, 5, 5);
		MarginBorder marginBorder = new MarginBorder(padding);
		figure.setBorder(marginBorder);
		//tl.setStretchMinorAxis(false);
		//fl.setStretchMinorAxis(true);
		//fl.setHorizontal(false);
		//figure.setSize(10, 20);
		figure.setLayoutManager(tl);
		
		dummy  = new Figure();
		dummy.setPreferredSize(10, 20);		
		figure.add(dummy);
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new VBoxLayoutEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILDREN))
		{
			ContentsModel elm = (ContentsModel)getModel();
			if(getFigure().getChildren().size() > 0){
				int i = getFigure().getChildren().size();
				IFigure ff = getFigure();
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
