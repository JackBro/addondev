package gef.example.helloworld.editparts;

import java.beans.PropertyChangeEvent;

import gef.example.helloworld.figure.CheckGroupBoxBorder;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.GroupBoxModel;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

public class GroupBoxEditPart extends BoxEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		GroupBoxModel model = (GroupBoxModel)getModel();
		
		IFigure figure = super.createFigure();
//		IFigure figure = new Figure();
//		figure.setLayoutManager(new BorderLayout());
//		Label label = new Label("text");
//		label.setTextAlignment(PositionConstants.LEFT);
//		figure.add(label, BorderLayout.LEFT);
		CheckGroupBoxBorder border = new CheckGroupBoxBorder();
		//border.setLabel(model.getName());
		border.setChekBox(model.isCheckBox());
		border.setLabel(model.getText());
		//border.paint(label, g, insets)
		figure.setBorder(border);

		return figure;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(GroupBoxModel.CAPTION)){
			String text = ((GroupBoxModel)getModel()).getText();
			((CheckGroupBoxBorder)getFigure().getBorder()).setLabel(text);
			getFigure().repaint();
		}else if(evt.getPropertyName().equals(GroupBoxModel.CHECKBOX)){
			boolean checkbox = ((GroupBoxModel)getModel()).isCheckBox();
			((CheckGroupBoxBorder)getFigure().getBorder()).setChekBox(checkbox);
			getFigure().repaint();
		}
	}

}
