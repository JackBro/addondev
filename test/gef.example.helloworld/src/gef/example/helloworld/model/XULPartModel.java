package gef.example.helloworld.model;

public class XULPartModel extends ContentsModel {

	private AbstractElementModel main;
	
	public AbstractElementModel getMain() {
		return main;
	}

	public void setMain(AbstractElementModel main) {
		this.main = main;
	}

	public XULPartModel(AbstractElementModel main) {
		super();
		this.main = main;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
