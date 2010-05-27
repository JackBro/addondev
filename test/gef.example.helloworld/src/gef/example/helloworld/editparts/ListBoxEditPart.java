package gef.example.helloworld.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.figure.AbstractElementFigure;
import gef.example.helloworld.figure.BoxFigure;
import gef.example.helloworld.figure.LabelFigure;
import gef.example.helloworld.model.ListBoxModel;
import gef.example.helloworld.model.ListCellModel;
import gef.example.helloworld.model.ListItemModel;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPolicy;

public class ListBoxEditPart extends BoxEditPart {
	
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
		fig.setLayoutManager(gl);
		
		ToolbarLayout tl = new ToolbarLayout();
		tl.setVertical(true);
		tl.setStretchMinorAxis(true);
		fig.setLayoutManager(tl);
		
		fig.setPreferredSize(100, 22);
		
		return fig;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(ListBoxModel.LISTCOLS)){
			
			ListBoxModel listboxmodel = (ListBoxModel)getModel();
			
			List cols = (List) evt.getNewValue();
			
			listboxmodel.getListcols().setChildren(cols);
			
			int numcolum = cols.size()==0?1:cols.size();
			//((GridLayout)getFigure().getLayoutManager()).numColumns = numcolum;	
			
			IFigure listbox = getFigure();
			int width = listbox.getSize().width;
			List<ListItemModel> items = listboxmodel.getListitems();
			
			listbox.getChildren().clear();
			for (int i = 0; i < items.size(); i++) {
				BoxFigure box = new BoxFigure();
				box.setLayoutManager(new ToolbarLayout(true));
				//int newwidth = width/numcolum;
				//box.setPreferredSize(newwidth, listbox.getSize().height);
				listbox.add(box);
				ListItemModel item = items.get(i);
							
				
				if(numcolum > 1){
					List<ListCellModel>cells = item.getListcells();
					for (ListCellModel cell : cells) {
						LabelFigure label = new LabelFigure();
						label.setText(cell.getText());
						box.add(label);
						int newwidth = width/numcolum;
						label.setPreferredSize(newwidth, label.getDefaultHeight());						
					}
				}else{
				//for (ListItemModel item : items) {
					LabelFigure label = new LabelFigure();
					label.setText(items.get(i).getText());
					box.add(label);
					int newwidth = width/numcolum;
					label.setPreferredSize(newwidth, label.getDefaultHeight());
				//}	
				}
			}

//			List<ListItemModel> items = listboxmodel.getListitems();
//			for (ListItemModel item : items) {
//				LabelFigure label = new LabelFigure();
//				label.setText(item.getText());
//				listbox.add(label);
//				int newwidth = width/numcolum;
//				label.setPreferredSize(newwidth, label.getDefaultHeight());
//			}
			
		}
		else if(evt.getPropertyName().equals(ListBoxModel.LISTITEMS)){
			ListBoxModel listboxmodel = (ListBoxModel)getModel();
			List<ListItemModel> items = (List) evt.getNewValue();
			listboxmodel.setListitems(items);
			
			int numcolum = listboxmodel.getListcols().getChildren().size()==0?1:listboxmodel.getListcols().getChildren().size();
			//((GridLayout)getFigure().getLayoutManager()).numColumns = numcolum;		
			
			IFigure listbox = getFigure();
			int width = listbox.getSize().width;
			//List<ListItemModel> items = listboxmodel.getListitems();
			
			listbox.getChildren().clear();
			for (int i = 0; i < items.size(); i++) {
				BoxFigure box = new BoxFigure();
				box.setLayoutManager(new ToolbarLayout(true));
				//int newwidth = width/numcolum;
				//box.setPreferredSize(newwidth, listbox.getSize().height);
				listbox.add(box);
				ListItemModel item = items.get(i);
							
				
				if(numcolum > 1){
					List<ListCellModel>cells = item.getListcells();
					for (ListCellModel cell : cells) {
						LabelFigure label = new LabelFigure();
						label.setText(cell.getText());
						box.add(label);
						int newwidth = width/numcolum;
						label.setPreferredSize(newwidth, label.getDefaultHeight());						
					}
				}else{
				//for (ListItemModel item : items) {
					LabelFigure label = new LabelFigure();
					label.setText(items.get(i).getText());
					box.add(label);
					int newwidth = width/numcolum;
					label.setPreferredSize(newwidth, label.getDefaultHeight());
				//}	
				}
			}
			
//			IFigure listbox = getFigure();
//			
//			listbox.getChildren().clear();
////			for (Object object : cols) {
////				LabelFigure colum = new LabelFigure();
////				object
////			}
//			int width = listbox.getSize().width;
//			//List<ListItemModel> items = listboxmodel.getListitems();
//			for (ListItemModel item : items) {
//				LabelFigure label = new LabelFigure();
//				label.setText(item.getText());
//				listbox.add(label);
//				int newwidth = width/numcolum;
//				label.setPreferredSize(newwidth, label.getDefaultHeight());
//			}			
		}
	}

	@Override
	public IFigure getMain() {
		// TODO Auto-generated method stub
		return getFigure();
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		//super.createEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new MyComponentEditPolicy());
	}
	
	
}
