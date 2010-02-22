package org.addondev.ui.view;

import org.addondev.ui.AddonDevUIPlugin;
import org.addondev.ui.template.JavaScriptTemplateContextType;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class TemplateView extends ViewPart {

	
	class TemplateFilter extends ViewerFilter
	{

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			// TODO Auto-generated method stub
			if(element instanceof Template)
			{
				Template t = (Template)element;
				String text = fFilterText.getText();
				return t.getPattern().contains(text);
			}
			return false;
		}
		
	}
	
	private Text fFilterText, fPatternText;
	private TableViewer fTableViewer;
	private TemplateFilter tf = new TemplateFilter();
	
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
		
		fFilterText = new Text(composite, SWT.BORDER);
		fFilterText.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.character == SWT.CR)
				{
					fTableViewer.resetFilters();
					fTableViewer.addFilter(tf);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		

		
		SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		fTableViewer = new TableViewer(sashForm, 
				SWT.H_SCROLL | 
                SWT.V_SCROLL | 
                SWT.SINGLE |
                SWT.FULL_SELECTION);
		Table table = fTableViewer.getTable();
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		table.setLayoutData(data);
		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		col1.setText("Name");
		col1.setWidth(100);


		TableColumn col2 = new TableColumn(table, SWT.LEFT);
		col2.setText("Description");
		col2.setWidth(100);
		
		table.setHeaderVisible(true);

		fTableViewer.setContentProvider(new ArrayContentProvider());
		fTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				Object obj = sel.getFirstElement();
				if(obj instanceof Template)
				{
					Template t = (Template)obj;
					fPatternText.setText(t.getPattern());
				}
			}
		});
		fTableViewer.setLabelProvider(new ITableLabelProvider()
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
		
		fTableViewer.setInput(templates);
		
		fPatternText = new Text(sashForm, SWT.V_SCROLL | SWT.H_SCROLL);
		
		
		sashForm.setWeights(new int[] { 1, 1 });

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
