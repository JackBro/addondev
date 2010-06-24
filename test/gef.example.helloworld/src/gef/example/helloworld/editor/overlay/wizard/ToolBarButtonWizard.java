package gef.example.helloworld.editor.overlay.wizard;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
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
		
		protected ToolBarButtonWizardPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
	        
	        new Label(composite, SWT.NONE).setText("id");
	        fId = new Text(composite, SWT.BORDER);
	        fId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        new Label(composite, SWT.NONE).setText("label");
	        fLabel = new Text(composite, SWT.BORDER);
	        fLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
	        setControl(composite);
		}
	}
	
	public class ToolBarButtonImageTypeWizardPage extends WizardPage {

		private Button each, rectangle;
		
		protected ToolBarButtonImageTypeWizardPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			
			each = new Button(composite, SWT.RADIO);
			each.setText("each select image");
			rectangle = new Button(composite, SWT.RADIO);
			rectangle.setText("form one image");
			
			each.setSelection(true);
			
			setControl(composite);
		}
		
	}

	public class ToolBarButtonImageWizardPage extends WizardPage {

		private String state;
		private String cond;
		private ArrayList<Image> images;
		private  Label imageW, imageH;

		public void setState(String state) {
			this.state = state;
		}

		public void setCond(String cond) {
			this.cond = cond;
		}

		protected ToolBarButtonImageWizardPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
			images = new ArrayList<Image>();
		}
		
		@Override
		public void createControl(Composite parent) {
	        Composite composite = new Composite(parent, SWT.NONE);
	        GridLayout layout = new GridLayout();
	        layout.marginWidth = 0;
	        layout.marginHeight = 0;
	        composite.setLayout(layout);
	        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
	                | GridData.HORIZONTAL_ALIGN_FILL));
			
			createToolBarImageEdit(composite, getTitle());
			setControl(composite);
			
			setPageComplete(true);
		}
				
		private void createToolBarImageEdit(final Composite parent, String label){
			
			final Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(3, false));
			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        new Label(composite, SWT.NONE).setText("path");
	        final Text path = new Text(composite, SWT.BORDER);
	        path.setEnabled(false);
	        path.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        Button btton = new Button(composite, SWT.NONE);
	        btton.setText("select image");
			
			Composite compositeWH = new Composite(parent, SWT.NONE);
			compositeWH.setLayout(new GridLayout(2, true));
			compositeWH.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			imageW = new Label(compositeWH, SWT.NONE);
			imageW.setText("width");
			imageW.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			imageH = new Label(compositeWH, SWT.NONE);
			imageH.setText("height");
			imageH.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			final FigureCanvas canvas = new FigureCanvas(parent);
			GridData gd2 = new GridData(GridData.FILL_BOTH);
			canvas.setLayoutData(gd2);
			Panel panel=new Panel();
			panel.setBorder(new LineBorder(1));
			panel.setLayoutManager(new FlowLayout());
			canvas.setContents(panel);
			final ImageFigure image = new ImageFigure();
			panel.add(image);
			
			ModifyListener modify = new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					// TODO Auto-generated method stub
					canvas.redraw();
				}
			};
			
	        final Button check = new Button(parent, SWT.CHECK);
	        check.setText("set rect");
	        check.setEnabled(false);
			//check.setLayoutData(gd);

			final Group rectgroup = new Group(parent, SWT.NONE);
			rectgroup.setText("rect");
			rectgroup.setEnabled(false);
			GridData gd = new GridData();
			gd.horizontalSpan = 3;
			rectgroup.setLayoutData(gd);
			rectgroup.setLayout(new GridLayout(4, false));
						
			final Spinner spinnerX = createSpinner(rectgroup, "x", modify);
			final Spinner spinnerY = createSpinner(rectgroup, "y", modify);
			final Spinner spinnerW = createSpinner(rectgroup, "width", modify);
			final Spinner spinnerH = createSpinner(rectgroup, "height", modify);
			
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
								imageW.setText("width:" + String.valueOf(pngimage.getImageData().width));
								imageH.setText("height:" + String.valueOf(pngimage.getImageData().height));
								check.setEnabled(true);
								images.add(pngimage);
								
								image.setImage(pngimage);
								image.repaint();
								
							} catch (CoreException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}		
			        }
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});	
	        
			check.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					rectgroup.setEnabled(check.getSelection());
					spinnerX.setEnabled(check.getSelection());
					spinnerY.setEnabled(check.getSelection());
					spinnerW.setEnabled(check.getSelection());
					spinnerH.setEnabled(check.getSelection());
					canvas.redraw();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});	
			

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			for (Image image : images) {
				image.dispose();
			}
			super.dispose();
		}
		
		private Spinner createSpinner(Composite parent, String text, ModifyListener modify){
			new Label(parent, SWT.NONE).setText(text);
			Spinner spinner = new Spinner(parent, SWT.BORDER);
			spinner.setMaximum(0);
			spinner.addModifyListener(modify);
			spinner.setEnabled(false);
			return spinner;
		}
	}
	
	public class ToolBarButtonEachImageWizardPage1 extends WizardPage {

		private Text nText, nText2, nText3, nText4;
		private ArrayList<Image> images = new ArrayList<Image>();
		//private Label imageW, imageH;
		
		protected ToolBarButtonEachImageWizardPage1(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
	        Composite composite = new Composite(parent, SWT.NONE);
	        GridLayout layout = new GridLayout();
	        layout.marginWidth = 0;
	        layout.marginHeight = 0;
	        composite.setLayout(layout);
	        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
	                | GridData.HORIZONTAL_ALIGN_FILL));
			
			createToolBarImage(composite, getTitle());
			setControl(composite);
			
			setPageComplete(true);
		}

		private void createToolBarImage(Composite parent, String title) {
			// TODO Auto-generated method stub

			nText = createSelectImage(parent, "toolbar");
			nText2 = createSelectImage(parent, "toolbar hover");
			nText3 = createSelectImage(parent, "toolbar small");
			nText4 = createSelectImage(parent, "toolbar small hover");
		}
		
		private Text createSelectImage(final Composite parent, String label){
			
			Group group = new Group(parent, SWT.NONE);
			group.setText(label);
			group.setLayout(new GridLayout(3, false));
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        final Text path = new Text(group, SWT.BORDER);
	        path.setEnabled(false);
	        path.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        Button btton = new Button(group, SWT.NONE);
	        btton.setText("select image");
			
	        Button clearbutton = new Button(group, SWT.NONE);
	        clearbutton.setText("clear");
	        
			btton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
			        
			        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(),
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
							path.setText(res.getFullPath().toString());
						}		
			        }
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			
			clearbutton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					path.setText("");
				}
				
			});
			
			return path;
		}
		
		
	}
	
	public class ToolBarButtonRectangleImageWizardPage1 extends WizardPage {

		private ImageFigure imagefigure;
		private ArrayList<Image> images = new ArrayList<Image>();
		private Label imagesize;
		
		protected ToolBarButtonRectangleImageWizardPage1(
				String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
	        Composite composite = new Composite(parent, SWT.NONE);
	        GridLayout layout = new GridLayout();
	        layout.marginWidth = 0;
	        layout.marginHeight = 0;
	        composite.setLayout(layout);
	        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
	                | GridData.HORIZONTAL_ALIGN_FILL));
			
			createToolBarImageLoad(composite);
			setControl(composite);
		}

		private void createToolBarImageLoad(Composite parent) {
			// TODO Auto-generated method stub
			final Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(3, false));
			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        new Label(composite, SWT.NONE).setText("path");
	        final Text path = new Text(composite, SWT.BORDER);
	        path.setEnabled(false);
	        path.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        Button btton = new Button(composite, SWT.NONE);
	        btton.setText("select image");
			
			imagesize = new Label(parent, SWT.NONE);
			imagesize.setText("");
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.verticalSpan = 3;
			
			final FigureCanvas canvas = new FigureCanvas(parent);
			GridData gd2 = new GridData(GridData.FILL_BOTH);
			canvas.setLayoutData(gd2);
			Panel panel=new Panel();
			panel.setBorder(new LineBorder(1));
			panel.setLayoutManager(new FlowLayout());
			canvas.setContents(panel);
			imagefigure = new ImageFigure();
			panel.add(imagefigure);

			btton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
			        
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
								imagesize.setText(String.valueOf(pngimage.getImageData().width)
										+ "x" + String.valueOf(pngimage.getImageData().height));
								images.add(pngimage);
								
								imagefigure.setImage(pngimage);
								imagefigure.repaint();
								
							} catch (CoreException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}		
			        }
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});	
		}
	}
	
	public class ToolBarButtonRectangleImageWizardPage2 extends WizardPage {
		
		private String state;
		private String cond;
		private ArrayList<Image> images = new ArrayList<Image>();
		private Label imageW, imageH;
		private HashMap<Group, Rectangle> rectangleMap = new HashMap<Group, Rectangle>();
		
		protected ToolBarButtonRectangleImageWizardPage2(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
	        Composite composite = new Composite(parent, SWT.NONE);
	        GridLayout layout = new GridLayout();
	        layout.marginWidth = 0;
	        layout.marginHeight = 0;
	        composite.setLayout(layout);
	        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
	                | GridData.HORIZONTAL_ALIGN_FILL));
			
			createToolBarImageEdit(composite, getTitle());
			setControl(composite);
			
			setPageComplete(true);
		}

		private void createToolBarImageEdit(Composite parent, String title) {
			// TODO Auto-generated method stub
			final Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(3, false));
			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        new Label(composite, SWT.NONE).setText("path");
	        final Text path = new Text(composite, SWT.BORDER);
	        path.setEnabled(false);
	        path.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        Button btton = new Button(composite, SWT.NONE);
	        btton.setText("select image");
			
			Composite compositeWH = new Composite(parent, SWT.NONE);
			compositeWH.setLayout(new GridLayout(2, true));
			compositeWH.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			imageW = new Label(compositeWH, SWT.NONE);
			imageW.setText("width");
			imageW.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			imageH = new Label(compositeWH, SWT.NONE);
			imageH.setText("height");
			imageH.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			final FigureCanvas canvas = new FigureCanvas(parent);
			GridData gd2 = new GridData(GridData.FILL_BOTH);
			canvas.setLayoutData(gd2);
			Panel panel=new Panel();
			panel.setBorder(new LineBorder(1));
			panel.setLayoutManager(new FlowLayout());
			canvas.setContents(panel);
			final ImageFigure image = new ImageFigure();
			panel.add(image);

			Composite compositeRectangleGroup = new Composite(parent, SWT.NONE);
			compositeRectangleGroup.setLayout(new GridLayout(2, true));
			compositeRectangleGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        createRectangleGroup(compositeRectangleGroup, "normal");
	        createRectangleGroup(compositeRectangleGroup, "hover");
	        createRectangleGroup(compositeRectangleGroup, "small");
	        createRectangleGroup(compositeRectangleGroup, "small hover");
			
			canvas.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					// TODO Auto-generated method stub
					for (Entry<Group, Rectangle> entry : rectangleMap.entrySet()) {
						e.gc.drawRectangle(entry.getValue());
					}
//					if(check.getSelection()){
//						int x =Integer.parseInt(spinnerX.getText());
//						int y =Integer.parseInt(spinnerY.getText());
//						int w =Integer.parseInt(spinnerW.getText());
//						int h =Integer.parseInt(spinnerH.getText());
//						e.gc.drawRectangle(x, y, w, h);
//					}
				}
			});
			
			
	        btton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
			        
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
								imageW.setText("width:" + String.valueOf(pngimage.getImageData().width));
								imageH.setText("height:" + String.valueOf(pngimage.getImageData().height));
								//check.setEnabled(true);
								images.add(pngimage);
								
								image.setImage(pngimage);
								image.repaint();
								
							} catch (CoreException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}		
			        }
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});	
	        

		}
		
		private Spinner createSpinner(Composite parent, String text){
			new Label(parent, SWT.NONE).setText(text);
			Spinner spinner = new Spinner(parent, SWT.BORDER);
			spinner.setMaximum(0);
			//spinner.setEnabled(false);
			return spinner;
		}
		
		private void createRectangleGroup(Composite parent, String text){
			final Group rectgroup = new Group(parent, SWT.NONE);
			rectgroup.setText("rect");
			//rectgroup.setEnabled(false);
			GridData gd = new GridData();
			//gd.horizontalSpan = 3;
			rectgroup.setLayoutData(gd);
			rectgroup.setLayout(new GridLayout(4, false));
			//rectgroup.setLayout(new );
			//rectgroup.setLayoutData(new FlowLayout(true));
			
			final Spinner spinnerX = createSpinner(rectgroup, "x");
			final Spinner spinnerY = createSpinner(rectgroup, "y");
			final Spinner spinnerW = createSpinner(rectgroup, "width");
			final Spinner spinnerH = createSpinner(rectgroup, "height");
			
			ModifyListener modify = new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					// TODO Auto-generated method stub
					int x =Integer.parseInt(spinnerX.getText());
					int y =Integer.parseInt(spinnerY.getText());
					int w =Integer.parseInt(spinnerW.getText());
					int h =Integer.parseInt(spinnerH.getText());
					Rectangle rec = new Rectangle(x, y, w, h);
					rectangleMap.put(rectgroup, rec);
					//canvas.redraw();
				}
			};
			
			spinnerX.addModifyListener(modify);
			spinnerY.addModifyListener(modify);
			spinnerW.addModifyListener(modify);
			spinnerH.addModifyListener(modify);
		}
		
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			for (Image image : images) {
				image.dispose();
			}
			super.dispose();
		}
	}
	
	private ToolBarButtonWizardPage page1;
	private ToolBarButtonImageTypeWizardPage imagetypepage;
	//private ToolBarButtonImageWizardPage page2, page3, page4, page5;
	private ToolBarButtonRectangleImageWizardPage2 page2;
	private ToolBarButtonEachImageWizardPage1 page6;

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		page1 = new ToolBarButtonWizardPage("ToolBarButton");
		addPage(page1);
		
		imagetypepage = new ToolBarButtonImageTypeWizardPage("ToolBarButton");
		addPage(imagetypepage);		
		
		page2 = new ToolBarButtonRectangleImageWizardPage2("normal");
		addPage(page2);
//		
//		page3 = new ToolBarButtonImageWizardPage("hover");
//		addPage(page3);
//		
//		page4 = new ToolBarButtonImageWizardPage("iconsize=small");
//		addPage(page4);
//		
//		page5 = new ToolBarButtonImageWizardPage("iconsize=small hover");
//		addPage(page5);
		
		page6 = new ToolBarButtonEachImageWizardPage1("iconsize=small hover");
		addPage(page6);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// TODO Auto-generated method stub
		if(page instanceof ToolBarButtonImageTypeWizardPage){
			if(((ToolBarButtonImageTypeWizardPage)page).each.getSelection()){
				return page6;
			}else{
				return page2;
			}
		}
		if(page == page2){
			return null;
		}
		return super.getNextPage(page);
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
