package gef.example.helloworld.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import gef.example.helloworld.editpolicies.TabBoxLayoutEditPolicy;
import gef.example.helloworld.figure.TabPanelFigure;
import gef.example.helloworld.figure.TabBoxFigure;
import gef.example.helloworld.figure.TabPanelLineBorder;
import gef.example.helloworld.model.BoxModel;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.TabBoxModel;
import gef.example.helloworld.model.TabPanelModel;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public abstract class AbstractTabEditPart extends BoxEditPart {

	private HashMap<IFigure, Label> buttonMap = new HashMap<IFigure, Label>();
	
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
//		BoxModel model =  (BoxModel)getModel();
//		model.addChild(new TabPanelModel());
//		model.addChild(new TabPanelModel());
		
		return new TabBoxFigure();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		
//		if(evt.getPropertyName().equals("tabs")){
//			BoxModel model =  (BoxModel)getModel();
//			ElementModel tabpanels = (ElementModel)model.getPropertyValue(TabBoxModel.ATTR_TABS);
//			List list = (List)evt.getNewValue();
//			while(tabpanels.getChildren().size() > 0){
//				tabpanels.getChildren().remove(0);
//			}
//			
//			for (Object object : list) {
//				tabpanels.getChildren().add(object);
//			}
//			
//			model.removeAllChild();
//			for (Object object : list) {
//				model.addChild((ElementModel) object);
//			}
//		}
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TabBoxLayoutEditPolicy());
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// TODO Auto-generated method stub
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        
        Object constraint = BorderLayout.CENTER;
        //Object constraint = BorderLayout.BOTTOM;
        TabBoxFigure figure = getLayout();
        figure.getCanvas().add(child, constraint, index);
        //TabButton b = new TabButton((TabPanelFigure)child, childEditPart);
        Label b = getNewTab((TabPanelFigure)child, childEditPart);
        figure.getTabs().add(b, constraint, index);
        this.buttonMap.put(child, b);
        hideChilds();
        activateChilds(childEditPart);
        child.setVisible(true);
        childEditPart.refresh();
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		// TODO Auto-generated method stub
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        TabBoxFigure figure = getLayout();
        figure.getCanvas().remove(child);
        deactivateChilds(childEditPart);
        //TabButton b = this.buttonMap.get(child);
        Label b = this.buttonMap.get(child);
        figure.getTabs().remove(b);
        this.buttonMap.remove(child);
	}

	@Override
	protected void refreshVisuals() {
        refreshTabs();
        super.refreshVisuals();
	}
	
    public void refreshTabs() {
        Set<Entry<IFigure, Label>> entries = this.buttonMap.entrySet();
        for (Entry<IFigure, Label> entry : entries) {
        	Label button = entry.getValue();
            TabPanelFigure f = (TabPanelFigure)entry.getKey();
           
            String message = button.getText();
            button.setText(message);
        }
    }
    
    protected TabBoxFigure getLayout() {
        return (TabBoxFigure) getContentPane();
    }
    
    protected void hideChilds() {
        List<IFigure> childs = getLayout().getCanvas().getChildren();
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setVisible(false);
        }
        List<EditPart> eps = getChildren();
        for (int i = 0; i < eps.size(); i++) {
            deactivateChilds(eps.get(i));
            eps.get(i).setFocus(false);
        }
    }
    
    protected void enableOtherButtons() {
    	TabBoxFigure figure = (TabBoxFigure) getContentPane();
        List<IFigure> tabs = figure.getTabs().getChildren();
        for (IFigure tab : tabs) {
        	Label button = (Label) tab;
            button.setEnabled(true);
            button.setBackgroundColor(ColorConstants.lightGray);
            button.validate();
            //button.setBorder(deselectedBorder);
            //button.update();
            button.repaint();
        }
    }
    
    protected void activateChilds(EditPart parent) {
        List<EditPart> childs = parent.getChildren();
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).activate();
        }
    }

    protected void deactivateChilds(EditPart parent) {
        List<EditPart> childs = parent.getChildren();
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).deactivate();
        }
    }

    protected abstract Label getNewTab(TabPanelFigure child, EditPart childEditPart);
}
