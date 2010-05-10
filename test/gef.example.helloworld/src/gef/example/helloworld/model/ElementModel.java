package gef.example.helloworld.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public abstract class ElementModel extends AbstractModel {
	
	protected List<ElementModel> children = new ArrayList<ElementModel>(); 
	
	public static final String ATTR_FLEX = "flex";
	public static final String ATTR_ID = "id";
	//protected String flex="0";
	
	private Map<Object, ModelProperty> fPropertyMap = new HashMap<Object, ModelProperty>();
	private Map<Object, Object> fAttributeMap = new HashMap<Object, Object>();
	protected void AddProperty(String id, IPropertyDescriptor propertyDescriptor, Object obj){
		
		fPropertyMap.put(id, new ModelProperty(id, propertyDescriptor));
		fAttributeMap.put(id, obj);
		
	}	
	
	public void installModelProperty(){
		AddProperty(ATTR_FLEX, new TextPropertyDescriptor(ATTR_FLEX, ATTR_FLEX), "");
		AddProperty(ATTR_ID, new TextPropertyDescriptor(ATTR_ID, ATTR_ID), "");
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		List<IPropertyDescriptor> propertydescriptors = new ArrayList<IPropertyDescriptor>();
		for (ModelProperty val : fPropertyMap.values()) {
			String name = val.getName();
			IPropertyDescriptor prop = val.getPropertyDescriptor();
			propertydescriptors.add(prop);
		}
		return propertydescriptors.toArray(new IPropertyDescriptor[propertydescriptors.size()]);
		//return super.getPropertyDescriptors();
	}

	@Override
	public Object getPropertyValue(Object id) {
		// TODO Auto-generated method stub
		//return super.getPropertyValue(id);
		return fAttributeMap.get(id);
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		//return super.isPropertySet(id);
		return fPropertyMap.containsKey(id);
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		super.resetPropertyValue(id);
		fPropertyMap.remove(id);
		fAttributeMap.remove(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		super.setPropertyValue(id, value);
		fAttributeMap.put(id, value);
		firePropertyChange(id.toString(), null, value);
	}

	private ContentsModel parent;

	public ElementModel(){
		installModelProperty();
	}

	
	public ContentsModel getParent() {
		return parent;
	}

	public void setParent(ContentsModel parent) {
		this.parent = parent;
	}
	
	public void setConstraint(Rectangle rectangle)
	{
		firePropertyChange("resize", null, rectangle);
	}
	
	public List getChildren() {
		return children; // 子モデルを返す
	}
	
	public void setPreSize(int w, int h){
		
	}
	
	public abstract String getName();
	public String getAttributes(){
		StringBuilder buf= new StringBuilder();
		for (Entry<Object, Object> attr : fAttributeMap.entrySet()) {
			String id = attr.getKey().toString();
			String value = attr.getValue().toString();
			if(value !=null && value.length()>0){
				buf.append(String.format(" %s=\"%s\" ", id, value));
			}
		}	
		return buf.toString();
	}
	public String toXML(){
		StringBuilder buf= new StringBuilder();
		buf.append("<");
		buf.append(getName());
		buf.append(getAttributes());
		if(children.size() > 0){
			buf.append(">");
			buf.append("\n");
			for (ElementModel element : children) {
				buf.append(element.toXML());
			}
			buf.append("</");
			buf.append(getName());
			buf.append(">");
		}else{
			buf.append("/>");
		}
		buf.append("\n");
		return buf.toString();
	}

}
