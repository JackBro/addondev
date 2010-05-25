package gef.example.helloworld.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

public class PreferencesEditPart extends DataElementEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Label label = new Label();
		label.setText("Preferences");
		return label;
	}

}
