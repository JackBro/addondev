package gef.example.helloworld.model;

import gef.example.helloworld.viewers.ListProperty;
import gef.example.helloworld.viewers.ListPropertyDescriptor;
import gef.example.helloworld.viewers.MenuPropertyDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public abstract class ElementModel extends AbstractModel {
	
	protected List<ElementModel> children = new ArrayList<ElementModel>(); 
	
	public static final String NAME = "name";
	
	public static final String ATTR_FLEX = "flex";
	public static final String ATTR_ID = "id";
	public static final String ATTR_CLASS = "class";
	public static final String ATTR_INSERTBEFORE = "insertbefore";
	//protected String flex="0";
	
	private Map<Object, ModelProperty> fPropertyMap = new HashMap<Object, ModelProperty>();
	private Map<Object, Object> fAttributeMap = new HashMap<Object, Object>();	
	
	public void AddProperty(String id, IPropertyDescriptor propertyDescriptor, Object obj){
		
		fPropertyMap.put(id, new ModelProperty(id, propertyDescriptor));
		fAttributeMap.put(id, obj);	
	}
	
	public void AddObjProperty(String id, String displayname, Object obj){
		AddProperty(id, new PropertyDescriptor(id, displayname), obj);	
	}
	
	public void AddTextProperty(String id, String displayname, Object obj){
		
		AddProperty(id, new TextPropertyDescriptor(id, displayname), obj);	
	}		
	
	public void AddListProperty(String id, String displayname, ElementModel obj){
		
		AddProperty(id, new ListPropertyDescriptor(id, displayname), obj);	
	}	
	
	public void AddListProperty(String id, String displayname, List obj){
		
		AddProperty(id, new ListPropertyDescriptor(id, displayname), obj);	
	}	
	
	public void AddMenuProperty(String id, String displayname, List obj){
		
		AddProperty(id, new MenuPropertyDescriptor(id, displayname), obj);	
	}
	
	public void installModelProperty(){
		AddObjProperty(NAME, NAME, getName());
		AddTextProperty(ATTR_FLEX, ATTR_FLEX, "");
		AddTextProperty(ATTR_ID, ATTR_ID, "");
	}
	
	public ElementModel getCopy(){
		ElementModel model= null;
		try {
			model = this.getClass().newInstance();
			for (Object id : fPropertyMap.keySet()) {
				ModelProperty prop = fPropertyMap.get(id);
				
				IPropertyDescriptor propdescriptor = prop.getPropertyDescriptor();
				if(propdescriptor instanceof TextPropertyDescriptor){
					Object obj = fAttributeMap.get(id);
					model.AddTextProperty((String) id, propdescriptor.getDisplayName(), obj);
				}else if(propdescriptor instanceof ListPropertyDescriptor){
					//ListProperty listprop = (ListProperty)fAttributeMap.get(id);
					//model.AddListProperty((String) id, propdescriptor.getDisplayName(), 
					//		listprop.getListClass(), listprop.getValues());
				}
			}
			for (Object obj : children) {
				ElementModel child = (ElementModel)obj;
				model.getChildren().add(child.getCopy());
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
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
		return fAttributeMap.get(id);
	}

	@Override
	public boolean isPropertySet(Object id) {
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
	
	public void setChildren(List children) {
		this.children = children; 
	}
	
	public void setPreSize(int w, int h){
		
	}
	
	public abstract String getName();
	public String getAttributes(){
		StringBuilder buf= new StringBuilder();
		for (Entry<Object, Object> attr : fAttributeMap.entrySet()) {
			String id = attr.getKey().toString();
			String value = attr.getValue().toString();
			ModelProperty mprop = fPropertyMap.get(attr.getKey());
			if(fPropertyMap.get(attr.getKey()).getPropertyDescriptor() instanceof TextPropertyDescriptor){
				if(value !=null && value.length()>0){
					buf.append(String.format(" %s=\"%s\" ", id, value));
				}
			}
		}	
		return buf.toString();
	}
	public String getListPropertysXML(){
		StringBuilder buf= new StringBuilder();
		for (ModelProperty val : fPropertyMap.values()) {
//			if(val.getPropertyDescriptor() instanceof ListPropertyDescriptor){
//				ListPropertyDescriptor listprop = (ListPropertyDescriptor)val.getPropertyDescriptor();
//				List<ElementModel> models = (List)getPropertyValue(listprop.getId());
//				for (ElementModel elementModel : models) {
//					
//				}
//				buf.append(model.toXML());
//				buf.append("\n");
//			}
		}	
		return buf.toString();
	}
	public String toXML(){
		return toXML(null);
//		StringBuilder buf= new StringBuilder();
//		buf.append("<");
//		buf.append(getName());
//		buf.append(getAttributes());
//		String listprop = getListPropertysXML();
//		if(children.size() > 0 || listprop.length()>0){
//			buf.append(">");
//			buf.append("\n");
//			buf.append(getListPropertysXML());
//			for (ElementModel element : children) {
//				buf.append(element.toXML());
//			}
//			buf.append("</");
//			buf.append(getName());
//			buf.append(">");
//		}else{
//			buf.append("/>");
//		}
//		buf.append("\n");
//		return buf.toString();
	}
	
	public String toXML(List models){
		StringBuilder buf= new StringBuilder();
		if(getName() != null){
			buf.append("<");
			buf.append(getName());
			buf.append(getAttributes());
		}
	
		//String listprop = getListPropertysXML();
		//if(children.size() > 0 || listprop.length()>0 || (models!=null && models.size()>0)){
		if(children.size() > 0){
			if(getName() != null){
			buf.append(">");
			buf.append("\n");
			}
			
			buf.append(getListPropertysXML());
			for (ElementModel element : children) {
				buf.append(element.toXML());
			}
			if(getName() != null){
			buf.append("</");
			buf.append(getName());
			buf.append(">");
			}
		}else{
			if(getName() != null){
			buf.append("/>");
			}
		}
		buf.append("\n");
		return buf.toString();
	}

}
