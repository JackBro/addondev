package gef.example.helloworld.model;

public class ColumnsModel extends AbstractElementModel {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "columns";
	}
	
	public void setDefault(){
		getChildren().add(new ColumnModel());
		//getChildren().add(new ColumnModel());
	}

}
