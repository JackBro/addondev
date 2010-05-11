package gef.example.helloworld.figure;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;

public class TabBoxFigure extends Figure {
	  private Figure canvas;
	  private Figure tabs;

	  public TabBoxFigure() {
	     setLayoutManager(new BorderLayout());
	     this.canvas = new Figure();
	     this.canvas.setLayoutManager(new StackLayout());
	     this.canvas.setPreferredSize(150, 100);
	     this.tabs = new Figure();
	     this.tabs.setPreferredSize(50, 20);
	     this.tabs.setBackgroundColor(ColorConstants.green);
	     this.tabs.setOpaque(true);
	     ToolbarLayout layout = new ToolbarLayout();
	     layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
	     layout.setVertical(false);
	     //layout.setStretchMinorAxis(false);
	     this.tabs.setLayoutManager(layout);
	     add(this.canvas,BorderLayout.CENTER);
	     add(this.tabs,BorderLayout.TOP);
	  }

	  public IFigure getCanvas() {
	    return this.canvas;
	  }
	 
	  public IFigure getTabs() {
	    return this.tabs;
	 }
}
