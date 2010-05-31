package gef.example.helloworld.figure;

import gef.example.helloworld.HelloworldPlugin;

import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.swt.graphics.Image;

public class ColorPicerFigure extends AbstractElementFigure {

	static final Image COLORPICER = HelloworldPlugin.getDefault().getImageRegistry().get(HelloworldPlugin.IMG_COLORPICKER); //LabelFigure.createImage("colorpicker.png");
	
	private boolean isbutton = true;
	private ImageFigure image;
	private Figure button;
	
	public boolean isButton() {
		return isbutton;
	}

	public void setIsButton(boolean isbutton) {
		this.isbutton = isbutton;
	}

	public ColorPicerFigure() {
		super();
		// TODO Auto-generated constructor stub
		setLayoutManager(new StackLayout());

		button = new Figure();
		button.setBorder(new SimpleRaisedBorder());
		StackLayout layout= new StackLayout();
		button.setLayoutManager(layout);
		Figure color = new Figure();
		color.setBorder(new LineBorder());
		//button.intersects(rect)
		button.add(color);	
		add(button);	
		
		image = new ImageFigure(COLORPICER);
		//image.setOpaque(true);
		add(image);		
		
		upDate();
	}

	public void upDate(){
		//removeAll();
		if(isbutton){
			image.setVisible(false);
			//button.setVisible(true);
		}else{
			//button.setVisible(false);
			image.setVisible(true);
		}
		setPreferredSize(getDefaultWidth(), getDefaultHeight());
	}
	
	@Override
	public int getDefaultHeight() {
		// TODO Auto-generated method stub
		if(isbutton)
			return 22;
		else
			return COLORPICER.getImageData().height;
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		if(isbutton)
			return 36;
		else
			return COLORPICER.getImageData().width;
	}

}
