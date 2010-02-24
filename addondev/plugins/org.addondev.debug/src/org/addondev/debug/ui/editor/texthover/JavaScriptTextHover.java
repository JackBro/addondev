package org.addondev.debug.ui.editor.texthover;

import org.addondev.debug.core.model.AddonDevStackFrame;
import org.addondev.ui.editor.hover.IJavaScriptTextHover;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;

public class JavaScriptTextHover implements IJavaScriptTextHover {

	private boolean textHoverEnable = false;
	
	public JavaScriptTextHover(ISourceViewer sourceViewer, String contentType) {
		// TODO Auto-generated constructor stub
        if(IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)){
        	textHoverEnable = true;
        }
	}

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		// TODO Auto-generated method stub
		if(!textHoverEnable) return null;
		
		IAdaptable object = DebugUITools.getDebugContext();
	    IDebugElement context = null;
	    //if (object instanceof IDebugElement) {
	    if (object instanceof AddonDevStackFrame) {
	        context = (AddonDevStackFrame) object;
	    }
	    
	    String text = null;
	    if(context != null){
			
			try {
				text = getTest(textViewer, hoverRegion.getOffset());
				return text;
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    else
	    {
			try {
				text = getTest(textViewer, hoverRegion.getOffset());
				return text;
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return null;
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		// TODO Auto-generated method stub
		return new Region(offset, 0);
	}

	@Override
	public IInformationControlCreator getHoverControlCreator() {
		// TODO Auto-generated method stub
		return new IInformationControlCreator() {
			@Override
			public IInformationControl createInformationControl(Shell parent) {
				// TODO Auto-generated method stub
				return new DefaultInformationControl(parent, EditorsUI.getTooltipAffordanceString());
			}
		};
	}
	
	private String getTest(ITextViewer textViewer, int offset) throws BadLocationException {
		int doclen = textViewer.getDocument().getLength();
		int startOffset = offset;
		while(startOffset >= 0)
		{
			char ch = textViewer.getDocument().getChar(startOffset);
			if(!Character.isJavaIdentifierPart(ch) && ch != '.')
			{
				startOffset++;
				break;
			}
			startOffset--;
		}
		
		int endOffset = offset;
		while(endOffset <= doclen)
		{
			char ch = textViewer.getDocument().getChar(endOffset);
			//if(!Character.isJavaIdentifierPart(ch) && ch != '.')
			if(!Character.isJavaIdentifierPart(ch))
			{
				endOffset--;
				break;
			}
			endOffset++;
		}
		
		if(endOffset > startOffset)
		{
			return textViewer.getDocument().get(startOffset, endOffset-startOffset+1);
		}
		
		return null;
	}

	@Override
	public void setContentType(String contentType) {
		// TODO Auto-generated method stub
		
	}

}
