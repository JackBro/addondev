package gef.example.helloworld.viewers;

import gef.example.helloworld.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ListCellEditor extends DialogCellEditor {
	
	private Class fClass;
	private AbstractElementModel flistenermodel;
	private String id;
//	private static Map<Class, Class> map = new HashMap<Class, Class>(); 
//	static {
//		map.put(MenuPopupModel.class, MenuItemModel.class);
//		map.put(TabPanelsModel.class, TabPanelModel.class);
//		map.put(PrefPanesModel.class, PrefpaneModel.class);
//		map.put(PreferencesModel.class, PreferenceModel.class);
//	}
	
	public void setClass(Class _class) {
		this.fClass = _class;
	}

	public void setListenerModel(AbstractElementModel listenermodel) {
		this.flistenermodel = listenermodel;
	}

	public void setID(String id) {
		this.id = id;
	}
	private ListCellEditor(Composite parent) {
        this(parent, SWT.NONE);
    }
    
    private ListCellEditor(Composite parent, int style) {
        super(parent, style);
    }
    
    public ListCellEditor(Composite parent, Class _class) {
        this(parent, SWT.NONE, _class);
        fClass = _class;
    }
    
    public ListCellEditor(Composite parent, int style, Class _class) {
        super(parent, style);
        fClass = _class;
    }
    
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		// TODO Auto-generated method stub
        ListDialog dialog = new ListDialog(cellEditorWindow.getShell());
        Object value = getValue();
        if(value != null ){
        	List orgChildren = null;
        	if(value instanceof AbstractElementModel)
        		orgChildren = ((AbstractElementModel)value).getChildren();
        	else if(value instanceof List)
        		orgChildren = (List)value;
        	
        	List newChildren = new ArrayList();
        	
        	//AbstractElementModel elem = (AbstractElementModel)value;
        	//List childern = elem.getChildren();
        	for (Object obj : orgChildren) {
        		AbstractElementModel child = (AbstractElementModel)obj;
        		AbstractElementModel newchile = child.getCopy();
        		newChildren.add(newchile);
//        		if(child instanceof ContentsModel){
//        			ContentsModel content = (ContentsModel)child;
//        			((ContentsModel)newchile).setChildren(content.getChildren());
//        		}
			}
        	dialog.setValue(newChildren);
        	dialog.setClass(fClass);
        }
//        if (value != null && (value instanceof ListProperty)) {
//        	ListProperty property = ((ListProperty)value).cp();
//        	///dialog.setValue(property.getClass(), property.getValues());
//        	dialog.setValue(property);
//		}
        int ret = dialog.open();
        if (ret == IDialogConstants.OK_ID) {
        	
        	List list = (List)dialog.getValue();
//        	AbstractElementModel elem = (AbstractElementModel)value;
//
//        	//elem.setChildren(list);
//        	//elem.getChildren().clear();
//        	//elem.getChildren().addAll(list);
//        	//return elem;
//        	if(elem.getParent() != null){
//        		elem.getParent().firePropertyChange(elem.getName(), null, list);
//        	}
        	if(flistenermodel != null){
        		flistenermodel.firePropertyChange(id, null, list);
        		//((AbstractElementModel)value).setChildren(list);
        	}else{
        		if(value instanceof AbstractElementModel){
        			((AbstractElementModel)value).setChildren(list);
        		}else{
        			List orgChildren = (List)value;
        			orgChildren = list;
        		}
        	}
        	
        	//return list;
        	return list;
        }
        else{
        	return null;
        }
	}
}
