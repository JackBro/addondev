package gef.example.helloworld.editor.overlay;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gef.example.helloworld.HelloworldPlugin;
import gef.example.helloworld.editor.overlay.action.AddAction;
import gef.example.helloworld.editor.overlay.wizard.ExtentionWizard;
import gef.example.helloworld.model.*;
import gef.example.helloworld.parser.xul.XULLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.SectionPart;

public class OverlayMasterBlock extends MasterDetailsBlock {

	private static Set<Class> registerSet = new HashSet<Class>();
	static{
		registerSet.add(KeySetModel.class);
		registerSet.add(CommandSetModel.class);
		registerSet.add(StatusbarModel.class);
		registerSet.add(MenuPopupModel.class);
		registerSet.add(MenuItemModel.class);
		registerSet.add(PopupModel.class);
	}
	
	private static Map<Class, List<Class>> addmenuMap = new HashMap<Class,List<Class>>();
	static{
		addmenuMap.put(KeySetModel.class, new ArrayList<Class>(Arrays.asList(KeyModel.class)));
		addmenuMap.put(CommandSetModel.class, new ArrayList<Class>(Arrays.asList(CommandModel.class)));
		
		addmenuMap.put(OverlayModel.class, new ArrayList<Class>(Arrays.asList(
			KeySetModel.class, CommandSetModel.class,
			StatusbarModel.class, MenuPopupModel.class)));
		addmenuMap.put(MenuPopupModel.class, new ArrayList<Class>(Arrays.asList(MenuItemModel.class, MenuSeparatorModel.class)));
		addmenuMap.put(StatusbarModel.class, new ArrayList<Class>(Arrays.asList(MenuPopupModel.class)));
	}
	
	private IEditorInput input;
	private AbstractElementModel fCurrentSelection;
	private IManagedForm fManagedForm;
	private TreeViewer fTreeViewer;
	private List<MenuItem> items = new ArrayList<MenuItem>();

	
	public OverlayMasterBlock(IEditorInput input) {
		super();
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
		section.setText("test");
		
		Composite viewercomposite = toolkit.createComposite(composite, SWT.NONE);
		viewercomposite.setLayout(new GridLayout(2, false));
		viewercomposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Tree tree = toolkit.createTree(viewercomposite, SWT.BORDER);
		fTreeViewer = new TreeViewer(tree);
		fTreeViewer.setContentProvider(new XULTreeContentProvider());
		fTreeViewer.setLabelProvider(new XULLabelProvider());
		
		MenuManager menu_manager = new MenuManager();
		
		final Action addmenu = new AddAction("add");
		menu_manager.add(addmenu);
		final Menu m = menu_manager.createContextMenu(fTreeViewer.getTree());
		addmenu.setMenuCreator(new IMenuCreator() {
			
			@Override
			public Menu getMenu(Menu parent) {
				// TODO Auto-generated method stub
				//if(fCurrentSelection == null) return null;
				//return null;
				  Menu menu = new Menu(parent);
				  for (final Class iterable_element : addmenuMap.get(OverlayModel.class)) {
	                  MenuItem item1 = new MenuItem(menu, SWT.NONE);
	                  item1.setText(iterable_element.getSimpleName());
	                  item1.setData(iterable_element);
	                  item1.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							// TODO Auto-generated method stub
							AbstractElementModel model = (AbstractElementModel)(fCurrentSelection);
							try {
								model.addChild((AbstractElementModel) iterable_element.newInstance());
								fTreeViewer.refresh();
							} catch (InstantiationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
							// TODO Auto-generated method stub
							
						}
					});	
	                  
	                  items.add(item1);
				  }
				  

                  return menu;
			}
			
			@Override
			public Menu getMenu(Control parent) {
				// TODO Auto-generated method stub
                  return null;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
		});
		
		fTreeViewer.getTree().setMenu(m);
		m.addMenuListener(new MenuListener() {
			
			@Override
			public void menuShown(MenuEvent e) {
				// TODO Auto-generated method stub
				if(fCurrentSelection == null) return;
				List<Class> classes = addmenuMap.get(fCurrentSelection.getClass());
				for (MenuItem item : items) {	
					item.setEnabled(classes.contains(item.getData()));
				}

			}
			
			@Override
			public void menuHidden(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		//menu_manager.add(copy_action);
		//menu_manager.add(open_action);
		
		final SectionPart part = new SectionPart(section);
		fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				AbstractElementModel model = (AbstractElementModel) ((IStructuredSelection)event.getSelection()).getFirstElement();
				if(fCurrentSelection == model){
					return;
				}
				fCurrentSelection = model;
				fManagedForm.fireSelectionChanged(part, event.getSelection());
			}
		});
		tree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				if(tree.getItem(new Point(e.x, e.y)) == null)
					fTreeViewer.setSelection(null);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		RootModel root = null;
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			root = new RootModel();
			try {
				XULLoader.loadXUL(file.getContents(), root);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fTreeViewer.setInput(root);
		fTreeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		createButtons(viewercomposite);
	}

	private void createButtons(final Composite composite) {
        final Composite buttons = fManagedForm.getToolkit().createComposite(composite);
        GridData layoutData = new GridData();
        layoutData.verticalAlignment = GridData.FILL;
        buttons.setLayoutData(layoutData);
        buttons.setLayout(new GridLayout());
        Button addButton = createButton(buttons, "add");
        addButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//
				ExtentionWizard wiz = new ExtentionWizard();
				Shell shell = HelloworldPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
				WizardDialog dialog = new WizardDialog(shell, wiz);
				int ret = dialog.open();
				if(ret == IDialogConstants.OK_ID){
					AbstractElementModel obj = (AbstractElementModel)wiz.getValue();
					AbstractElementModel model = (AbstractElementModel)((AbstractElementModel)fTreeViewer.getInput()).getChildren().get(0);
					model.addChild(obj);
					fTreeViewer.refresh();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
    private Button createButton(final Composite parent, final String label) {
        Button button = fManagedForm.getToolkit().createButton(
                                parent, 
                                label, 
                                SWT.PUSH);

        GridData layoutData = new GridData();
        layoutData.horizontalAlignment = GridData.FILL;
        button.setLayoutData(layoutData);

        return button;
    }
	
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		for (Class regclass : registerSet) {
			PropertyDetailsPage detailpage = new PropertyDetailsPage();
			detailpage.setPropertyChange(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					// TODO Auto-generated method stub
					fTreeViewer.refresh();
				}
			});
			detailsPart.registerPage(regclass, detailpage);
		}
	}

}
