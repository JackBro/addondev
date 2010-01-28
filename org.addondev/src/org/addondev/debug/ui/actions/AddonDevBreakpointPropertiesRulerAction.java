package org.addondev.debug.ui.actions;

import org.addondev.debug.core.model.AddonDevLineBreakpoint;
import org.addondev.debug.ui.model.AddonDevDebugModelPresentation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IUpdate;

public class AddonDevBreakpointPropertiesRulerAction extends Action implements IUpdate{

	private ITextEditor fEditor;
	private IVerticalRulerInfo fRuler;
	private IBreakpoint fBreakpoint;
	
	public AddonDevBreakpointPropertiesRulerAction(ITextEditor editor, IVerticalRulerInfo rulerInfo) {
		// TODO Auto-generated constructor stub
		fEditor = editor;
		fRuler = rulerInfo;
		setText("BreakpointProperties");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
        if (fBreakpoint != null) {
            PropertyDialogAction action = new PropertyDialogAction(fEditor.getEditorSite(), new ISelectionProvider() {
                public void addSelectionChangedListener(ISelectionChangedListener listener) {
                }

                public ISelection getSelection() {
                    return new StructuredSelection(fBreakpoint);
                }

                public void removeSelectionChangedListener(ISelectionChangedListener listener) {
                }

                public void setSelection(ISelection selection) {
                }
            });
            action.run();
        }
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		int line = fRuler.getLineOfLastMouseButtonActivity()+1;
		IResource resource = BreakpointRulerAction.getResourceForDebugMarkers(fEditor);
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(AddonDevDebugModelPresentation.DEBUG_MODEL_ID);
		for (IBreakpoint ibreakpoint : breakpoints) {
			if(ibreakpoint instanceof AddonDevLineBreakpoint)
			{
				AddonDevLineBreakpoint breakpoint = (AddonDevLineBreakpoint)ibreakpoint;
				if(breakpoint.getLocation().equals(resource.getLocation()))	
				{
					int bline = -1;
					try {
						bline = breakpoint.getLineNumber();
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(bline  == line)
					{
						fBreakpoint = ibreakpoint;
						return;
					}
				}
			}
		}
		fBreakpoint = null;
	}

}
