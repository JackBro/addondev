package gef.example.helloworld.editor.overlay.wizard;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import gef.example.helloworld.HelloworldPlugin;
import gef.example.helloworld.model.ToolBarButtonModel;
import gef.example.helloworld.model.ToolBarPaletteModel;
import gef.example.helloworld.parser.css.Attr;
import gef.example.helloworld.parser.css.CSS;
import gef.example.helloworld.parser.css.CSSException;
import gef.example.helloworld.parser.css.CSSParser;
import gef.example.helloworld.parser.css.Declaration;
import gef.example.helloworld.parser.xul.Element;
import gef.example.helloworld.parser.xul.XULParser;
import gef.example.helloworld.util.Util;

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
	
	private ToolBarButtonWizardPage page1;
	private ToolBarButtonImageTypeWizardPage imagetypepage;
	private ToolBarButtonRectangleImageWizardPage1 rectpage1;
	private ToolBarButtonRectangleImageWizardPage2 rectpage2;
	private ToolBarButtonEachImageWizardPage1 eachpage1;
	
	private static String NORMAl = "normal";
	private static String NORMAl_HOVER = "normal_hover";
	private static String SMALL = "small";
	private static String SMALL_HOVER = "small_hover";
	

	private String getToolBarButtonID(){
		return page1.getToolBarButtonID();
	}
	
	private void setCss(CSS css, String id){
		
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		page1 = new ToolBarButtonWizardPage("ToolBarButton");
		addPage(page1);
		
		imagetypepage = new ToolBarButtonImageTypeWizardPage("ToolBarButton");
		addPage(imagetypepage);		
	
		rectpage1 = new ToolBarButtonRectangleImageWizardPage1("normal");
		addPage(rectpage1);
		
		rectpage2 = new ToolBarButtonRectangleImageWizardPage2("normal");
		addPage(rectpage2);
		
		
		
		
//		
//		page3 = new ToolBarButtonImageWizardPage("hover");
//		addPage(page3);
//		
//		page4 = new ToolBarButtonImageWizardPage("iconsize=small");
//		addPage(page4);
//		
//		page5 = new ToolBarButtonImageWizardPage("iconsize=small hover");
//		addPage(page5);
		
		eachpage1 = new ToolBarButtonEachImageWizardPage1("iconsize=small hover");
		addPage(eachpage1);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// TODO Auto-generated method stub
		if(page instanceof ToolBarButtonImageTypeWizardPage){
			if(((ToolBarButtonImageTypeWizardPage)page).each.getSelection()){
				return eachpage1;
			}else{
				return rectpage1;
			}
		}
 
		if(page == rectpage2){
			rectpage2.setImage(rectpage1.imgfile);
			return null;
		}
		
		 
		if(page == eachpage1){
			return null;
		}
		return super.getNextPage(page);
	}

	@Override
	protected Object getElement() {
		// TODO Auto-generated method stub
		ToolBarPaletteModel palette = new ToolBarPaletteModel();
		palette.setPropertyValue("id", "BrowserToolbarPalette");
		ToolBarButtonModel button = new ToolBarButtonModel();
		button.setPropertyValue("id", page1.getToolBarButtonID());
		button.setPropertyValue("label", page1.getToolBarButtonLabel());
		
		palette.addChild(button);
		
		return palette;
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		
        IEditorPart editor = HelloworldPlugin.getActiveEditorPart();
        FileEditorInput file = (FileEditorInput) editor.getEditorInput();
		
		String src = Util.getContent(file.getFile());
		XULParser parser = new XULParser();
		parser.parse(src);
		List<Element> styles = parser.getStylesheets();
		
		String chromeurl = styles.get(1).getAttr("href");
		IFile sfile = Util.getFile(chromeurl);
		
		CSSParser cssparser = new CSSParser();
		try {
			String csssrc = Util.getContent(sfile);
			cssparser.parse(csssrc);
		} catch (CSSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CSS css = cssparser.getCSS();
		
		if(imagetypepage.each.getSelection()){
			IFile normal = eachpage1.imagefilemap.get(NORMAl);
			String normalurl = Util.getChromeUrl(normal);
			
			
		}
		
		String id = getToolBarButtonID();
		Attr attr = new Attr("iconsize", "=", "small");
		Declaration declaration = cssparser.declaration_stmt("-moz-image-region: rect(24px 24px 48px 0px);");
		css.addDeclaration(id, 
				new ArrayList<Attr>(Arrays.asList(attr)), 
				new ArrayList<String>(Arrays.asList("hover")),
				declaration);
		
		String cssstr = css.toString();
		
		return super.performFinish();
	}
	
	private void add(){
		
	}
	
	public class ToolBarButtonWizardPage extends WizardPage {

		protected Text fId, fLabel;
		protected Button popupbutton;
		
		public String getToolBarButtonID(){
			return fId.getText();
		}
		
		public String getToolBarButtonLabel(){
			return fLabel.getText();
		}
		
		public boolean isPopUp(){
			return popupbutton.getSelection();
		}
		
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
	        
	        popupbutton = new Button(composite, SWT.CHECK);
	        popupbutton.setText("add popup menu");
	        
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
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(1, false));
			
			each = new Button(composite, SWT.RADIO);
			each.setText("each select image");
			rectangle = new Button(composite, SWT.RADIO);
			rectangle.setText("form one image");
			
			each.setSelection(true);
			
			setControl(composite);
		}
		
	}
	
	public class ToolBarButtonEachImageWizardPage1 extends WizardPage {

		private HashMap<String, IFile> imagefilemap = new HashMap<String, IFile>();
		
		protected ToolBarButtonEachImageWizardPage1(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
			setPageComplete(false);
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
		}

		private void createToolBarImage(Composite parent, String title) {
			// TODO Auto-generated method stub

			createSelectImage(parent, "button image", NORMAl);
			createSelectImage(parent, "button image(mouse hover)", NORMAl_HOVER);
			createSelectImage(parent, "small button image", SMALL);
			createSelectImage(parent, "small button image(mouse hover)", SMALL_HOVER);
		}
		
		private void createSelectImage(final Composite parent, String label, final String id){
			
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
			        
			        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
			        		parent.getShell(),
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
							imagefilemap.put(id, (IFile)res);
							path.setText(res.getFullPath().toString());
							setPageComplete(true);
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
		}
	}
	
	public class ToolBarButtonRectangleImageWizardPage1 extends WizardPage {

		private Text imagepath;
		private IFile imgfile;
		private ImageFigure imagefigure;
		private ArrayList<Image> images = new ArrayList<Image>();
		private Label imagesize;
		
		protected ToolBarButtonRectangleImageWizardPage1(
				String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

//		@Override
//		public boolean isPageComplete() {
//			// TODO Auto-generated method stub
//			return imagepath.getText() != null;
//			
//			//return super.isPageComplete();
//		}

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
			
			//setPageComplete(false);
		}

		private void createToolBarImageLoad(Composite parent) {
			// TODO Auto-generated method stub
			final Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(3, false));
			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        new Label(composite, SWT.NONE).setText("path");
	        imagepath = new Text(composite, SWT.BORDER);
	        imagepath.setEnabled(false);
	        imagepath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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
			//panel.setBorder(new LineBorder(1));
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
							imgfile = (IFile)res;
							
							InputStream input = null;
							try {
								input = imgfile.getContents();
								imagepath.setText(res.getFullPath().toString());
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
							//setPageComplete(true);
						}		
			        }
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
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
	}
	
	public class ToolBarButtonRectangleImageWizardPage2 extends WizardPage {
		
		private Text imagepath;
		private ImageFigure imagefigure;
		private Label imagesize;
		private ArrayList<Image> images = new ArrayList<Image>();
		private Spinner spinnerX, spinnerY, spinnerW, spinnerH;
		private FigureCanvas canvas;
		//private HashMap<Group, Rectangle> rectangleMap = new HashMap<Group, Rectangle>();
		
		protected ToolBarButtonRectangleImageWizardPage2(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
			
		}

		public void setImage(IFile imgfile){
			imagepath.setText(imgfile.getFullPath().toPortableString());
			
			IEditorPart editor = HelloworldPlugin.getActiveEditorPart();
	        FileEditorInput file = (FileEditorInput) editor.getEditorInput();
	        IProject project = file.getFile().getProject();
	        
				if(imgfile.exists()){		
					InputStream input = null;
					try {
						input = imgfile.getContents();
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
			composite.setLayout(new GridLayout(2, false));
			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        new Label(composite, SWT.NONE).setText("path");
	        imagepath = new Text(composite, SWT.BORDER);
	        imagepath.setEnabled(false);
	        imagepath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			imagesize = new Label(parent, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			imagesize.setLayoutData(gd);
			
			canvas = new FigureCanvas(parent);
			GridData gd2 = new GridData(GridData.FILL_BOTH);
			canvas.setLayoutData(gd2);
			Panel panel=new Panel();
			//panel.setBorder(new LineBorder(1));
			panel.setLayoutManager(new FlowLayout());
			canvas.setContents(panel);
			//final ImageFigure imagefigure = new ImageFigure();
			imagefigure = new ImageFigure();
			panel.add(imagefigure);		

			Composite compositeRectangleGroup = new Composite(parent, SWT.NONE);
			compositeRectangleGroup.setLayout(new GridLayout(2, true));
			compositeRectangleGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
	        createRectangleGroup(compositeRectangleGroup, "normal");
	        //createRectangleGroup(compositeRectangleGroup, "hover");
	        //createRectangleGroup(compositeRectangleGroup, "small");
	        //createRectangleGroup(compositeRectangleGroup, "small hover");
			
			canvas.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					// TODO Auto-generated method stub
//					for (Entry<Group, Rectangle> entry : rectangleMap.entrySet()) {
//						e.gc.drawRectangle(entry.getValue());
//					}
//					if(check.getSelection()){
						int x =Integer.parseInt(spinnerX.getText());
						int y =Integer.parseInt(spinnerY.getText());
						int w =Integer.parseInt(spinnerW.getText());
						int h =Integer.parseInt(spinnerH.getText());
						e.gc.drawRectangle(x, y, w, h);
//					}
				}
			});
		}
		
		private Spinner createSpinner(Composite parent, String text){
			new Label(parent, SWT.CENTER).setText(text);
			Spinner spinner = new Spinner(parent, SWT.BORDER);
			spinner.setMaximum(0);
			return spinner;
		}
		
		private void createRectangleGroup(Composite parent, String text){
			final Group rectgroup = new Group(parent, SWT.NONE);
			rectgroup.setText("Trimming");
			GridData gd = new GridData();
			rectgroup.setLayoutData(gd);
			rectgroup.setLayout(new GridLayout(4, false));
			
			spinnerX = createSpinner(rectgroup, "x");
			spinnerY = createSpinner(rectgroup, "y");
			spinnerW = createSpinner(rectgroup, "width");
			spinnerH = createSpinner(rectgroup, "height");
			
			ModifyListener modify = new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					// TODO Auto-generated method stub
					//int x =Integer.parseInt(spinnerX.getText());
					//int y =Integer.parseInt(spinnerY.getText());
					//int w =Integer.parseInt(spinnerW.getText());
					//int h =Integer.parseInt(spinnerH.getText());
					//Rectangle rec = new Rectangle(x, y, w, h);
					//rectangleMap.put(rectgroup, rec);
					canvas.redraw();
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
}
