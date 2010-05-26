package gef.example.helloworld.editparts;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.figure.BoxFigure;
import gef.example.helloworld.figure.AbstractElementFigure;
import gef.example.helloworld.model.BoxModel;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.AbstractElementModel;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class BoxEditPart extends AbstractContentsEditPart {

	private Figure dummy=null;
	@Override
	protected IFigure createFigure() {
		
		AbstractElementModel model = (AbstractElementModel)getModel();
		
		AbstractElementFigure figure = new BoxFigure();
		
		ToolbarLayout tl = new ToolbarLayout();
		tl.setSpacing(5);	
		tl.setVertical(isVertical());
		tl.setStretchMinorAxis(true);
		figure.setLayoutManager(tl);
		
		if(model.getChildren().size() == 0){
			dummy  = new Figure();
			dummy.setPreferredSize(10, 20);		
			figure.add(dummy);
		}
		
		model.setStyle(figure);
		
		return figure;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		//super.propertyChange(evt);
		
		if (evt.getPropertyName().equals(ContentsModel.P_ADD_CHILD))
		{
			if(getFigure().getChildren().size() > 0 && dummy != null){
				getFigure().getChildren().remove(dummy);
			}
			refreshChildren();
		}else if(evt.getPropertyName().equals(ContentsModel.P_REMOVE_CHILD)){
			ContentsModel elm = (ContentsModel)getModel();
			if(elm.getChildren().size() == 0 && dummy != null){
				getFigure().add(dummy);	
			}			
			refreshChildren();
		}else 
		if(evt.getPropertyName().equals(BoxModel.ATTR_ORIENT)){
			ToolbarLayout tl = (ToolbarLayout) getFigure().getLayoutManager();
			tl.setVertical(isVertical());
			getFigure().validate();
		}else if(evt.getPropertyName().equals(AbstractElementModel.ATTR_FLEX)){
	    	AbstractEditPartWithListener ep = (AbstractEditPartWithListener)getParent();
	    	//ep.resizeWidth();
	    	ep.resize();
		}
	}

	protected boolean isVertical(){
		String orient = ((BoxModel)getModel()).getPropertyValue(BoxModel.ATTR_ORIENT).toString();
		return orient.equalsIgnoreCase("vertical");		
	}
	
	@Override
	public IFigure getBottom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFigure getMain() {
		// TODO Auto-generated method stub
		return getFigure();
	}

	@Override
	public IFigure getTop() {
		// TODO Auto-generated method stub
		return null;
	}

	interface Function1{
		public int getFigureSize(IFigure figure);
		public int getFigurePreferredSize(IFigure figure);
		public void setFigurePreferredSize(IFigure figure, int newsize);
	}
	
	@Override
	public void resize() {
		// TODO Auto-generated method stub
		Function1 func = null;
		if(isVertical()){
			func = new Function1() {
				
				@Override
				public void setFigurePreferredSize(IFigure figure, int newsize) {
					// TODO Auto-generated method stub
					figure.setPreferredSize(new Dimension(figure.getSize().width, newsize));
				}
				
				@Override
				public int getFigurePreferredSize(IFigure figure) {
					// TODO Auto-generated method stub
					return figure.getPreferredSize().height;
				}

				@Override
				public int getFigureSize(IFigure figure) {
					// TODO Auto-generated method stub
					return figure.getSize().height;
				}
			};	
		}else{

			func = new Function1() {	
				@Override
				public void setFigurePreferredSize(IFigure figure, int newsize) {
					figure.setPreferredSize(new Dimension(newsize, figure.getSize().height));
				}
				
				@Override
				public int getFigurePreferredSize(IFigure figure) {
					return figure.getPreferredSize().width;
				}

				@Override
				public int getFigureSize(IFigure figure) {
					// TODO Auto-generated method stub
					return figure.getSize().width;
				}
			};
		}
		_resize(func);	
	}
	
	private void _resize(Function1 func){
		//int w = ((AbstractGraphicalEditPart)getParent()).getFigure().getPreferredSize().width;
		//int w2 = getFigure().getSize().width;
		int w = func.getFigureSize(getFigure());
		double sumflex=0;
		double sumzerofilexw=0;

		//TODO
		List children = getChildren();	
		
		for (Object object : children) {
			AbstractElementModel elem = (AbstractElementModel)((AbstractEditPartWithListener)object).getModel();
			if(!elem.isVisible()) continue;
			//Object jjj = elem.getPropertyValue(ElementModel.ATTR_FLEX);
			String i = (String) elem.getPropertyValue(AbstractElementModel.ATTR_FLEX);
			if(i==null) i="";
			i=i.length()==0?"0":i;
			//int flex = Integer.parseInt(elem.getPropertyValue(ElementModel.ATTR_FLEX).toString());
			int flex = Integer.parseInt(i);
			sumflex += flex;
			if(flex==0){
				AbstractElementFigure figuer = (AbstractElementFigure)((AbstractEditPartWithListener)object).getFigure();
				figuer.setPreferredSize(figuer.getDefaultWidth(), figuer.getDefaultHeight());
				//sumzerofilexw += figuer.getSize().width;
				sumzerofilexw += func.getFigurePreferredSize(figuer);
			}
		}
		w -= sumzerofilexw;
		for (Object object : children) {
			AbstractElementModel elem = (AbstractElementModel)((AbstractEditPartWithListener)object).getModel();
			if(!elem.isVisible()) continue;
			//int flex = Integer.parseInt(elem.getPropertyValue(ElementModel.ATTR_FLEX).toString());
			String i = (String) elem.getPropertyValue(AbstractElementModel.ATTR_FLEX);
			if(i==null) i="";
			i=i.length()==0?"0":i;
			int flex = Integer.parseInt(i);
			if(flex>0){
				int newsize = (int) (flex/sumflex*w);
				IFigure figuer = ((AbstractEditPartWithListener)object).getFigure();
				//figuer.setPreferredSize(new Dimension(newwidth, figuer.getSize().height));
				func.setFigurePreferredSize(figuer, newsize);
				//((EditPartWithListener)object).resizeWidth();
				((AbstractEditPartWithListener)object).resize();
			}
		}			
	}
}
