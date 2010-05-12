package gef.example.helloworld.viewers;

import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.LabelModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class ListDialog extends Dialog {

	private TableViewer viewer;
	private PropertySheetPage fPropertySheetPage;
	private Button fAddButton, fDeleteButton;
	private List<Map<String, String>> value;
	private Object[] fObjects;
	private List fValues;
	
	public void setValue(Object[] objects) {
		fObjects = objects;
		Class<?> type =  fObjects.getClass().getComponentType();
		int i=0;
	}
	
	public void setValue(List<Class> objects) {
		fValues = objects;
		Class cl = fValues.getClass();
		Class cl4 = cl.getComponentType();
		Class cl2 = cl.getClass();
		Class<?> type =  fValues.getClass().getComponentType();
		Class cl3 = objects.getClass();
		int i=0;
	}

	public List<Map<String, String>> getValue() {
		return value;
	}
	
	private Object getModel(){
		if(fObjects == null) return null;
		//if(fObjects.length == 0) return null;
		try {
			Class cl = fObjects[0].getClass();
			Object element = cl.newInstance();
			//viewer.add(element);
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

		//GridLayout layout= new GridLayout();
		//layout.numColumns= 2;
		//layout.marginWidth= 0;
		//layout.marginHeight= 0;
		//composite.setLayout(layout);
		
		//GridData gd= new GridData();
		//gd.grabExcessVerticalSpace = true;
		//gd.grabExcessHorizontalSpace = true;
		//gd.verticalAlignment= GridData.FILL_VERTICAL;
		//gd.horizontalAlignment= GridData.FILL_HORIZONTAL;
		//composite.setLayoutData(gd);
		
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(3, false));
		
		viewer = new TableViewer(composite, SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
//		List<String> properties = new ArrayList<String>();
//		List<CellEditor> cells = new ArrayList<CellEditor>();	
//		Map<String, String> map = value.get(0);
//		for (String key : map.keySet()) {
//			TableColumn column = new TableColumn(table, SWT.NULL);
//			column.setText(key);
//			column.setWidth(100);
//			properties.add(key);
//			cells.add(new TextCellEditor(viewer.getTable()));
//		}
//		
//		viewer.setColumnProperties(properties.toArray(new String[properties.size()])); 
//		viewer.setCellEditors(cells.toArray(new CellEditor[cells.size()]));
//		viewer.setCellModifier(new ListCellModifier());
		
		//viewer.setContentProvider(new ViewContentProvider());
		
		TableColumn column = new TableColumn(table, SWT.NULL);
		column.setText("");
		column.setWidth(100);
		
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ViewLableProvider());
		viewer.setInput(fObjects);
		
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
				if(element instanceof ElementModel){
					en.setValues(new Object[]{element});
				}
				
			}
		});
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		fPropertySheetPage.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		Composite buttonComposite = new Composite(composite, SWT.None);
		buttonComposite.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
		buttonComposite.setLayout(new GridLayout(1, false));		
		
		fAddButton = new Button(buttonComposite, SWT.NONE);
		fAddButton.setText("Add");
		fAddButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Object element =  getModel();
				viewer.add(element);
//				try {
//					Class cl = getValueClass();
//					Object element = cl.newInstance();
//					viewer.add(element);
//				} catch (InstantiationException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IllegalAccessException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		fDeleteButton = new Button(buttonComposite, SWT.NONE);
		fDeleteButton.setText("Delete");
		
//	    // 各カラムの幅を計算する
//	    TableColumn[] columns = table.getColumns();
//	    for(int i = 0; i < columns.length; i++) {
//	      columns[i].pack();
//	    }

		
		return composite;
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
			if(element instanceof ElementModel){
				//Map<String, String> map = (Map<String, String>)element;
				ElementModel model = (ElementModel)element;
				return model.getName();
			}
			return null;
		}
		
	}

	class ViewContentProvider implements IStructuredContentProvider{

		@Override
		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
			return value.toArray(new Map[value.size()]);
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public class ListCellModifier implements ICellModifier {

		@Override
		public boolean canModify(Object element, String property) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Object getValue(Object element, String property) {
			// TODO Auto-generated method stub
			
			Map<String, String> map = (Map<String, String>)element;
			if (map.containsKey(property)){
				return map.get(property);
			}
			return null;
		}

		@Override
		public void modify(Object element, String property, Object value) {
			// TODO Auto-generated method stub
			if(element instanceof TableItem){
				Map<String, String> map = (Map<String, String>)((TableItem)element).getData();
				map.put(property, (String) value);
				viewer.update(element, null);	
				viewer.refresh();
			}
			//Map<String, String> map = (Map<String, String>)element;
		}
	}
}
