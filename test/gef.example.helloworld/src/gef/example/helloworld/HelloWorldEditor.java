package gef.example.helloworld;

import java.util.EventObject;

import gef.example.helloworld.editparts.MyEditPartFactory;
import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.GridModel;
import gef.example.helloworld.model.GroupBoxModel;
import gef.example.helloworld.model.HBoxModel;
import gef.example.helloworld.model.LabelModel;
import gef.example.helloworld.model.RadioGroupModel;
import gef.example.helloworld.model.RadioModel;
import gef.example.helloworld.model.RootModel;
import gef.example.helloworld.model.TabPanelModel;
import gef.example.helloworld.model.TabBoxModel;
import gef.example.helloworld.model.VBoxModel;
import gef.example.helloworld.model.WindowModel;
import gef.example.helloworld.parser.XULLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;

public class HelloWorldEditor extends GraphicalEditorWithPalette {

	private RootModel root;

	public HelloWorldEditor() {

		setEditDomain(new DefaultEditDomain(this));
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		RootModel root = new RootModel();
		// viewer.setContents(root);

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

	/*
	 * (非 Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {

		RootModel parent = (RootModel) getGraphicalViewer().getContents()
				.getModel();
		ElementModel model = (ElementModel) parent.getChildren().get(0);
		String mm = model.toXML();
		getCommandStack().markSaveLocation();
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {

	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.EditorPart#gotoMarker(org.eclipse.core.resources.
	 * IMarker)
	 */
	public void gotoMarker(IMarker marker) {

	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
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

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		// EditPartFactoryの作成と設定
		viewer.setEditPartFactory(new MyEditPartFactory());

	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#getPaletteRoot()
	 */
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

		// 'モデルの作成'ツールの作成と追加
		CreationToolEntry creationEntry = new CreationToolEntry(
				"HelloModelの作成", // パレットに表示される文字列
				"モデル作成", // ツールチップ
				new SimpleFactory(LabelModel.class), // モデルを作成するファクトリ
				descriptor, // パレットに表示する16X16のイメージ
				descriptor);// パレットに表示する24X24のイメージ
		drawer.add(creationEntry);

		// 'モデルの作成'ツールの作成と追加
		CreationToolEntry creationVBoxEntry = new CreationToolEntry("VBoxの作成", // パレットに表示される文字列
				"モデル作成", // ツールチップ
				new SimpleFactory(VBoxModel.class), // モデルを作成するファクトリ
				descriptor, // パレットに表示する16X16のイメージ
				descriptor);// パレットに表示する24X24のイメージ
		drawer.add(creationVBoxEntry);

		// 'モデルの作成'ツールの作成と追加
		CreationToolEntry creationHBoxEntry = new CreationToolEntry("HBoxの作成", // パレットに表示される文字列
				"モデル作成", // ツールチップ
				new SimpleFactory(HBoxModel.class), // モデルを作成するファクトリ
				descriptor, // パレットに表示する16X16のイメージ
				descriptor);// パレットに表示する24X24のイメージ
		drawer.add(creationHBoxEntry);

		CreationToolEntry creationGridEntry = new CreationToolEntry("Gridの作成", // パレットに表示される文字列
				"モデル作成", // ツールチップ
				new SimpleFactory(GridModel.class), // モデルを作成するファクトリ
				descriptor, // パレットに表示する16X16のイメージ
				descriptor);// パレットに表示する24X24のイメージ
		drawer.add(creationGridEntry);

		CreationToolEntry creationGroupBoxEntry = new CreationToolEntry(
				"GroupBoxの作成", // パレットに表示される文字列
				"モデル作成", // ツールチップ
				new SimpleFactory(GroupBoxModel.class), // モデルを作成するファクトリ
				descriptor, // パレットに表示する16X16のイメージ
				descriptor);// パレットに表示する24X24のイメージ
		drawer.add(creationGroupBoxEntry);

		addCreationToolEntry(drawer, RadioModel.class, "Radioの作成", "モデル作成", descriptor);
		addCreationToolEntry(drawer, RadioGroupModel.class, "RadioGroupの作成", "モデル作成", descriptor);
		addCreationToolEntry(drawer, TabBoxModel.class, "TabBoxの作成", "モデル作成", descriptor);
		addCreationToolEntry(drawer, TabPanelModel.class, "TabPanelの作成", "モデル作成", descriptor);

		// 作成した2つのグループをルートに追加
		root.add(toolGroup);
		root.add(drawer);

		return root;
	}

	private void addCreationToolEntry(PaletteDrawer drawer, Class modelClass, String paret, String tooltip,
			ImageDescriptor descriptor){
		CreationToolEntry entry = new CreationToolEntry(
				paret, // パレットに表示される文字列
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

}
