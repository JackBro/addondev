package gef.example.helloworld.model;

import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class ModelProperty implements Cloneable {

	private boolean isattr;
	private String fName;
	private IPropertyDescriptor fPropertyDescriptor;
	//private boolean fIsSerialize;
	
	public ModelProperty(String name, IPropertyDescriptor propertyDescriptor) {
		super();
		this.fName = name;
		this.fPropertyDescriptor = propertyDescriptor;
		this.isattr = false;
		//this.fIsSerialize = false;
	}

	public boolean isAttr() {
		return isattr;
	}

	public void setAttr(boolean isattr) {
		this.isattr = isattr;
	}
	
	public String getName() {
		return fName;
	}

	public void setName(String fName) {
		this.fName = fName;
	}

	public IPropertyDescriptor getPropertyDescriptor() {
		return fPropertyDescriptor;
	}

	public void setPropertyDescriptor(IPropertyDescriptor fPropertyDescriptor) {
		this.fPropertyDescriptor = fPropertyDescriptor;
	}

//	public boolean isSerialize() {
//		return fIsSerialize;
//	}
//
//	public void setIsSerialize(boolean isserialize) {
//		this.fIsSerialize = isserialize;
//	}
	
	public ModelProperty clone() {
		try {
			return (ModelProperty) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
