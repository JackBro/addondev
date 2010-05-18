package gef.example.helloworld.model;

public class XULPartModel extends ContentsModel {

	private ElementModel main;
	
	public ElementModel getMain() {
		return main;
	}

	public void setMain(ElementModel main) {
		this.main = main;
	}

	public XULPartModel(ElementModel main) {
		super();
		this.main = main;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
