package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.MenuItemModel;
import gef.example.helloworld.model.MenuModel;
import gef.example.helloworld.model.MenuPopupModel;
import gef.example.helloworld.model.MenuSeparatorModel;
import gef.example.helloworld.viewers.ListDialog.ViewLableProvider;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class MenuDialog extends ListDialog {

//	private TableViewer viewer;
//	private PropertySheetPage fPropertySheetPage;
//	
//	private List fValues;
//	//private Button fAddButton;
//	
//	public List getValue(){
//		return fValues;
//	}
//	
//	public void setValue(List values){
//		fValues = values;
//	}
//	
//	protected MenuDialog(Shell parentShell) {
//		super(parentShell);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	protected int getShellStyle() {
//		// TODO Auto-generated method stub
//		return super.getShellStyle()|SWT.RESIZE|SWT.MAX;
//	}
//
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		// TODO Auto-generated method stub
//		//return super.createDialogArea(parent);
//		Composite composite = (Composite)super.createDialogArea(parent);
//		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		composite.setLayout(new GridLayout(2, false));
//		
//		SashForm baseSash = new SashForm(composite, SWT.HORIZONTAL);
//		baseSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		
//		viewer = new TableViewer(baseSash, SWT.BORDER|SWT.FULL_SELECTION);
//		Table table = viewer.getTable();
//		table = viewer.getTable();
//		TableColumn column = new TableColumn(table, SWT.NULL);
//		column.setWidth(100);
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//		
//
//		viewer.setContentProvider(new ArrayContentProvider());
//		viewer.setLabelProvider(new ViewLableProvider());
//		viewer.setInput(fValues);
//		
//		fPropertySheetPage = new PropertySheetPage();
//		fPropertySheetPage.createControl(baseSash);
//		final PropertySheetEntry en = new PropertySheetEntry();
//		en.setPropertySourceProvider(new IPropertySourceProvider() {
//			
//			@Override
//			public IPropertySource getPropertySource(Object object) {
//				// TODO Auto-generated method stub
//				if(object instanceof IPropertySource){
//					IPropertySource src = (IPropertySource) object;
//					return src;
//				}
//				return null;
//			}
//		});
//		fPropertySheetPage.setRootEntry(en);		
//		//baseSash.setWeights(new int [] {20,80})
//		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
//			
//			@Override
//			public void selectionChanged(SelectionChangedEvent event) {
//				// TODO Auto-generated method stub
//				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
//				Object element = sel.getFirstElement();
//				if(element instanceof AbstractElementModel){
//					en.setValues(new Object[]{element});
//				}
//				
//			}
//		});
//		
//		Composite buttonComposite = new Composite(composite, SWT.None);
//		buttonComposite.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
//		buttonComposite.setLayout(new GridLayout(1, false));		
//		
//	
//
//		
//		return composite;
//	}

	protected MenuDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createButtionArea(Composite parent) {
		// TODO Auto-generated method stub
		//super.createButtionArea(parent);
		
		createButtion(parent, "add menu", MenuModel.class);
		createButtion(parent, "add menupopup", MenuPopupModel.class);
		createButtion(parent, "add menuitem", MenuItemModel.class);
		createButtion(parent, "add menuseparator", MenuSeparatorModel.class);
		
		Button fDeleteButton = new Button(parent, SWT.NONE);
		fDeleteButton.setText("delete");
		fDeleteButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		fDeleteButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
				Object element = sel.getFirstElement();
				viewer.remove(element);
				fValues.remove(element);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void createButtion(Composite parent, String label, final Class instance){
		
		Button button = new Button(parent, SWT.NONE);
		button.setText(label);
		button.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				try {
					Object element = instance.newInstance();
					viewer.add(element);
					fValues.add(element);
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
	}
	
//	class ViewLableProvider extends LabelProvider implements ITableLabelProvider {
//
//		@Override
//		public Image getColumnImage(Object element, int columnIndex) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getColumnText(Object element, int columnIndex) {
//			// TODO Auto-generated method stub
//			if(element instanceof AbstractElementModel){
//				//Map<String, String> map = (Map<String, String>)element;
//				AbstractElementModel model = (AbstractElementModel)element;
//				return model.getName();
//			}
//			return null;
//		}
//		
//	}
}
