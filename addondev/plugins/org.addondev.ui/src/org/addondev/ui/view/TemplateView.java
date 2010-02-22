package org.addondev.ui.view;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.template.JavaScriptTemplateContextType;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class TemplateView extends ViewPart {

	public TemplateView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));	
		
		Text text = new Text(composite, SWT.BORDER);
		
		TableViewer tv = new TableViewer(composite, 
				SWT.H_SCROLL | 
                SWT.V_SCROLL | 
                SWT.BORDER |
                SWT.FULL_SELECTION);
		Table table = tv.getTable();
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		table.setLayoutData(data);
		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		col1.setText("Name");
		 col1.setWidth(100);


		TableColumn col2 = new TableColumn(table, SWT.LEFT);
		col2.setText("Description");
		 col2.setWidth(100);

		
		table.setHeaderVisible(true);

//		tv.setContentProvider(new IContentProvider() {
//			
//			@Override
//			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void dispose() {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		tv.setContentProvider(new ArrayContentProvider());
		tv.setLabelProvider(new ITableLabelProvider()
		{

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				if(element instanceof Template)
				{
					Template t = (Template)element;
	                switch(columnIndex){
	                        case 0:
	                        return t.getName();
	                        case 1:
	                        return t.getDescription();
	                }					
				}
				return null;
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		Template[] templates = AddonDevUIPlugin.getDefault().getTemplateStore().getTemplates(JavaScriptTemplateContextType.JAVASCRIPT_CONTEXT_TYPE);
		
		tv.setInput(templates);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
