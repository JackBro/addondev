package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.MyXYLayoutEditPolicy;
import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.figure.BoxFigure;
import gef.example.helloworld.figure.AbstractElementFigure;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.GridModel;
import gef.example.helloworld.model.RowModel;
import gef.example.helloworld.model.RowsModel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;


public class GridEditPart extends AbstractEditPartWithListener {

	List<IFigure> dummys = new ArrayList<IFigure>();
	@Override
	protected IFigure createFigure() {
		GridModel model = (GridModel)getModel();
		
		AbstractElementFigure fig = new BoxFigure();
		GridLayout gl = new GridLayout();
		gl.numColumns = model.getColumns().getChildren().size();
		fig.setBorder(new LineBorder(ColorConstants.black,1, Graphics.LINE_DOT));
		fig.setLayoutManager(gl);
		
		if(model.getChildren().size()==0){	
			for(int i=0; i<1; i++){
				Label label = new Label("cBox");
				label.setBorder(
				new CompoundBorder(new LineBorder(), new MarginBorder(1)));
				dummys.add(label);
			}
	
			for (IFigure dummy : dummys) {
				fig.add(dummy);
			}
		}
		return fig;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new BoxLayoutEditPolicy());
	
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILD)) {
			
			if(((ContentsModel) getModel()).getChildren().size()==1 ){
				getFigure().remove(dummys.get(0));
			} 
//			else if(((ContentsModel) getModel()).getChildren().size()==2){
//				getFigure().remove(dummys.get(1));
//			}
			
			GridModel grid = (GridModel)getModel();
			int rows = children.size()/grid.getColumns().getChildren().size();
			//if(grid.getRows().getChildren().size() < rows){
			int ss = ((RowsModel)grid.getPropertyValue(GridModel.ATTR_ROWS)).getChildren().size();
			if(((RowsModel)grid.getPropertyValue(GridModel.ATTR_ROWS)).getChildren().size()< rows){
				//grid.getRows().getChildren().add(new RowModel());
				((RowsModel)grid.getPropertyValue(GridModel.ATTR_ROWS)).getChildren().add(new RowModel());
			}
			refreshChildren();
			
		}else if(evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILD)){
			if(((ContentsModel) getModel()).getChildren().size()==0 ){
				getFigure().add(dummys.get(0));
			}
//			else if(((ContentsModel) getModel()).getChildren().size()==1 ){
//				getFigure().add(dummys.get(1));
//			}
			refreshChildren();
		} else if (evt.getPropertyName().equals(GridModel.ATTR_COLUMS)) {
			GridModel elem = (GridModel)getModel();
			int columns = elem.getColumns().getChildren().size();
			List<Integer> columlist = elem.getColumnFlex();
			
			List cheldern = getChildren();			
			if(cheldern.size() == 0) return;
			
			List<Integer> cwidths =  getResizedWidth(cheldern, columns, columlist);	
			
			int rows = cheldern.size()/columns;
			
			for (int j = 0; j < rows; j++) {
				for (int i = 0; i < columns; i++) {
					int index = j*rows+i;
					//ElementModel elm = (ElementModel)((EditPartWithListener)cheldern.get(index)).getModel();
					//ElementModel elm = (ElementModel)cheldern.get(index);
					//elm.setPreSize(cwidths.get(i), ph);
					AbstractElementFigure figuer = (AbstractElementFigure) ((AbstractEditPartWithListener)cheldern.get(index)).getFigure();
					figuer.setPreferredSize(new Dimension(cwidths.get(i), figuer.getDefaultHeight()));
				}
			}
	    }
	}
	
//	public void resizeColumns(){
//    	//EditPartWithListener ep = (EditPartWithListener)getParent();
//    	//ep.resizeChildren();
//		ArrayList<Integer> columlist = new ArrayList<Integer>(); 
//		GridModel elem = (GridModel)getModel();
//		String cflexs = elem.getPropertyValue(GridModel.COLUMS_FLEX).toString();
//		String[] cs = cflexs.split(",");
//		for (String string : cs) {
//			int flex = Integer.parseInt(string.trim());
//			columlist.add(flex);
//		}
//		
//		int pwidth = getFigure().getPreferredSize().width;
//		
//		
//		
//		
//		List cheldern = getChildren();
//	
//
//		int columns = 2;
//		int rows = cheldern.size()/columns;
//		
//		for (int j = 0; j < rows; j++) {
//			for (int i = 0; i < columns; i++) {
//				int index = j*rows+i;
//				ElementModel elm = (ElementModel)cheldern.get(index);
//				//elm.setPreSize(w, h)
//			}
//		}		
//	}
	
	public List<Integer> getResizedWidth(List children, int columnsize, List<Integer> flexs){
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		
		int w = getFigure().getSize().width;
		double sumflex=0;
		double sumzerofilexw=0;
		if(columnsize > children.size()) columnsize = children.size();
		//List children = getChildren();
		for (int i = 0; i < columnsize; i++) {
			Object object = children.get(i);
			AbstractElementModel elem = (AbstractElementModel)((AbstractEditPartWithListener)object).getModel();
			//int flex = Integer.parseInt(elem.getPropertyValue(ElementModel.ATTR_FLEX).toString());
			int flex = flexs.get(i);
			sumflex += flex;
			if(flex==0){
				AbstractElementFigure figuer = (AbstractElementFigure)((AbstractEditPartWithListener)object).getFigure();
				//figuer.setPreferredSize(figuer.getDefaultWidth(), figuer.getDefaultHeight());
				sumzerofilexw += figuer.getDefaultWidth();//figuer.getSize().width;
			}		
		}

		w -= sumzerofilexw;
		//for (Object object : children) {
		for (int i = 0; i < columnsize; i++) {
			Object object = children.get(i);
			AbstractElementModel elem = (AbstractElementModel)((AbstractEditPartWithListener)object).getModel();
			//int flex = Integer.parseInt(elem.getPropertyValue(ElementModel.ATTR_FLEX).toString());
			int flex = flexs.get(i);
			
			if(flex>0){
				int newwidth = (int) (flex/sumflex*w);
				
				res.add(newwidth);
				//figuer.setPreferredSize(new Dimension(newwidth, figuer.getSize().height));
			}else{
				AbstractElementFigure figuer = (AbstractElementFigure) ((AbstractEditPartWithListener)object).getFigure();
				res.add(figuer.getDefaultWidth());
			}
		}
		
		return res;
	}
}
