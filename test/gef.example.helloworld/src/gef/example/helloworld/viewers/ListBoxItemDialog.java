package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.ListCellModel;
import gef.example.helloworld.model.ListItemModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ListBoxItemDialog extends ListDialog {

	// protected TableViewer viewer;
	private int numcolum;

	public void setNumColum(int numcolum) {
		this.numcolum = numcolum;
	}

	protected ListBoxItemDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object getInstance() {
		// TODO Auto-generated method stub
		Object obj =  super.getInstance();
		if(numcolum == 0)
			return obj;
		else{
			ListItemModel item = (ListItemModel)obj;
			for (int i = 0; i < numcolum; i++) {
				item.getListcells().add(new ListCellModel());
			}
			return item;
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		List<ListItemModel> listitems = getValue();
		if(numcolum == 0){
			for (ListItemModel item : listitems) {
				if(item.getListcells().size() > 0){
					
				}
			}
		}else{
			
		}
		
		Control composite = super.createDialogArea(parent);

		Table table = viewer.getTable();

		List<TextCellEditor> ed = new ArrayList<TextCellEditor>();
		ed.add(new TextCellEditor(table));
		
		List<String> clist = new ArrayList<String>();
		clist.add("0");
		
		int columnum = table.getColumnCount();
		for (int i = 1; i < numcolum; i++) {
			//TableColumn column = new TableColumn(table, SWT.NULL);
			//column.setWidth(100);	
			ed.add(new TextCellEditor(table));
			clist.add(String.valueOf(i));
		}

		//List<ListItemModel> listitems = getValue();

		String[] properties = clist.toArray(new String[clist.size()]);
		viewer.setColumnProperties(properties);

		CellEditor[] editors = ed.toArray(new CellEditor[ed.size()]);
		viewer.setCellEditors(editors);
		viewer.setCellModifier(new MyCellModifier(viewer));
		
		
		

		return composite;
	}

	
	@Override
	protected Object[] getTableViewerSelectionElements(
			AbstractElementModel element) {
		// TODO Auto-generated method stub
		//return super.getTableViewerSelectionElements(element);
		if (numcolum == 0) {
			return super.getTableViewerSelectionElements(element);
		} else {
			ListItemModel listitem = (ListItemModel) element;
			List list = listitem.getListcells();
			return list.toArray();
		}		
	}

	@Override
	protected void createTableColumns(Table table) {
		// TODO Auto-generated method stub
		for (int i = 0; i < numcolum; i++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setWidth(100);	
			//ed.add(new TextCellEditor(table));
			//clist.add(String.valueOf(i));
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);	
	}

	@Override
	protected void createButtionArea(Composite parent) {
		// TODO Auto-generated method stub
		super.createButtionArea(parent);
	}

	@Override
	protected ITableLabelProvider getLableProvider() {
		// TODO Auto-generated method stub
		return new MyLabelProvider();
	}

	class MyLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			String result = "";

			ListItemModel listitem = (ListItemModel) element;
			if (numcolum == 0) {
				result = listitem.getText();
			} else {
				List list = listitem.getListcells();
				if(list.size() > columnIndex)
					result = listitem.getListcells().get(columnIndex).getText();
			}

			return result;
		}
	}

	class MyCellModifier implements ICellModifier {
		private TableViewer viewer;

		public MyCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			ListItemModel item = (ListItemModel) element;
			if(numcolum == 0){
				return item.getText();
			}else{
				
			    return item.getListcells().get(Integer.parseInt(property)).getText();
			}

		    //return null;
		  }

		public void modify(Object element, String property, Object value) {
			if (element instanceof TableItem) {
				element = ((TableItem) element).getData();
			}
			ListItemModel item = (ListItemModel) element;

			if(numcolum == 0){
				item.getText();
				item.setText((String) value);
			}else{
				
			    item.getListcells().get(Integer.parseInt(property)).setText((String) value);
			}
			
//			if (property == "0") {
//				item.setText((String) value);
//			}

			// テーブル・ビューワを更新
			viewer.update(element, null);
		}

	}
}
