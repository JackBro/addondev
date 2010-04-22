package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.editpolicies.VBoxLayoutEditPolicy;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.HelloModel;
import gef.example.helloworld.model.VBoxModel;

import java.beans.PropertyChangeEvent;
import java.util.List;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

public class VBoxEditPart extends EditPartWithListener {
	//Layer figure;
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Figure figure = new Figure();
		//Layer figure = new Layer();
		LineBorder lb = new LineBorder();
		lb.setColor(ColorConstants.red);
		lb.setWidth(3);
		figure.setBorder(lb);
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
		//label.setText("model.getText()");

		// 外枠とマージンの設定
		label.setBorder(
			new CompoundBorder(new LineBorder(), new MarginBorder(3)));

		// 背景色をオレンジに
		label.setBackgroundColor(ColorConstants.orange);
		// 背景色を不透明に
		label.setOpaque(false);
		
		
		Insets padding = new Insets(5, 5, 5, 5);
		MarginBorder marginBorder = new MarginBorder(padding);
//		label.setBorder(marginBorder);
		GridLayout gl = new GridLayout();
		
		
		FlowLayout fl = new FlowLayout();
		ToolbarLayout tl = new ToolbarLayout();
		tl.setVertical(true);
		//tl.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		tl.setStretchMinorAxis(false);
		//fl.setStretchMinorAxis(true);
		fl.setHorizontal(false);
		figure.setLayoutManager(tl);
		figure.setBorder(marginBorder);

		figure.add(label);
		//figure.setConstraint(label, new Rectangle(2, 2, -1, -1));
		
		return figure;
	}

//	@Override
//	protected void registerVisuals() {
//		// TODO Auto-generated method stub
//		super.registerVisuals();
//		//getModelChildren().get(getModelChildren().size()-1);
////		Label label = new Label();
////		label.setText("model.getText()");
////		figure.add(label);
//	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new VBoxLayoutEditPolicy());
		//installEditPolicy(EditPolicy.COMPONENT_ROLE,new MyComponentEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals(VBoxModel.P_CHILDREN))
		{
//			Label label = new Label();
//			label.setText("model.getText()");
//			figure.add(label);
			//Object obj = evt.getNewValue();
			//figure.add((Label) obj);
			//refreshVisuals(); // ビューを更新する
			refreshChildren();
		}
	}
	
	protected List getModelChildren() {
		return ((VBoxModel) getModel()).getChildren();
	}

}
