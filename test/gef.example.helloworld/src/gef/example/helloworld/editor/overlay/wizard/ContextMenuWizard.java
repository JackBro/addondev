package gef.example.helloworld.editor.overlay.wizard;

import gef.example.helloworld.model.MenuItemModel;
import gef.example.helloworld.model.MenuModel;
import gef.example.helloworld.model.MenuPopupModel;
import gef.example.helloworld.model.PopupModel;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class ContextMenuWizard extends AbstractXULWizard {
	
	public class ContextMenuWizardPage extends WizardPage {
		
		private Button button, button2;
		private Text fid, fLabel;
		private Text fid2, fLabel2;
		
		protected ContextMenuWizardPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			Composite composite = new Composite(parent, SWT.NONE);
	        composite.setLayout(new GridLayout(1, false));
			
	        button = new Button(composite, SWT.RADIO);
			button.setText("memu");
			button.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					fid2.setEditable(false);
					fLabel2.setEditable(false);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
	        button2 = new Button(composite, SWT.RADIO);
			button2.setText("sub memu");
			button2.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					fid2.setEditable(true);
					fLabel2.setEditable(true);					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
	        Group group = new Group(composite, SWT.NONE);
	        group.setText("menu");
	        group.setLayout(new GridLayout(2, false));
	        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
	        new Label(group, SWT.NONE).setText("id");
	        fid = new Text(group, SWT.BORDER);
	        fid.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        new Label(group, SWT.NONE).setText("label");
	        fLabel = new Text(group, SWT.BORDER);
	        fLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
	        Group group2 = new Group(composite, SWT.NONE);
	        group2.setText("sub menu");
	        group2.setLayout(new GridLayout(2, false));
	        group2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        new Label(group2, SWT.NONE).setText("id");
	        fid2 = new Text(group2, SWT.BORDER);
	        fid2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        new Label(group2, SWT.NONE).setText("label");
	        fLabel2 = new Text(group2, SWT.BORDER);
	        fLabel2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
	        button.setSelection(true);
			fid2.setEditable(false);
			fLabel2.setEditable(false);
			
			setControl(composite);
		}
	}
	
	private ContextMenuWizardPage page1;

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		page1 = new ContextMenuWizardPage("ContextMenu");
		addPage(page1);
	}

	@Override
	protected Object getElement() {
		// TODO Auto-generated method stub
		PopupModel popup = new PopupModel();
		popup.setPropertyValue(MenuPopupModel.ATTR_ID, "contentAreaContextMenu");
		if(page1.button.getSelection()){
			MenuItemModel menuitem = new MenuItemModel();
			menuitem.setPropertyValue(MenuPopupModel.ATTR_ID, page1.fid.getText());
			menuitem.setPropertyValue(MenuItemModel.ATTR_LABEL, page1.fLabel.getText());
			
			popup.addChild(menuitem);
			
			return popup;
		}else{
			MenuModel menu = new MenuModel();
			menu.setPropertyValue(MenuPopupModel.ATTR_ID, page1.fid.getText());
			menu.setPropertyValue(MenuItemModel.ATTR_LABEL, page1.fLabel.getText());
			
			MenuItemModel menuitem = new MenuItemModel();
			menuitem.setPropertyValue(MenuPopupModel.ATTR_ID, page1.fid2.getText());
			menuitem.setPropertyValue(MenuItemModel.ATTR_LABEL, page1.fLabel2.getText());
			
			menu.addChild(menuitem);
			
			popup.addChild(menu);
			return popup;
		}

	}

}
