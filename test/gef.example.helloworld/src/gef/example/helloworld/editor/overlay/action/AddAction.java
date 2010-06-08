package gef.example.helloworld.editor.overlay.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class AddAction extends Action {

	 
	
	public AddAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
		setMenuCreator(new IMenuCreator() {
			
			@Override
			public Menu getMenu(Menu parent) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Menu getMenu(Control parent) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		super.run();
	}

	@Override
	public void setMenuCreator(IMenuCreator creator) {
		// TODO Auto-generated method stub
		super.setMenuCreator(creator);
	}

}
