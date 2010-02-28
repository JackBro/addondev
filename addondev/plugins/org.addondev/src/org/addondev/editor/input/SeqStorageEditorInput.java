package org.addondev.editor.input;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class SeqStorageEditorInput implements IStorageEditorInput {

	//private IStorage fStorage;
	private String fName, fPath, fText;
	
	
	public SeqStorageEditorInput(String name, String path, String text) {
		super();
		fName = name;
		fPath = path;
		fText = text;
		//this.fStorage = fStorage;
	}

	@Override
	public IStorage getStorage() throws CoreException {
		// TODO Auto-generated method stub
		return new IStorage() {
			
			@Override
			public Object getAdapter(Class adapter) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean isReadOnly() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return fName;
			}
			
			@Override
			public IPath getFullPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public InputStream getContents() throws CoreException {
				// TODO Auto-generated method stub
				//return new StringBufferInputStream(fText);
				return new ByteArrayInputStream(fText.getBytes());
			}
		};
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
		return fName;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return fPath;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}
