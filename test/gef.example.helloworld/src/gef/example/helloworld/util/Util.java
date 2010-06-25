package gef.example.helloworld.util;

import java.io.IOException;

import gef.example.helloworld.HelloworldPlugin;

import org.addondev.core.AddonDevPlugin;
import org.addondev.util.ChromeURLMap;
import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

public class Util {
	public static IFile getFile(IProject project, String chromeurl){
		ChromeURLMap map = AddonDevPlugin.getDefault().getChromeURLMap(project, false);
		return map.convertChrome2File(chromeurl);
	}
	
	public static IProject getCurrentProject(){
        IEditorPart editor = HelloworldPlugin.getActiveEditorPart();
        FileEditorInput file = (FileEditorInput) editor.getEditorInput();
        IProject project = file.getFile().getProject();
        return project;
	}
	
	public static String getContent(IFile file){
		String text = "";
		try {
			text = FileUtil.getContent(file.getContents());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return text;
	}
}
