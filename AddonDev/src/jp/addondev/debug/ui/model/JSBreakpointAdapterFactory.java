package jp.addondev.debug.ui.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.ui.texteditor.ITextEditor;

public class JSBreakpointAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		// TODO Auto-generated method stub
		if (adapterType == IToggleBreakpointsTarget.class) {
			return new JSToggleBreakpointAdapter();
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		// TODO Auto-generated method stub
		return new Class[]{IToggleBreakpointsTarget.class};
	}

}
