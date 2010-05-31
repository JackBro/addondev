package gef.example.helloworld.figure;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Image;

public class LabelFigure extends AbstractElementFigure {

	protected Label label;

	public LabelFigure() {
		super();
		label = new Label();
		label.setBackgroundColor(ColorConstants.orange);
		label.setOpaque(true);
		
		init();
		
		setBorder(new CompoundBorder(new LineBorder(), new MarginBorder(3)));
		setBackgroundColor(ColorConstants.orange);
		setOpaque(true);
		
		ToolbarLayout tl = new ToolbarLayout();
		tl.setStretchMinorAxis(false);
		tl.setVertical(true);
		setLayoutManager(tl);
		add(label);
	}

	protected void init() {
		// TODO Auto-generated method stub
		
	}

	public String getText(){
		return label.getText();
	}
	public void setText(String text){
		label.setText(text);
	}
	
	@Override
	public int getDefaultHeight() {
		// TODO Auto-generated method stub
		//return label.getSize().height+8;
		return 20;
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		return label.getSize().width+8;
	}
	
	protected void setImage(Image image){
		label.setIcon(image);
	}
}
