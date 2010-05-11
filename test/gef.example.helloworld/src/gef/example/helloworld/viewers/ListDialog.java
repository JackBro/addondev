package gef.example.helloworld.viewers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
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

public class ListDialog extends Dialog {

	private TableViewer viewer;
	private Button fAddButton, fDeleteButton;
	private List<Map<String, String>> value;
	
	public void setValue(List<Map<String, String>> value) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		for (Map<String, String> src : value) {
			Map<String, String> dis = new HashMap<String, String>();
			dis.putAll(src);
			list.add(dis);
		}
		this.value = list;
	}

	public List<Map<String, String>> getValue() {
		return value;
	}
	
	protected ListDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX );
		//setShellStyle(getShellStyle() | SWT.RESIZE);
	}

//	@Override
//	protected boolean isResizable() {
//		// TODO Auto-generated method stub
//		return true;
//	}

	@Override
	protected int getShellStyle() {
		// TODO Auto-generated method stub
		return super.getShellStyle()|SWT.RESIZE|SWT.MAX; //super.getShellStyle();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		//return super.createDialogArea(parent);		
		Composite composite = (Composite)super.createDialogArea(parent);
		//composite.setLayout(new FillLayout(SWT.VERTICAL));

		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		//layout.marginWidth= 0;
		//layout.marginHeight= 0;
		//composite.setLayout(layout);
		
		GridData gd= new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment= GridData.FILL_VERTICAL;
		gd.horizontalAlignment= GridData.FILL_HORIZONTAL;
		//composite.setLayoutData(gd);
		
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));
		
		viewer = new TableViewer(composite, SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		List<String> properties = new ArrayList<String>();
		List<CellEditor> cells = new ArrayList<CellEditor>();	

		
		Map<String, String> map = value.get(0);
		for (String key : map.keySet()) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(key);
			column.setWidth(100);
			properties.add(key);
			cells.add(new TextCellEditor(viewer.getTable()));
		}
		
		viewer.setColumnProperties(properties.toArray(new String[properties.size()])); 
		viewer.setCellEditors(cells.toArray(new CellEditor[cells.size()]));
		viewer.setCellModifier(new ListCellModifier());
		
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLableProvider());
		viewer.setInput(value);
		
		fAddButton = new Button(composite, SWT.NONE);
		fAddButton.setText("Add");
		
		fDeleteButton = new Button(composite, SWT.NONE);
		fDeleteButton.setText("Delete");
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
//	    // 各カラムの幅を計算する
//	    TableColumn[] columns = table.getColumns();
//	    for(int i = 0; i < columns.length; i++) {
//	      columns[i].pack();
//	    }

		
		return composite;
	}
	
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		// TODO Auto-generated method stub
//		GridLayout layout= (GridLayout) parent.getLayout();
//		layout.numColumns++;
//		layout.makeColumnsEqualWidth= false;
//		Label label= new Label(parent, SWT.NONE);
//		GridData data= new GridData();
//		data.widthHint= layout.horizontalSpacing;
//		label.setLayoutData(data);
//		super.createButtonsForButtonBar(parent);
//	}

	class ViewLableProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			if(element instanceof Map){
				Map<String, String> map = (Map<String, String>)element;
				
				String text = viewer.getTable().getColumn(columnIndex).getText();
				if(map.containsKey(text)){
					return map.get(text);
				}
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
