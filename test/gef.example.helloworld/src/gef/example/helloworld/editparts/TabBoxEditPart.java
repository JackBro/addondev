package gef.example.helloworld.editparts;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import gef.example.helloworld.editpolicies.BoxLayoutEditPolicy;
import gef.example.helloworld.editpolicies.MyComponentEditPolicy;
import gef.example.helloworld.editpolicies.TabBoxLayoutEditPolicy;
import gef.example.helloworld.figure.TabPanelFigure;
import gef.example.helloworld.figure.TabBoxFigure;
import gef.example.helloworld.model.TabBoxModel;
import gef.example.helloworld.model.TabPanelModel;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class TabBoxEditPart extends EditPartWithListener {

	private HashMap<IFigure, TabButton> buttonMap = 
        new HashMap<IFigure, TabButton>();
	
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return new TabBoxFigure();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
//		if(evt.getPropertyName().equals(TabBoxModel.ATTR_TABS)){
//			EditPartWithListener edit = new TabPanelEditPart();
//			edit.setModel(new TabPanelModel());
//			int s = getChildren().size();
//			int ms = getModelChildren().size();
//			//activateChilds(edit);
//			int ss = this.buttonMap.size();
//			addChild(edit, ss);
//
//			//addChildVisual(edit, ss);
//			//addChild(child, index)
//		}
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TabBoxLayoutEditPolicy());
		//installEditPolicy(EditPolicy.COMPONENT_ROLE, new MyComponentEditPolicy());
	}

	@Override
	protected void addChild(EditPart child, int index) {
		// TODO Auto-generated method stub
		super.addChild(child, index);
        
//		IFigure childfigure = ((GraphicalEditPart) child).getFigure();
//        //Object constraint = BorderLayout.CENTER;
//        //TabBoxFigure figure = getLayout();
//        //figure.getCanvas().add(childfigure, constraint, index);
//        TabButton b = new TabButton((TabPanelFigure)childfigure, child);
//        //figure.getTabs().add(b, constraint, index);
//        this.buttonMap.put(childfigure, b);
////        hideChilds();
////        activateChilds(child);
////        childfigure.setVisible(true);
////        child.refresh();		
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// TODO Auto-generated method stub
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        Object constraint = BorderLayout.CENTER;
        TabBoxFigure figure = getLayout();
        figure.getCanvas().add(child, constraint, index);
        TabButton b = new TabButton((TabPanelFigure)child, childEditPart);
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
        TabButton b = this.buttonMap.get(child);
        figure.getTabs().remove(b);
        this.buttonMap.remove(child);
	}

	@Override
	protected void refreshVisuals() {
        refreshTabs();
        super.refreshVisuals();
	}
	
    public void refreshTabs() {
        Set<Entry<IFigure, TabButton>> entries = this.buttonMap.entrySet();
        for (Entry<IFigure, TabButton> entry : entries) {
            TabButton button = entry.getValue();
            TabPanelFigure f = (TabPanelFigure)entry.getKey();
            String message = "put your";
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
            TabButton button = (TabButton) tab;
            button.setEnabled(true);
            button.setBackgroundColor(ColorConstants.lightGray);
            button.validate();
            //button.setBorder(deselectedBorder);
            button.update();
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
    
    
    //public class TabButton extends Button implements ActionListener {
    public class TabButton extends Label implements MouseListener {
        private TabPanelFigure pane;
        //private IFigure pane;
        private EditPart childEditPart;
        //private Label tabLabel;

        public TabButton(TabPanelFigure pane, EditPart childEditPart) {
            this.pane = pane;
            this.childEditPart = childEditPart;
            //TabModel paneModel = pane.getModel();
            setOpaque(true);
            String message = "panel";
            //this.tabLabel = new Label(message);
            setText(message);
            //setContents(this.tabLabel);
            //addActionListener(this);
            //setBorder(selectedBorder);
            addMouseListener(this);

            setBackgroundColor(ColorConstants.lightGray);
            setForegroundColor(ColorConstants.black);
            enableOtherButtons();

        }

        public void actionPerformed(ActionEvent event) {
            hideChilds();
            showEp();
            this.pane.setVisible(true);
            enableOtherButtons();
            setBackgroundColor(ColorConstants.lightGray);
            setForegroundColor(ColorConstants.red);
            //setBorder(selectedBorder);
            getLayout().repaint();

        }

        protected void showEp() {
            activateChilds(this.childEditPart);
        }
        
        public void update() {
            String message = "pane";
            //this.tabLabel.setText(message);
            this.setText(message);
        }
        
//        public void setText(String text) {
//            //this.tabLabel.setText(text);
//        	this.setText(text);
//        }

		@Override
		public void mouseDoubleClicked(MouseEvent me) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent me) {
			// TODO Auto-generated method stub
            hideChilds();
            showEp();
            this.pane.setVisible(true);
            enableOtherButtons();
            setBackgroundColor(ColorConstants.lightGray);
            setForegroundColor(ColorConstants.red);
            //setBorder(selectedBorder);
            getLayout().repaint();			
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			// TODO Auto-generated method stub
			
		}

    }
}
