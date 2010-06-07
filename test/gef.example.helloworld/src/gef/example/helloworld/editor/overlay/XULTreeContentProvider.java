package gef.example.helloworld.editor.overlay;

import gef.example.helloworld.model.AbstractElementModel;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class XULTreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		AbstractElementModel elem = (AbstractElementModel)parentElement;
		
		return elem.getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		AbstractElementModel elem = (AbstractElementModel)element;
		return elem.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		AbstractElementModel elem = (AbstractElementModel)element;
		return elem.getChildren().size()!=0;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		AbstractElementModel elem = (AbstractElementModel)inputElement;
		return elem.getChildren().toArray();
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
