package gef.example.helloworld.model;

import java.util.ArrayList;
import java.util.List;

public class TreeRowModel extends AbstractElementModel {

	private List treecells; //treecell
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "treerow";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		
		treecells = new ArrayList();
		
	}

}
