package gef.example.helloworld.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.figure.AbstractElementFigure;
import gef.example.helloworld.figure.BoxFigure;
import gef.example.helloworld.figure.LabelFigure;
import gef.example.helloworld.model.ListBoxModel;
import gef.example.helloworld.model.ListCellModel;
import gef.example.helloworld.model.ListColModel;
import gef.example.helloworld.model.ListItemModel;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPolicy;

public class ListBoxEditPart extends AbstractElementEditPart {

	private Figure top;
	private Figure center;

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		AbstractElementFigure fig = new BoxFigure();

		Figure header = new Figure();

		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 0;
		gl.horizontalSpacing = 0;
		gl.makeColumnsEqualWidth = true;
		gl.numColumns = 1;
		// fig.setLayoutManager(gl);

		ToolbarLayout tl = new ToolbarLayout();
		tl.setVertical(false);
		tl.setStretchMinorAxis(true);
		// fig.setLayoutManager(tl);

		BorderLayout bl = new BorderLayout();
		fig.setLayoutManager(bl);

		top = new Figure();
		top.setOpaque(true);
		top.setLayoutManager(tl);
		center = new Figure();
		center.setBackgroundColor(ColorConstants.white);
		center.setOpaque(true);

		fig.add(top, BorderLayout.TOP);
		fig.add(center, BorderLayout.CENTER);

		fig.setPreferredSize(100, 100);
		
		upDateFigure();

		return fig;
	}

	protected void upDateFigure() {
		
		ListBoxModel listboxmodel = (ListBoxModel) getModel();
		listboxmodel.upDateCol();
		
		List cols = listboxmodel.getListcols().getChildren();
		
		int numcolum = cols.size();
		top.removeAll();
		for (int i = 0; i < numcolum; i++) {
			if (((ListColModel) cols.get(i)).isHeader()) {
				Figure header = new Figure();
				header.setBackgroundColor(ColorConstants.lightGray);
				header.setOpaque(true);
				header.setBorder(new SimpleRaisedBorder());
				header.setPreferredSize(100, 20);
				top.add(header);
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		if (evt.getPropertyName().equals(ListBoxModel.LISTCOLS)) {

			List cols = (List) evt.getNewValue();
			ListBoxModel listboxmodel = (ListBoxModel) getModel();
			listboxmodel.getListcols().setChildren(cols);
			listboxmodel.upDate();

			int numcolum = listboxmodel.getListcols().getChildren().size();
			top.removeAll();
			// if(listboxmodel.isHeader()){
			for (int i = 0; i < numcolum; i++) {
				if (((ListColModel) cols.get(i)).isHeader()) {
					Figure header = new Figure();
					header.setBackgroundColor(ColorConstants.lightGray);
					header.setOpaque(true);
					header.setBorder(new SimpleRaisedBorder());
					header.setPreferredSize(100, 20);
					top.add(header);
				}
			}

		}

	}
}
