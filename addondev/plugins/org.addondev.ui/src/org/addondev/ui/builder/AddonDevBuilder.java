package org.addondev.ui.builder;

import org.addondev.builder.IAddonDevBuilder;
import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.editor.xulmultipageeditor.XULMultiPageEditor;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class AddonDevBuilder implements IAddonDevBuilder {

	private boolean auto, xul, dtd, css;
	private IPreferenceStore fStote;

	public AddonDevBuilder() {
		// TODO Auto-generated constructor stub
		fStote = AddonDevUIPlugin.getDefault().getPreferenceStore();
		auto = fStote.getBoolean(AddonDevUIPrefConst.XULPREVIEW_REFRESH_AUTO);
		xul = fStote.getBoolean(AddonDevUIPrefConst.XULPREVIEW_REFRESH_XUL);
		dtd = fStote.getBoolean(AddonDevUIPrefConst.XULPREVIEW_REFRESH_DTD);
		css = fStote.getBoolean(AddonDevUIPrefConst.XULPREVIEW_REFRESH_CSS);
	}

	@Override
	public void visit(IResourceDelta delta) {
		// TODO Auto-generated method stub
		//if(!auto) return;
		
		switch (delta.getKind()) 
		{
		case IResourceDelta.CHANGED:
//			if(resource instanceof IFile )
//			{
//				IFile file = (IFile)resource;
//				int i=0;
//				getEditorPart(getProject(), file);
//			}
			checkFile(delta.getResource());
			break;
		}		
	}
	
	protected void checkFile(IResource resource) {

		if(resource instanceof IFile )
		{
			IFile file = (IFile)resource;
			if("css".equals(file.getFileExtension()) || "xul".equals(file.getFileExtension()))
			{
				getEditorPart(file.getProject(), file);			
			}
		}
	}
	
	private void getEditorPart(final IProject project, final IFile file)
	{
		//final ArrayList<XULPreviewForm> xulforms = new ArrayList<XULPreviewForm>();
		
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow iWorkbenchWindow : windows) {
			//final IEditorPart editor = iWorkbenchWindow.getActivePage().getActiveEditor();		
			IEditorPart[] editors = iWorkbenchWindow.getActivePage().getEditors();	
			for (final IEditorPart editor : editors) {
				if(editor instanceof XULMultiPageEditor)
				{
					IProject editorproject = ((FileEditorInput)editor.getEditorInput()).getFile().getProject();
					//if(editorproject != null && editorproject.getName().equals(project.getName()))
					if(editorproject != null && editorproject.getName().equals(project.getName()))
					{
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub	
								((XULMultiPageEditor)editor).reLoad();
								//((XULMultiPageEditor)editor).refresh();
							}
						});
					}
				}
			}
		}
	}

}
