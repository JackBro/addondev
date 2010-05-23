package gef.example.helloworld.viewers;

import gef.example.helloworld.model.AbstractElementModel;

import java.util.ArrayList;
import java.util.List;

public class ListProperty {
	private Class fClass;
	private List<AbstractElementModel> fValues;
	private AbstractElementModel fModel;
	
	public List getValues() {
		return fValues;
	}

	public void setValues(List fValues) {
		this.fValues = fValues;
	}

	public Class getListClass() {
		return fClass;
	}

	public ListProperty(Class fClass, List<AbstractElementModel> fValues) {
		super();
		this.fClass = fClass;
		this.fValues = fValues;
	}
	
//	public ListProperty(ElementModel parent) {
//		super();
//		this.fModel = fValues;
//	}
	
	public ListProperty cp(){
		List<AbstractElementModel> newlist = new ArrayList<AbstractElementModel>();
		for (AbstractElementModel model : fValues) {
			newlist.add(model.getCopy());
		}
		ListProperty newprop = new ListProperty(fClass, newlist);
		return newprop;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}
