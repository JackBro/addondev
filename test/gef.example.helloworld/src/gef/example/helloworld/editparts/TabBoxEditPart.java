package gef.example.helloworld.editparts;

import gef.example.helloworld.figure.TabBoxFigure;
import gef.example.helloworld.figure.TabPanelFigure;
import gef.example.helloworld.figure.TabPanelLineBorder;
import gef.example.helloworld.model.BoxModel;
import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.TabBoxModel;
import gef.example.helloworld.model.TabPanelModel;
import gef.example.helloworld.model.TabPanelsModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.EditPart;

public class TabBoxEditPart extends AbstractTabEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		//BoxModel model =  (BoxModel)getModel();
		//model.addChild(new TabPanelModel());
		//model.addChild(new TabPanelModel());
		
		return super.createFigure();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		super.propertyChange(evt);
		
		if(evt.getPropertyName().equals("tabpanels")){
			TabBoxModel tabboxmodel =  (TabBoxModel)getModel();
			//AbstractElementModel tabpanels = (AbstractElementModel)model.getPropertyValue("tabpanels");
			//AbstractElementModel tabpanels = tabboxmodel.getTabPanelsModel();
			List list = (List)evt.getNewValue();
//			while(tabpanels.getChildren().size() > 0){
//				tabpanels.getChildren().remove(0);
//			}
//			
//			for (Object object : list) {
//				tabpanels.getChildren().add(object);
//			}
			
			tabboxmodel.removeAllChild();
			for (Object object : list) {
				tabboxmodel.addChild((AbstractElementModel) object);
			}
		}
	}	
	
	@Override
	protected Label getNewTab(TabPanelFigure child, EditPart childEditPart) {
		// TODO Auto-generated method stub
		return new TabButton(child, childEditPart);
	}
	
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
            setBorder(new TabPanelLineBorder());
            
            ((TabPanelModel)childEditPart.getModel()).addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					// TODO Auto-generated method stub
					refreshTabs();
				}
			});
            
            String message = ((TabPanelModel)childEditPart.getModel()).getTabLabel();
            
            setText(message);
            addMouseListener(this);

            setBackgroundColor(ColorConstants.lightGray);
            setForegroundColor(ColorConstants.black);
            enableOtherButtons();

        }

        @Override
		public void setText(String s) {
			// TODO Auto-generated method stub
			super.setText(s);
			//((TabPanelModel)childEditPart.getModel()).a
		}

		@Override
		public String getText() {
			// TODO Auto-generated method stub
        	String message = ((TabPanelModel)childEditPart.getModel()).getTabLabel();
			return message;
		}

		protected void showEp() {
            activateChilds(this.childEditPart);
        }
        
        public void update() {
            String message = getText();
            //this.tabLabel.setText(message);
            this.setText(message);
        }

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
            setBackgroundColor(ColorConstants.white);
            setForegroundColor(ColorConstants.black);
            //setBorder(selectedBorder);
            getLayout().repaint();			
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			// TODO Auto-generated method stub
			
		}

    }
}
