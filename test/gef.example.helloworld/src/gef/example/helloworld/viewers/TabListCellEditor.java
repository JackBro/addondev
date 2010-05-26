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
	private AbstractElementModel flistenermodel;
	private String id;
	
	public void setClass(Class _class) {
		this.fClass = _class;
	}

	public void setListenerModel(AbstractElementModel listenermodel) {
		this.flistenermodel = listenermodel;
	}

	public void setID(String id) {
		this.id = id;
	}

	public TabListCellEditor(Composite parent) {
        this(parent, SWT.NONE);
    }
    
    public TabListCellEditor(Composite parent, int style) {
        super(parent, style);
    }
	
    @Override
	protected Object openDialogBox(Control cellEditorWindow) {
		// TODO Auto-generated method stub
    	ListDialog dialog = new ListDialog(cellEditorWindow.getShell());
    	Object value = getValue();
    	List orgChildren = null;
    	if(value != null && (value instanceof List)){
    		//orgChildren = ((AbstractElementModel)value).getChildren();
    		orgChildren = (List)value;
    	
	    	List newChildren = new ArrayList();
	    	for (Object obj : orgChildren) {
	    		AbstractElementModel orgchild = (AbstractElementModel)obj;
	    		AbstractElementModel newchile = orgchild.getAttrCopy();
	    		newchile.setChildren(orgchild.getChildren());
	    		newChildren.add(newchile);
	    	}
	    	dialog.setValue(newChildren);
	    	dialog.setClass(fClass);
    	}
    	
        int ret = dialog.open();
        if (ret == IDialogConstants.OK_ID) {
        	
        	List list = (List)dialog.getValue();
//        	AbstractElementModel elem = (AbstractElementModel)value;
//        	//elem.setChildren(list);
//        	//elem.getChildren().clear();
//        	//elem.getChildren().addAll(list);
//        	//return elem;
//        	if(elem.getParent() != null){
//        		elem.getParent().firePropertyChange(elem.getName(), null, list);
//        	}
        	
        	flistenermodel.firePropertyChange(id, null, list);
        	//return list;
        	return list;
        }
        else{
        	return null;
        }
	}

}
