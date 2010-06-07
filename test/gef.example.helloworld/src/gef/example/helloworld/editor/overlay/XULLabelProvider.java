package gef.example.helloworld.editor.overlay;

import gef.example.helloworld.model.AbstractElementModel;

import org.eclipse.jface.viewers.LabelProvider;

public class XULLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		AbstractElementModel elem = (AbstractElementModel)element;
		String name = elem.getName();
		return name;
		//return super.getText(element);
	}

}
