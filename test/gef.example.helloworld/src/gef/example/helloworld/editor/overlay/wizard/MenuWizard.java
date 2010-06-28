package gef.example.helloworld.editor.overlay.wizard;

import gef.example.helloworld.model.MenuItemModel;
import gef.example.helloworld.model.MenuPopupModel;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MenuWizard extends AbstractXULWizard {

	public class MenuWizardPage1 extends WizardPage {

		private Button addnewbutton, addexistingbutton;

		public boolean isAddNewMenu(){
			return addnewbutton.getSelection();
		}
		
		protected MenuWizardPage1(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			Composite composite = new Composite(parent, SWT.NONE);
	        composite.setLayout(new GridLayout(1, false));
	        
	        addexistingbutton = new Button(composite, SWT.RADIO);
	        addexistingbutton.setText("add to existing menu");  
	        addexistingbutton.setSelection(true);
	        
	        addnewbutton = new Button(composite, SWT.RADIO);
	        addnewbutton.setText("add new menu");
	        
	        setControl(composite);
		}
	}
	
	public class AddNewMenuWizardPage extends WizardPage {

		protected AddNewMenuWizardPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class AddExistMenuWizardPage extends WizardPage {

		private Text fid, fLabel;
		private String fPart;
		
		protected AddExistMenuWizardPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			Composite composite = new Composite(parent, SWT.NONE);
	        composite.setLayout(new GridLayout(1, false));
	        new Label(composite, SWT.NONE).setText("menu");
	       
//			Label label = new Label(composite, SWT.LEFT);
//			label.setText("Element");
//			GridData gd = new GridData();
//			gd.horizontalSpan = 2;
//			label.setLayoutData(gd);
	        
	        Group group = new Group(composite, SWT.NONE);
	        group.setText("place");
	        FillLayout fill = new FillLayout();
	        fill.marginHeight = 5;
	        fill.marginWidth = 5;
	        group.setLayout(fill);
	        
	        createRaidButton(group, "File", "menu_FilePopup").setSelection(true);
	        createRaidButton(group, "Edit", "menu_EditPopup");
	        createRaidButton(group, "view", "menu_viewPopup");
	        createRaidButton(group, "Tools", "menu_ToolsPopup");
	        
	        

			Composite attr = new Composite(composite, SWT.NONE);
			attr.setLayout(new GridLayout(2, false));	
			attr.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        new Label(attr, SWT.NONE).setText("id");
	        fid = new Text(attr, SWT.BORDER);
	        fid.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        new Label(attr, SWT.NONE).setText("label");
	        fLabel = new Text(attr, SWT.BORDER);
	        fLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
	        setControl(composite);
		}
		
		private Button createRaidButton(Composite parent, String text, final String data){
			Button button = new Button(parent, SWT.RADIO);
			button.setText(text);
			//button.setData(data);
			button.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					fPart = data;
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			return button;
		}
	}	

	private AddExistMenuWizardPage page1;
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		page1 = new AddExistMenuWizardPage("menu");
		addPage(page1);
	}

	@Override
	protected Object getElement() {
		// TODO Auto-generated method stub
		MenuPopupModel popup = new MenuPopupModel();
		popup.setPropertyValue(MenuPopupModel.ATTR_ID, page1.fPart);
		MenuItemModel menuitem = new MenuItemModel();
		menuitem.setPropertyValue(MenuPopupModel.ATTR_ID, page1.fid.getText());
		menuitem.setPropertyValue(MenuItemModel.ATTR_LABEL, page1.fLabel.getText());
		popup.addChild(menuitem);
		return popup;
	}
}
