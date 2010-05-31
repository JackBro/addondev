package gef.example.helloworld.figure;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Image;

public abstract class AbstractElementFigure extends Figure {
	public abstract int getDefaultWidth();
	public abstract int getDefaultHeight();
	
	public static Image createImage(String name) {
		if(name == null) return null;
		
		InputStream stream = AbstractElementFigure.class.getResourceAsStream(name);
		Image image = new Image(null, stream);
		try {
			stream.close();
		} catch (IOException ioe) {
		}
		return image;
	}	
}
