package gef.example.helloworld.editor.overlay.wizard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import gef.example.helloworld.HelloworldPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;

public class ToolBarButtonWizard extends AbstractXULWizard {
	
	public class ToolBarButtonWizardPage extends WizardPage {

		protected Text fId, fLabel;
		private ArrayList<Image> images;
		
		protected ToolBarButtonWizardPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
			images = new ArrayList<Image>();
		}

		@Override
		public void createControl(final Composite parent) {
			// TODO Auto-generated method stub
			Composite c = new Composite(parent, SWT.NONE);
	        c.setLayout(new GridLayout(2, false));
	        GridData gd;
	        //new Label(c, SWT.NONE).setText("ToolBarButton");
	        
	        new Label(c, SWT.NONE).setText("id");
	        fId = new Text(c, SWT.BORDER);
	        new Label(c, SWT.NONE).setText("label");
	        fLabel = new Text(c, SWT.BORDER);
	        

	        createToolBarImageEdit(c, "test");

	        
	        setControl(c);
		}
		
		private void createToolBarImageEdit(final Composite parent, String label){
			
			final Composite composite = new Composite(parent, SWT.NONE);
			
			composite.setLayout(new GridLayout(3, false));
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			
	        new Label(composite, SWT.NONE).setText("path");
	        final Text path = new Text(composite, SWT.BORDER);
	        Button btton = new Button(composite, SWT.NONE);
	        btton.setText("browse");
	        
	        final Button check = new Button(composite, SWT.CHECK);
	        check.setText("rect");
			GridData gd = new GridData();
			gd.horizontalSpan = 3;
			//check.setLayoutData(gd);

	        
			final FigureCanvas canvas = new FigureCanvas(composite);
			GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd2.horizontalSpan = 3;
			canvas.setLayoutData(gd2);
			Panel panel=new Panel();
			panel.setBorder(new LineBorder(2));
			//panel.setPreferredSize(200, 100);
			panel.setLayoutManager(new FlowLayout());
			canvas.setContents(panel);
			final ImageFigure image = new ImageFigure();//new ImageFigure(HelloworldPlugin.getDefault().getImageRegistry().get(HelloworldPlugin.IMG_DUMMY));
			
			panel.add(image);

			check.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					canvas.redraw();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});	
			
			ModifyListener modify = new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					// TODO Auto-generated method stub
					canvas.redraw();
				}
			};
			Label xlabel = new Label(composite, SWT.NONE);
			xlabel.setText("x");
			final Spinner spinnerX = new Spinner(composite, SWT.BORDER);
			spinnerX.setMaximum(0);
			spinnerX.addModifyListener(modify);
			
			new Label(composite, SWT.NONE).setText("y");
			final Spinner spinnerY = new Spinner(composite, SWT.BORDER);
			spinnerY.setMaximum(0);
			spinnerY.addModifyListener(modify);
			
			Label widthlabel = new Label(composite, SWT.NONE);
			widthlabel.setText("width");
			final Spinner spinnerW = new Spinner(composite, SWT.BORDER);
			spinnerW.setMaximum(0);
			spinnerW.addModifyListener(modify);
			
			Label heightlabel = new Label(composite, SWT.NONE);
			heightlabel.setText("height");
			final Spinner spinnerH = new Spinner(composite, SWT.BORDER);
			spinnerH.setMaximum(0);
			spinnerH.addModifyListener(modify);
			
			canvas.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					// TODO Auto-generated method stub
					if(check.getSelection()){
						int x =Integer.parseInt(spinnerX.getText());
						int y =Integer.parseInt(spinnerY.getText());
						int w =Integer.parseInt(spinnerW.getText());
						int h =Integer.parseInt(spinnerH.getText());
						e.gc.drawRectangle(x, y, w, h);
					}
				}
			});
			
			
	        btton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
			        
			        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(composite.getShell(),
			        		new WorkbenchLabelProvider(), 
			        		new WorkbenchContentProvider());
			        IEditorPart editor = HelloworldPlugin.getActiveEditorPart();
			        FileEditorInput file = (FileEditorInput) editor.getEditorInput();
			        IProject project = file.getFile().getProject();
			        
			        dialog.setInput(project);
			        if (dialog.open() == Window.OK) {
						IResource res= (IResource) dialog.getFirstResult();
						if(res.getType() == IResource.FILE)
						{
							IFile imgfile = (IFile)res;
							
							InputStream input = null;
							try {
								input = imgfile.getContents();
								path.setText(res.getFullPath().toString());
								Image pngimage = new Image(null, input);
								
								images.add(pngimage);
								
								image.setImage(pngimage);
								image.repaint();
							} catch (CoreException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} //new FileInputStream(imgfile);


						}		
			        }
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});			
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		for (Image image : page1.images) {
			image.dispose();
		}
		super.dispose();
		//image
	}

}
