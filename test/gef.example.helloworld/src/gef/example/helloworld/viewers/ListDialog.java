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

public class ListDialog extends Dialog {

	protected TableViewer viewer;
	private PropertySheetPage fPropertySheetPage;
	protected Class fClass;
	protected List fValues;
	protected boolean isConst;

	public void setValue(List values) {
		fValues = values;
	}

	public List getValue() {
		return fValues;
	}
	
	public void setClass(Class _class){
		fClass = _class;
	}
	
	public boolean isConst() {
		return isConst;
	}

	public void setConst(boolean isConst) {
		this.isConst = isConst;
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
		isConst = false;
	}
	
	@Override
	protected int getShellStyle() {
		return super.getShellStyle()|SWT.RESIZE|SWT.MAX;
	}

	@Override
	protected Control createDialogArea(Composite parent) {	
		Composite composite = (Composite)super.createDialogArea(parent);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));
		
		SashForm baseSash = new SashForm(composite, SWT.HORIZONTAL);
		baseSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		viewer = new TableViewer(baseSash, SWT.BORDER|SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table = viewer.getTable();
		TableColumn column = new TableColumn(table, SWT.NULL);
		column.setWidth(100);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(getLableProvider());
		viewer.setInput(getValue());
		
		fPropertySheetPage = new PropertySheetPage();
		fPropertySheetPage.createControl(baseSash);
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
		//baseSash.setWeights(new int [] {20,80})
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
		
		Composite buttonComposite = new Composite(composite, SWT.None);
		buttonComposite.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
		buttonComposite.setLayout(new GridLayout(1, false));
		createButtionArea(buttonComposite);

		
//	    // 各カラムの幅を計算する
//	    TableColumn[] columns = table.getColumns();
//	    for(int i = 0; i < columns.length; i++) {
//	      columns[i].pack();
//	    }

		
		return composite;
	}

	protected void createButtionArea(Composite parent){
		Button fAddButton = new Button(parent, SWT.NONE);
		fAddButton.setText("Add");
		fAddButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
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
	
	protected ViewLableProvider getLableProvider(){
		return new ViewLableProvider();
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
