package gef.example.helloworld.editor.overlay.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;

public abstract class AbstractXULWizard extends Wizard {

	private FinishPerformSelectionLisner fSelectionListener;
	
	public void setSelectionListener(FinishPerformSelectionLisner fSelectionListener) {
		this.fSelectionListener = fSelectionListener;
	}
	
	protected abstract Object getElement();
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		Event event = new Event();
		event.data = getElement();
		fSelectionListener.finishSelected(event);
		return true;
	}

}
