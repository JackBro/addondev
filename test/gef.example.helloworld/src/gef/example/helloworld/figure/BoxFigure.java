package gef.example.helloworld.figure;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

public class BoxFigure extends AbstractElementFigure {

	public BoxFigure() {
		super();
		// TODO Auto-generated constructor stub
		setBorder(new LineBorder(ColorConstants.black,2, Graphics.LINE_DOT));

		Insets padding = new Insets(5, 5, 5, 5);
		MarginBorder marginBorder = new MarginBorder(padding);
		//setBorder(marginBorder);
		
	}

	@Override
	public int getDefaultHeight() {
		// TODO Auto-generated method stub
		int w=0;
		ToolbarLayout tl = (ToolbarLayout) getLayoutManager();
		if(tl.isHorizontal()){		
			List list = getChildren();
			int max = 0;
			if(list.size()>0){
				for (Object obj : list) {
					
					AbstractElementFigure fig = (AbstractElementFigure) obj;
					if(max < fig.getDefaultHeight()){
						max = fig.getDefaultHeight();
					}
				}
				return max;
			}		
		}else{
			List list = getChildren();
			if(list.size()>0){
				for (Object obj : list) {
					AbstractElementFigure fig = (AbstractElementFigure) obj;
					w+=fig.getDefaultHeight();
				}
				return w;
			}
		}
		return 100;
	}

	@Override
	public int getDefaultWidth() {
		// TODO Auto-generated method stub
		int w=0;
		ToolbarLayout tl = (ToolbarLayout) getLayoutManager();
		if(tl.isHorizontal()){		
			List list = getChildren();
			if(list.size()>0){
				for (Object obj : list) {
					AbstractElementFigure fig = (AbstractElementFigure) obj;
					w+=fig.getDefaultWidth();
				}
				return w;
			}		
		}else{
			List list = getChildren();
			int max = 0;
			if(list.size()>0){
				for (Object obj : list) {
					
					AbstractElementFigure fig = (AbstractElementFigure) obj;
					if(max < fig.getDefaultWidth()){
						max = fig.getDefaultWidth();
					}
				}
				return max;
			}			
		}
		return 100;
	}

}
