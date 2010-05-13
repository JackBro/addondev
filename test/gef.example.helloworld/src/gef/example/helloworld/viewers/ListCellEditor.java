package gef.example.helloworld.viewers;

import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.MenuItemModel;
import gef.example.helloworld.model.MenuListModel;
import gef.example.helloworld.model.MenuPopupModel;
import gef.example.helloworld.model.PrefPanesModel;
import gef.example.helloworld.model.PrefpaneModel;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ListCellEditor extends DialogCellEditor {
	
	private static Map<Class, Class> map = new HashMap<Class, Class>(); 
	static {
		map.put(MenuPopupModel.class, MenuItemModel.class);
		map.put(PrefPanesModel.class, PrefpaneModel.class);
	}
	private Object orgObject;
    public ListCellEditor(Composite parent) {
        this(parent, SWT.NONE);
    }
    
    public ListCellEditor(Composite parent, int style) {
        super(parent, style);
        //doSetValue(new RGB(0, 0, 0));
    }
    
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		// TODO Auto-generated method stub
        ListDialog dialog = new ListDialog(cellEditorWindow.getShell());
        Object value = getValue();
        if(value != null){
        	Class cl = value.getClass();
        	Class val = map.get(cl);
        	List newChildern = new ArrayList();
        	
        	ElementModel elem = (ElementModel)value;
        	List childern = elem.getChildren();
        	for (Object obj : childern) {
        		ElementModel child = (ElementModel)obj;
        		newChildern.add(child.cp());
			}
        	dialog.setValue(val, newChildern);
        }
//        if (value != null && (value instanceof ListProperty)) {
//        	ListProperty property = ((ListProperty)value).cp();
//        	///dialog.setValue(property.getClass(), property.getValues());
//        	dialog.setValue(property);
//		}
        int ret = dialog.open();
        if (ret == IDialogConstants.OK_ID) {
        	List list = (List)dialog.getValue();
        	ElementModel elem = (ElementModel)value;
        	elem.getChildren().clear();
        	elem.getChildren().addAll(list);
        	return elem;
        	//return dialog.getValue();
        }
        else{
        	return null;
        }
	}

	@Override
	protected void updateContents(Object value) {
		// TODO Auto-generated method stub
		super.updateContents(value);
	}
}
