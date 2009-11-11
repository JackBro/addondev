package jp.addondev.debug.ui.launching;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class LaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		// TODO Auto-generated method stub
		if (selection != null && selection instanceof IStructuredSelection && !selection.isEmpty()) {
			IProject targetProject = null;
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof IProject) {
				targetProject = (IProject) obj;
			} else if (obj instanceof IResource) {
				targetProject = ((IResource) obj).getProject();
			} else if (obj instanceof IAdaptable) {
				targetProject = (IProject) ((IAdaptable) obj).getAdapter(IProject.class);
			}

			if (targetProject != null) {
				launch(targetProject);
			}
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO Auto-generated method stub
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			IProject targetProject = ((IFileEditorInput) input).getFile().getProject();
			launch(targetProject);
		}
	}
	
	/**
	 * build and launch for air application.
	 * @param targetProject
	 */
	private void launch(IProject targetProject) {
		try {
			ILaunchConfiguration config = getLaunchConfiguration(targetProject);
			DebugUITools.launch(config, ILaunchManager.DEBUG_MODE);
		} catch (Exception ex) {
			//AIRPlugin.logException(ex);
		}
	}
	
	/**
	 * create and save for air launch configuration.
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	private ILaunchConfiguration getLaunchConfiguration(IProject project) throws CoreException {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] configs = manager.getLaunchConfigurations();

//		for (int i = 0; i < configs.length; i++) {
//			String value = configs[i].getAttribute(LaunchAIRMainTab.ATTR_PROJECT, "");
//			if (value.equals(project.getName())) {
//				return configs[i];
//			}
//		}

		ILaunchConfigurationType type = manager.getLaunchConfigurationType("jp.javascript.debug.core.launchConfigurationType");//LaunchAIRConfiguration.ID);

		ILaunchConfigurationWorkingCopy wc = type.newInstance(null, manager
				.generateUniqueLaunchConfigurationNameFrom(project.getName()));

//		ScopedPreferenceStore projectStore = new ScopedPreferenceStore(new ProjectScope(project),
//				AIRPlugin.PLUGIN_ID);
//		String descriptor = projectStore.getString(AIRPlugin.PREF_DESCRIPTOR);
//
//		wc.setAttribute(LaunchAIRMainTab.ATTR_PROJECT, project.getName());
//		wc.setAttribute(LaunchAIRMainTab.ATTR_TARGET, descriptor);
		//wc.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);
		//wc.setAttribute(IDebugUIConstants.ID_DEBUG_PERSPECTIVE, false);

		return wc.doSave();
	}
}
