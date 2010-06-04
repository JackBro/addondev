package gef.example.helloworld.figure;

import gef.example.helloworld.HelloworldPlugin;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class CheckGroupBoxBorder extends GroupBoxBorder {

	private boolean ischekbox;
	private static Image checkboximage = HelloworldPlugin.getDefault().getImageRegistry().get(HelloworldPlugin.IMG_CHECKBOX);
	
	public boolean isChekBox() {
		return ischekbox;
	}


	public void setChekBox(boolean ischekbox) {
		this.ischekbox = ischekbox;
	}

	@Override
	public void paint(IFigure figure, Graphics g, Insets insets) {
		// TODO Auto-generated method stub
		//super.paint(figure, g, insets);
		
		tempRect.setBounds(getPaintRectangle(figure, insets));
		Rectangle r = tempRect;
		if (r.isEmpty())
			return;

		Rectangle textLoc = new Rectangle(r.getTopLeft(), getTextExtents(figure));
		r.crop(new Insets(getTextExtents(figure).height / 2));
		FigureUtilities.paintEtchedBorder(g, r);

		textLoc.x += getInsets(figure).left;
		if(isChekBox()){
			Color tmp = g.getBackgroundColor();
			//g.setBackgroundColor(checkboxColor);
			g.fillRectangle(textLoc.x-3 , figure.getBounds().y, 16+3, 16-5);
			g.drawImage(checkboximage, textLoc.x, figure.getBounds().y-3);
			g.setBackgroundColor(tmp);
			textLoc.x += 16;
		}
		
		g.setFont(getFont(figure));
		g.setForegroundColor(getTextColor());
		g.clipRect(textLoc);
		g.fillText(getLabel(), textLoc.getTopLeft());
	}

}
