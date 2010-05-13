package gef.example.helloworld.viewers;

import gef.example.helloworld.model.ElementModel;

import java.util.ArrayList;
import java.util.List;

public class ListProperty {
	private Class fClass;
	private List<ElementModel> fValues;
	private ElementModel fModel;
	
	public List getValues() {
		return fValues;
	}

	public void setValues(List fValues) {
		this.fValues = fValues;
	}

	public Class getListClass() {
		return fClass;
	}

	public ListProperty(Class fClass, List<ElementModel> fValues) {
		super();
		this.fClass = fClass;
		this.fValues = fValues;
	}
	
	public ListProperty(Class fClass, ElementModel fValues) {
		super();
		this.fClass = fClass;
		this.fModel = fValues;
	}
	
	public ListProperty cp(){
		List<ElementModel> newlist = new ArrayList<ElementModel>();
		for (ElementModel model : fValues) {
			newlist.add(model.cp());
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
