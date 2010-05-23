package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;
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

public class ListDialog extends Dialog {

	private TableViewer viewer;
	private PropertySheetPage fPropertySheetPage;
	//private Button fAddButton, fDeleteButton;
	private Class fClass;
	private List fValues;
	//private ListProperty fListproperty;
	
//	public void setValue(ListProperty listproperty) {
//		fListproperty = listproperty;
//		fClass = fListproperty.getClass();
//		fValues = fListproperty.getValues();
//	}
	
	public void setValue(Class _class, List values) {
		//fListproperty = listproperty;
		fClass = _class;
		fValues = values;
	}

	public Object getValue() {
		return fValues;
//		Object o = viewer.getInput();
//		viewer.getTable().get
//		return o;
	}
	
	private Object getInstance(){
		try {
			Object element = fClass.newInstance();
			return element;
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	protected ListDialog(Shell parentShell) {
		super(parentShell);
		//setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX );
	}
	
	@Override
	protected int getShellStyle() {
		// TODO Auto-generated method stub
		return super.getShellStyle()|SWT.RESIZE|SWT.MAX; //super.getShellStyle();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub		
		Composite composite = (Composite)super.createDialogArea(parent);
		
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(3, false));
		
		viewer = new TableViewer(composite, SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn column = new TableColumn(table, SWT.NULL);
		column.setText("");
		column.setWidth(100);
		
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ViewLableProvider());
		viewer.setInput(fValues);
		
		fPropertySheetPage = new PropertySheetPage();
		fPropertySheetPage.createControl(composite);
		
		final PropertySheetEntry en = new PropertySheetEntry();
		en.setPropertySourceProvider(new IPropertySourceProvider() {
			
			@Override
			public IPropertySource getPropertySource(Object object) {
				// TODO Auto-generated method stub
				if(object instanceof IPropertySource){
					IPropertySource src = (IPropertySource) object;
					return src;
				}
				return null;
			}
		});
		fPropertySheetPage.setRootEntry(en);

		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				Object element = sel.getFirstElement();
				if(element instanceof AbstractElementModel){
					en.setValues(new Object[]{element});
				}
				
			}
		});
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		fPropertySheetPage.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		Composite buttonComposite = new Composite(composite, SWT.None);
		buttonComposite.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
		buttonComposite.setLayout(new GridLayout(1, false));		
		createButtions(buttonComposite);

		
//	    // 各カラムの幅を計算する
//	    TableColumn[] columns = table.getColumns();
//	    for(int i = 0; i < columns.length; i++) {
//	      columns[i].pack();
//	    }

		
		return composite;
	}

	protected void createButtions(Composite parent){
		Button fAddButton = new Button(parent, SWT.NONE);
		fAddButton.setText("Add");
		fAddButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Object element = getInstance();
				viewer.add(element);
				fValues.add(element);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		Button fDeleteButton = new Button(parent, SWT.NONE);
		fDeleteButton.setText("Delete");
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
	
	class ViewLableProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			if(element instanceof AbstractElementModel){
				AbstractElementModel model = (AbstractElementModel)element;
				return model.getName();
			}
			return null;
		}
		
	}
}
