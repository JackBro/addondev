package org.addondev.ui.editor.manifest;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class InstallMasterDetailsBlock extends MasterDetailsBlock {

	IEditorInput input;
	private IManagedForm fManagedForm;
	private TreeViewer fTreeViewer;
	
	public InstallMasterDetailsBlock(IEditorInput input) {
		super();
		// TODO Auto-generated constructor stub
		this.input = input;
	}

	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		// TODO Auto-generated method stub
		fManagedForm = managedForm;
		
		FormToolkit toolkit =  managedForm.getToolkit();
		
		Composite composite = toolkit.createComposite(parent);
		composite.setLayout(new GridLayout(1, false));			
		
		Section section = toolkit.createSection(composite, Section.TITLE_BAR );
		section.setText("overlay");
		
		Composite viewercomposite = toolkit.createComposite(composite, SWT.NONE);
		viewercomposite.setLayout(new GridLayout(2, false));
		viewercomposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Tree tree = toolkit.createTree(viewercomposite, SWT.BORDER);
		fTreeViewer = new TreeViewer(tree);
		//fTreeViewer.setContentProvider(new XULTreeContentProvider());
		//fTreeViewer.setLabelProvider(new XULLabelProvider());
		
		MenuManager menu_manager = new MenuManager();
		
		//final Action addmenu = new AddAction("add");
		//menu_manager.add(addmenu);

	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub

	}

}
