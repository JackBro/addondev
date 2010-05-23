package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.RootXYLayoutEditPolicy;
import gef.example.helloworld.model.ContentsModel;

import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPolicy;

public class XULRootEditPart extends AbstractEditPartWithListener {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Layer figure = new Layer();
		figure.setBorder(new MarginBorder(3));
		
		ToolbarLayout tl = new ToolbarLayout();
		FlowLayout fl = new FlowLayout();
		//tl.setStretchMinorAxis(true);
		//tl.setVertical(true);
		//tl.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
		//figure.setLayoutManager(tl);	
		figure.setLayoutManager(fl);
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootXYLayoutEditPolicy((ContentsModel)getModel()));
	}

}
