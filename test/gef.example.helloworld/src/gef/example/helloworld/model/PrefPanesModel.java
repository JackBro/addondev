package gef.example.helloworld.model;

public class PrefPanesModel extends ElementModel {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "prefnanes";
	}	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "PrefPanes";
	}
	
	public void setDefault(){
		getChildren().add(new PrefpaneModel());
		getChildren().add(new PrefpaneModel());
	}
}
