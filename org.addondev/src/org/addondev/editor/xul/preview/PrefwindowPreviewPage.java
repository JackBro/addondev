package org.addondev.editor.xul.preview;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.Page;

public class PrefwindowPreviewPage extends XULPreviewPage{

	//protected Composite displayArea = null;
	TabFolder fTabFolder ;
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		//displayArea = new Composite( parent, SWT.NONE );
		//displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		fTabFolder = new TabFolder(parent, SWT.NONE);
		TabItem item = new TabItem(fTabFolder, SWT.NONE);
		item.setText("---");
		
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return fTabFolder;
		//return displayArea;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocument(List<String> xuls) {
		// TODO Auto-generated method stub
		for (String xul : xuls) {
			
		}
	}

	@Override
	public void setPreviewFile(File file) {
		// TODO Auto-generated method stub
		
	}

}
