package gef.example.helloworld.editor.overlay;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class OverlayMasterBlock extends MasterDetailsBlock {

	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		// TODO Auto-generated method stub
		FormToolkit toolkit =  managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
		section.setText("test");

		Composite client = toolkit.createComposite(section);
		client.setLayout(new FillLayout());
		Tree tree = toolkit.createTree(parent, SWT.NONE);
		TreeViewer viewer = new TreeViewer(tree);
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(labelProvider):
		viewer.setInput(input);
	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		// TODO Auto-generated method stub

	}

}
