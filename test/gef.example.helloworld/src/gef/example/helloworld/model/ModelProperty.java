package gef.example.helloworld.model;

import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class ModelProperty {

	private String fName;
	private IPropertyDescriptor fPropertyDescriptor;
	
	public ModelProperty(String name, IPropertyDescriptor propertyDescriptor) {
		super();
		this.fName = name;
		this.fPropertyDescriptor = propertyDescriptor;
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
}
