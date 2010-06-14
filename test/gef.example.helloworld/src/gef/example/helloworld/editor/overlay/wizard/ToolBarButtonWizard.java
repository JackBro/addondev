package gef.example.helloworld.editor.overlay.wizard;

import gef.example.helloworld.HelloworldPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;

public class ToolBarButtonWizard extends AbstractXULWizard {
	
	public class ToolBarButtonWizardPage extends WizardPage {

		protected Text fId, fLabel;
		
		
		protected ToolBarButtonWizardPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(final Composite parent) {
			// TODO Auto-generated method stub
			Composite c = new Composite(parent, SWT.NONE);
	        c.setLayout(new GridLayout(2, false));
	        GridData gd;
	        new Label(c, SWT.NONE).setText("ToolBarButton");
	       
	        Button button1 = new Button(c, SWT.RADIO);
	        button1.setText("1");
	        Button button2 = new Button(c, SWT.RADIO);
	        button2.setText("2");
	        
	        ListViewer list = new ListViewer(c);
	        list.add("menu_FilePopup");
	        list.add("menu_EditPopup");
	        list.add("menu_viewPopup");
	        list.add("menu_ToolsPopup");
	        
	        new Label(c, SWT.NONE).setText("id");
	        fId = new Text(c, SWT.BORDER);
	        new Label(c, SWT.NONE).setText("label");
	        fLabel = new Text(c, SWT.BORDER);
	        

	        new Label(c, SWT.NONE).setText("path");
	        final Text path = new Text(c, SWT.BORDER);
	        Button btton = new Button(c, SWT.NONE);
	        btton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
			        
			        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(),
			        		new WorkbenchLabelProvider(), 
			        		new WorkbenchContentProvider());
			        IEditorPart editor = HelloworldPlugin.getActiveEditorPart();
			        FileEditorInput file = (FileEditorInput) editor.getEditorInput();
			        IProject project = file.getFile().getProject();
			        dialog.setInput(project);
			        if (dialog.open() == Window.OK) {
						IResource res= (IResource) dialog.getFirstResult();
						//path.setText(res.getFullPath().makeRelative().toString());
						path.setText(res.getFullPath().toString());
			        }
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			FigureCanvas canvas = new FigureCanvas(c);
			canvas.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					// TODO Auto-generated method stub
					
				}
			});

	        
	        setControl(c);
		}

	}

	private ToolBarButtonWizardPage page1;

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		page1 = new ToolBarButtonWizardPage("ToolBarButton");
		addPage(page1);
		
	}

	@Override
	protected Object getElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return super.performFinish();
	}

}
