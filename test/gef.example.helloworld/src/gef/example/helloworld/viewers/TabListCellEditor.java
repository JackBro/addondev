package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TabListCellEditor extends DialogCellEditor {
    
	private Class fClass;
	
	public TabListCellEditor(Composite parent, Class _class) {
        this(parent, SWT.NONE, _class);
        fClass = _class;
    }
    
    public TabListCellEditor(Composite parent, int style, Class _class) {
        super(parent, style);
        fClass = _class;
    }
	
    @Override
	protected Object openDialogBox(Control cellEditorWindow) {
		// TODO Auto-generated method stub
    	ListDialog dialog = new ListDialog(cellEditorWindow.getShell());
    	Object value = getValue();
    	List orgChildren = null;
    	if(value != null && (value instanceof AbstractElementModel)){
    		orgChildren = ((AbstractElementModel)value).getChildren();
    	
	    	List newChildren = new ArrayList();
	    	for (Object obj : orgChildren) {
	    		AbstractElementModel orgchild = (AbstractElementModel)obj;
	    		AbstractElementModel newchile = orgchild.getAttrCopy();
	    		newchile.setChildren(orgchild.getChildren());
	    		newChildren.add(newchile);
	    	}
	    	dialog.setValue(fClass, newChildren);
    	}
    	
        int ret = dialog.open();
        if (ret == IDialogConstants.OK_ID) {
        	
        	List list = (List)dialog.getValue();
        	AbstractElementModel elem = (AbstractElementModel)value;
        	//elem.setChildren(list);
        	//elem.getChildren().clear();
        	//elem.getChildren().addAll(list);
        	//return elem;
        	if(elem.getParent() != null){
        		elem.getParent().firePropertyChange(elem.getName(), null, list);
        	}
        	//return list;
        	return null;
        }
        else{
        	return null;
        }
	}

}
