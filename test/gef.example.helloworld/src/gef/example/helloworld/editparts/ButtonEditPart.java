package gef.example.helloworld.editparts;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

import gef.example.helloworld.figure.LabelFigure;

public class ButtonEditPart extends AbstractElementEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
//		Button button = new Button();
//		ToolbarLayout tl = new ToolbarLayout();
//		tl.setStretchMinorAxis(false);
//		tl.setVertical(true);
//		//button.setLayoutManager(tl);
//		button.setLayoutManager(new BorderLayout());
//		
//		Insets padding = new Insets(0, 0, 0, 0);
//		MarginBorder marginBorder = new MarginBorder(padding);
		//button.setBorder(marginBorder);
		
		LabelFigure label = new LabelFigure();
		label.setBorder(new SimpleRaisedBorder());
		label.setText("button");
		//button.add(label, BorderLayout.CENTER);
		return label;
	}

}
