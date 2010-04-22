package gef.example.helloworld;

import gef.example.helloworld.editparts.MyEditPartFactory;
import gef.example.helloworld.model.ContentsModel;
import gef.example.helloworld.model.HBoxModel;
import gef.example.helloworld.model.HelloModel;
import gef.example.helloworld.model.VBoxModel;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
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

public class HelloWorldEditor extends GraphicalEditorWithPalette {

	public HelloWorldEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		// 最上位のモデルの設定
//		ContentsModel parent = new ContentsModel();
//
//		HelloModel child1 = new HelloModel();
//		// 制約の設定
//		child1.setConstraint(new Rectangle(0, 0, -1, -1));
//		parent.addChild(child1);
//
//		HelloModel child2 = new HelloModel();
//		child2.setConstraint(new Rectangle(30, 30, -1, -1));
//		parent.addChild(child2);
//
//		HelloModel child3 = new HelloModel();
//		child3.setConstraint(new Rectangle(10, 80, 80, 50));
//		parent.addChild(child3);

		VBoxModel parent = new VBoxModel();
		//HBoxModel parent = new HBoxModel();
		HelloModel child1 = new HelloModel();
		child1.setParent(parent);
		// 制約の設定
		child1.setConstraint(new Rectangle(0, 0, -1, -1));
		//parent.addChild(child1);		
		
		viewer.setContents(parent);

	}

	/* (非 Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {

	}

	/* (非 Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {

	}

	/* (非 Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IMarker marker) {

	}

	/* (非 Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	/* (非 Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		// EditPartFactoryの作成と設定
		viewer.setEditPartFactory(new MyEditPartFactory());

	}

	/* (非 Javadoc)
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

		ImageDescriptor descriptor =
		  ImageDescriptor.createFromFile(
			HelloWorldEditor.class,
			"newModel.gif");

		// 'モデルの作成'ツールの作成と追加
		CreationToolEntry creationEntry =
		  new CreationToolEntry(
			"HelloModelの作成", // パレットに表示される文字列
			"モデル作成", // ツールチップ
			new SimpleFactory(HelloModel.class), // モデルを作成するファクトリ
			descriptor, // パレットに表示する16X16のイメージ
			descriptor);// パレットに表示する24X24のイメージ
		drawer.add(creationEntry);
		
		// 'モデルの作成'ツールの作成と追加
		CreationToolEntry creationVBoxEntry =
		  new CreationToolEntry(
			"VBoxの作成", // パレットに表示される文字列
			"モデル作成", // ツールチップ
			new SimpleFactory(VBoxModel.class), // モデルを作成するファクトリ
			descriptor, // パレットに表示する16X16のイメージ
			descriptor);// パレットに表示する24X24のイメージ
		drawer.add(creationVBoxEntry);
		
		// 'モデルの作成'ツールの作成と追加
		CreationToolEntry creationHBoxEntry =
		  new CreationToolEntry(
			"HBoxの作成", // パレットに表示される文字列
			"モデル作成", // ツールチップ
			new SimpleFactory(HBoxModel.class), // モデルを作成するファクトリ
			descriptor, // パレットに表示する16X16のイメージ
			descriptor);// パレットに表示する24X24のイメージ
		drawer.add(creationHBoxEntry);

		// 作成した2つのグループをルートに追加
		root.add(toolGroup);
		root.add(drawer);

		return root;
	}

}
