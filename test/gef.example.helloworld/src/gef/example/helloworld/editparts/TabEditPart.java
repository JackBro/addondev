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

public abstract class TabEditPart extends EditPartWithListener {

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
//		if(evt.getPropertyName().equals(TabBoxModel.ATTR_TABS)){
//			BoxModel model =  (BoxModel)getModel();
//			model.addChild(new TabPanelModel());
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
    
//    public List getModelChildren() {
//        return ((TabPanel)getModel()).getTabs();
//    }
    
    protected abstract Label getNewTab(TabPanelFigure child, EditPart childEditPart);
    
//    public class TabButton extends Label implements MouseListener {
//        private TabPanelFigure pane;
//        //private IFigure pane;
//        private EditPart childEditPart;
//        //private Label tabLabel;
//        
//        public TabButton(TabPanelFigure pane, EditPart childEditPart) {
//            this.pane = pane;
//            this.childEditPart = childEditPart;
//            //TabModel paneModel = pane.getModel();
//            setOpaque(true);         
//            setBorder(new TabPanelLineBorder());
//            
//            ((TabPanelModel)childEditPart.getModel()).addPropertyChangeListener(new PropertyChangeListener() {
//				
//				@Override
//				public void propertyChange(PropertyChangeEvent arg0) {
//					// TODO Auto-generated method stub
//					refreshTabs();
//				}
//			});
//            
//            String message = ((TabPanelModel)childEditPart.getModel()).getTabLabel();
//            
//            setText(message);
//            addMouseListener(this);
//
//            setBackgroundColor(ColorConstants.lightGray);
//            setForegroundColor(ColorConstants.black);
//            enableOtherButtons();
//
//        }
//
//        @Override
//		public void setText(String s) {
//			// TODO Auto-generated method stub
//			super.setText(s);
//			//((TabPanelModel)childEditPart.getModel()).a
//		}
//
//		@Override
//		public String getText() {
//			// TODO Auto-generated method stub
//        	String message = ((TabPanelModel)childEditPart.getModel()).getTabLabel();
//			return message;
//		}
//
//		protected void showEp() {
//            activateChilds(this.childEditPart);
//        }
//        
//        public void update() {
//            String message = getText();
//            //this.tabLabel.setText(message);
//            this.setText(message);
//        }
//
//		@Override
//		public void mouseDoubleClicked(MouseEvent me) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void mousePressed(MouseEvent me) {
//			// TODO Auto-generated method stub
//            hideChilds();
//            showEp();
//            this.pane.setVisible(true);
//            enableOtherButtons();
//            setBackgroundColor(ColorConstants.white);
//            setForegroundColor(ColorConstants.black);
//            //setBorder(selectedBorder);
//            getLayout().repaint();			
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent me) {
//			// TODO Auto-generated method stub
//			
//		}
//
//    }
}
