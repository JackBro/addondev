package org.addondev.debug.core;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class SeqEditorInput implements IEditorInput {

	private String filepath;
	private String url;
	private String fn;
	
	private String filename;
	
	public String getFilepath() {
		return filepath;
	}

	public String getUrl() {
		return url;
	}

	public String getFn() {
		return fn;
	}

	public SeqEditorInput(String filepath, String url, String fn) {
		super();
		this.filepath = filepath;
		this.url = url;
		this.fn = fn;
		
		filename = new File(this.filepath).getName();
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return filename;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return filepath;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}
