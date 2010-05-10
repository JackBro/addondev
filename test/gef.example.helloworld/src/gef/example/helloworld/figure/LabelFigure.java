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

public class LabelFigure extends ElementFigure {

	private Label label;

	public LabelFigure() {
		super();
		label = new Label();
		//label.setBorder(new CompoundBorder(new LineBorder(), new MarginBorder(3)));
		// 背景色をオレンジに
		label.setBackgroundColor(ColorConstants.orange);
		// 背景色を不透明に
		label.setOpaque(true);
		
		init();
		
		setBorder(new CompoundBorder(new LineBorder(), new MarginBorder(3)));
		// 背景色をオレンジに
		setBackgroundColor(ColorConstants.orange);
		// 背景色を不透明に
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
		return label.getSize().height+8;
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		return label.getSize().width+8;
	}
	
	protected void setImage(Image image){
		label.setIcon(image);
	}
	
	public static Image createImage(String name) {
		if(name == null) return null;
		
		InputStream stream = LabelFigure.class.getResourceAsStream(name);
		Image image = new Image(null, stream);
		try {
			stream.close();
		} catch (IOException ioe) {
		}
		return image;
	}
}
