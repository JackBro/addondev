package org.addondev.ui.preferences;

import org.addondev.ui.AddonDevUIPlugin;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public abstract class SyntaxColorPage {

	private TableViewer fTableViewer;
	private Button fBoldButton;
	
	private SourceViewer fSourceViewer;
	private TextSourceViewerConfiguration fSourceViewerConfiguration;
	
	class ColorElement
	{
		private String fName;
		private String fColorKey;
		private RGB fColorValue;
		
		public ColorElement(String name, String colorkey, RGB colorvalue) {
			this.fName = name;
			this.fColorKey = colorkey;
			this.fColorValue = colorvalue;
		}
		
		public String getName() {
			return fName;
		}

		public void setName(String fName) {
			this.fName = fName;
		}

		public String getColorKey() {
			return fColorKey;
		}

		public void setColorKey(String fColorKey) {
			this.fColorKey = fColorKey;
		}

		public RGB getColorValue() {
			return fColorValue;
		}

		public void setColorValue(RGB fColorValue) {
			this.fColorValue = fColorValue;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return getName();
		}
	}
	
	protected abstract String[][] getColorStrings();
	protected abstract IDocument getDocument();
	protected abstract TextSourceViewerConfiguration getSourceViewerConfiguration();
	
	private ColorElement[] getColorData() {
		String[][] colors = getColorStrings();
		IPreferenceStore store = AddonDevUIPlugin.getDefault().getPreferenceStore();
		ColorElement[] list = new ColorElement[colors.length];
		for (int i = 0; i < colors.length; i++) {
			String displayName = colors[i][0];
			String key = colors[i][1];
			RGB setting = PreferenceConverter.getColor(store, key);
			boolean bold = store.getBoolean(key + AddonDevUIPrefConst.BOLD_SUFFIX);
			boolean italic = store.getBoolean(key + AddonDevUIPrefConst.ITALIC_SUFFIX);
			list[i] = new ColorElement(displayName, key, setting);
		}
		return list;
	}
	public Control createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createElementTable(container);
		createPreview(container);
	
		return container;
	}
	
	private void createElementTable(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, true);
		layout.marginWidth = layout.marginHeight = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(composite, SWT.LEFT);
		label.setText("Element");
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		
		fTableViewer = new TableViewer(composite);
		fTableViewer.setLabelProvider(new LabelProvider());
		fTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		fTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite colorComposite = new Composite(composite, SWT.NONE);
		colorComposite.setLayout(new GridLayout(2, false));
		colorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		label = new Label(colorComposite, SWT.LEFT);
		label.setText("Color ");
		final ColorSelector colorSelector = new ColorSelector(colorComposite);
		Button colorButton = colorSelector.getButton();
		colorButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				ColorElement item = getColorElement(fTableViewer);
				item.setColorValue(colorSelector.getColorValue());
			}
		});
		
		fBoldButton = new Button(colorComposite, SWT.CHECK);
		fBoldButton.setText("Bold");
		gd = new GridData();
		gd.horizontalSpan = 2;
		fBoldButton.setLayoutData(gd);	
		
		fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				ColorElement item = getColorElement(fTableViewer);
				colorSelector.setColorValue(item.getColorValue());
			}
		});
		
		fTableViewer.setInput(getColorData());	
		fTableViewer.setSelection(new StructuredSelection(fTableViewer.getElementAt(0)));
	}
	
	private void createPreview(Composite parent)
	{
		Composite previewComp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		previewComp.setLayout(layout);
		previewComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(previewComp, SWT.NONE);
		label.setText("preview");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));	
		
		fSourceViewer = new SourceViewer(previewComp, null, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		fSourceViewerConfiguration = getSourceViewerConfiguration();

		if (fSourceViewerConfiguration != null)
			fSourceViewer.configure(fSourceViewerConfiguration);

		fSourceViewer.setEditable(false);
		fSourceViewer.getTextWidget().setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));
		fSourceViewer.setDocument(getDocument());

		Control control = fSourceViewer.getControl();
		control.setLayoutData(new GridData(GridData.FILL_BOTH));		
	}
	
	private ColorElement getColorElement(TableViewer viewer) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		return (ColorElement) selection.getFirstElement();
	}
}
