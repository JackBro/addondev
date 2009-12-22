package org.addondev.editor.xul;

import java.util.ArrayList;
import java.util.List;

import jp.aonir.fuzzyxml.FuzzyXMLDocument;
import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;
import jp.aonir.fuzzyxml.FuzzyXMLParser;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class XULOutlinePage extends ContentOutlinePage {

	private XULEditor fEditor;
	private XMLInput input = new XMLInput();
	private int fElementStartOffset;
	TreeViewer tree;
	  
	private class XMLInput 
	{
	    FuzzyXMLElement documentElement;
	}
	
	public XULOutlinePage(XULEditor editor)
	{
		fEditor = editor;
	}
	
	public int getElementStartOffset()
	{
		return fElementStartOffset;
	}
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createControl(parent);
		
		tree = getTreeViewer();
		tree.setContentProvider(new ITreeContentProvider(){

			@Override
			public Object[] getChildren(Object parentElement) {
				// TODO Auto-generated method stub
				if(parentElement==null){
					return new Object[0];
				}
				FuzzyXMLNode[] children = ((FuzzyXMLElement)parentElement).getChildren();
				List<FuzzyXMLNode> elements = new ArrayList<FuzzyXMLNode>();
				for(int i=0;i<children.length;i++){
					if(children[i] instanceof FuzzyXMLElement){
						elements.add(children[i]);
					}
				}
				return elements.toArray(new FuzzyXMLElement[elements.size()]);
			}

			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return ((FuzzyXMLElement)element).getParentNode();
			}

			@Override
			public boolean hasChildren(Object element) {
				// TODO Auto-generated method stub
				return getChildren(element).length > 0;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				//return getChildren(((FuzzyXMLElement)inputElement));
				return getChildren(((XMLInput)inputElement).documentElement);
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		tree.setLabelProvider(new LabelProvider(){

			@Override
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				// TODO Auto-generated method stub
				if(element instanceof FuzzyXMLElement){
					return ((FuzzyXMLElement)element).getName();
				}
				return null;
			}
			
		});
		
		tree.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
		        IStructuredSelection sel = (IStructuredSelection)event.getSelection();
		        Object element = sel.getFirstElement();
		        if(element instanceof FuzzyXMLNode){
		        	FuzzyXMLNode node = (FuzzyXMLNode)element;
		        	
		        	String xml = node.toXMLString();
		        	int i=0;
		        	i++;
		        }
			}				
		});
		
		tree.setInput(input);
		update(-1);
	}

	public void update(int offset)
	{
		IDocument doc = fEditor.getDocumentProvider().getDocument(fEditor.getEditorInput());
		FuzzyXMLDocument document = new FuzzyXMLParser().parse(doc.get());
		input.documentElement = document.getDocumentElement();
		if(offset >= 0)
		{
			FuzzyXMLElement element = document.getElementByOffset(offset);
			tree.setSelection(new StructuredSelection(element));
			//tree.setSelection(new sel);
		}
		fElementStartOffset = input.documentElement.getOffset();
		getTreeViewer().refresh();
	}		  
}
