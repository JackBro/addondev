package gef.example.helloworld;

import java.util.EventObject;
import java.util.List;

import gef.example.helloworld.editparts.MyEditPartFactory;
import gef.example.helloworld.editparts.tree.TreeEditPartFactory;
import gef.example.helloworld.model.*;
import gef.example.helloworld.parser.XULLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;


public class HelloWorldEditor extends GraphicalEditorWithPalette {

	class MyContentOutlinePage extends ContentOutlinePage {

		public MyContentOutlinePage() {
			// GEFツリービューワを使用する
			super(new TreeViewer());
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			super.createControl(parent);
			EditPartViewer viewer = getViewer();
			GraphicalViewer gviewer = getGraphicalViewer();
			viewer.setEditDomain(gviewer.getEditDomain());
			getViewer().setEditPartFactory(new TreeEditPartFactory());
			//viewer.setContextMenu(new OutlineContextMenuProvider());
			viewer.setContents(root);
			getSelectionSynchronizer().addViewer(getViewer());
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
			getSelectionSynchronizer().removeViewer(getViewer());
			super.dispose();
		}
	}

	private RootModel root;
	//private XULRootModel xulroot;
	//private XULPartModel xulpart;
	private MyContentOutlinePage outline;

	public HelloWorldEditor() {

		setEditDomain(new DefaultEditDomain(this));
	}

	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		root = new RootModel();
		// viewer.setContents(root);

		// XULRootModel xulroot = new XULRootModel();
		//xulroot = new XULRootModel();
		//xulpart = new XULPartModel(xulroot);
		// xulpart.AddObjProperty("root", "", xulroot);

		//root.addChild(xulpart);
		//root.addChild(xulroot);
		//root.addChild(xulpart);
		// xulroot.addChild(new WindowModel());

		viewer.setContents(root);

		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			// root.removeAllChild();
			try {
				XULLoader.loadXUL(file.getContents(), root);
				if (root.getChildren().size() > 0) {
					// viewer.setContents(root.getChildren().get(0));
					viewer.setContents(root);
				} else {
					WindowModel child1 = new WindowModel();
					root.addChild(child1);
					viewer.setContents(root);
				}
				// getGraphicalViewer().getRootEditPart().getContents().refresh();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void doSave(IProgressMonitor monitor) {

		// RootModel parent = (RootModel) getGraphicalViewer().getContents()
		// .getModel();
		// ElementModel model = (ElementModel) parent.getChildren().get(0);
		// String mm = model.toXML();
		//AbstractElementModel model = (AbstractElementModel) xulroot.getChildren().get(0);
		// String mm = model.toXML(xulpart.getChildren());
		//String mm = model.toXML();
		String mm = root.toXML();
		getCommandStack().markSaveLocation();
	}

	public void doSaveAs() {

	}

	public void gotoMarker(IMarker marker) {

	}

	public boolean isDirty() {
		// return false;
		return getCommandStack().isDirty();
	}

	@Override
	public void commandStackChanged(EventObject event) {
		// TODO Auto-generated method stub
		firePropertyChange(IEditorPart.PROP_DIRTY);

		super.commandStackChanged(event);
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		// EditPartFactoryの作成と設定
		viewer.setEditPartFactory(new MyEditPartFactory());

	}

	protected PaletteRoot getPaletteRoot() {
		// パレットのルート
		PaletteRoot root = new PaletteRoot();

		// モデル作成以外のツールを格納するグループ
		PaletteGroup toolGroup = new PaletteGroup("ツール");

		// '選択' ツールの作成と追加
		ToolEntry tool = new SelectionToolEntry();
		toolGroup.add(tool);
		root.setDefaultEntry(tool); // デフォルトでアクティブになるツール

		// '囲み枠' ツールの作成と追加
		tool = new MarqueeToolEntry();
		toolGroup.add(tool);

		// モデルの作成を行うツールを格納するグループ
		PaletteDrawer drawer = new PaletteDrawer("作成");

		ImageDescriptor descriptor = ImageDescriptor.createFromFile(
				HelloWorldEditor.class, "newModel.gif");

		addCreationToolEntry(drawer, ButtonModel.class, "Button", "モデル作成", descriptor);	
		addCreationToolEntry(drawer, CheckBoxModel.class, "CheckBox", "モデル作成", descriptor);	
		addCreationToolEntry(drawer, ListBoxModel.class, "ListBox", "モデル作成", descriptor);
		addCreationToolEntry(drawer, LabelModel.class, "Label", "モデル作成", descriptor);
		addCreationToolEntry(drawer, VBoxModel.class, "VBox", "モデル作成", descriptor);
		addCreationToolEntry(drawer, HBoxModel.class, "HBox", "モデル作成", descriptor);
		addCreationToolEntry(drawer, GridModel.class, "Grid", "モデル作成", descriptor);
		addCreationToolEntry(drawer, GroupBoxModel.class, "GroupBox", "モデル作成", descriptor);
		addCreationToolEntry(drawer, RadioModel.class, "Radio", "モデル作成",descriptor);
		addCreationToolEntry(drawer, RadioGroupModel.class, "RadioGroup","モデル作成", descriptor);
		addCreationToolEntry(drawer, TabBoxModel.class, "TabBox", "モデル作成",descriptor);
		addCreationToolEntry(drawer, MenuListModel.class, "MenuList","モデル作成", descriptor);
		addCreationToolEntry(drawer, TextBoxModel.class, "TextBox", "モデル作成",descriptor);
		addCreationToolEntry(drawer, MenuPopupModel.class, "MenuPopup","モデル作成", descriptor);
		addCreationToolEntry(drawer, StatusbarModel.class, "Statusbar","モデル作成", descriptor);
		addCreationToolEntry(drawer, ToolBoxModel.class, "ToolBox","モデル作成", descriptor);
		//addCreationToolEntry(drawer, MenuModel.class, "Menuの作成","モデル作成", descriptor);
		//addCreationToolEntry(drawer, MenuItemModel.class, "MenuItemの作成","モデル作成", descriptor);
		addCreationToolEntry(drawer, MenubarModel.class, "MenuBar","モデル作成", descriptor);
		addCreationToolEntry(drawer, ColorPickerModel.class, "ColorPicker","モデル作成", descriptor);
		addCreationToolEntry(drawer, SeparatorModel.class, "Separator","モデル作成", descriptor);

		PaletteDrawer datadrawer = new PaletteDrawer("data");
		addCreationToolEntry(datadrawer, PreferencesModel.class, "Preferences","モデル作成", descriptor);
		addCreationToolEntry(datadrawer, TemplateModel.class, "Template","モデル作成", descriptor);
		addCreationToolEntry(datadrawer, CommandSetModel.class, "CommandSet","モデル作成", descriptor);
		addCreationToolEntry(datadrawer, KeySetModel.class, "KeySet","モデル作成", descriptor);
		addCreationToolEntry(datadrawer, StringBundleSetModel.class, "StringBundleSet","モデル作成", descriptor);
		
		// 作成した2つのグループをルートに追加
		root.add(toolGroup);
		root.add(drawer);
		root.add(datadrawer);

		return root;
	}

	private void addCreationToolEntry(PaletteDrawer drawer, Class modelClass,
			String paret, String tooltip, ImageDescriptor descriptor) {
		CreationToolEntry entry = new CreationToolEntry(paret, // パレットに表示される文字列
				tooltip, // ツールチップ
				new SimpleFactory(modelClass), // モデルを作成するファクトリ
				descriptor, // パレットに表示する16X16のイメージ
				descriptor);// パレットに表示する24X24のイメージ
		drawer.add(entry);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part.getSite().getWorkbenchWindow().getActivePage() == null)
			return;

		super.selectionChanged(part, selection);
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			return file.getName();
		}
		return super.getTitle();
	}

	@Override
	public Object getAdapter(Class type) {
		// TODO Auto-generated method stub
	    if (type == IContentOutlinePage.class) {
	    	if(outline ==null){
	    		outline = new MyContentOutlinePage();
	    	}
	        return outline;
	     }
		return super.getAdapter(type);
	}

}
