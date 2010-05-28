package gef.example.helloworld.model;

import gef.example.helloworld.viewers.ElementComboBoxPropertyDescriptor;
import gef.example.helloworld.viewers.ListPropertyDescriptor;
import gef.example.helloworld.viewers.MenuPropertyDescriptor;
import gef.example.helloworld.viewers.MultiTextPropertyDescriptor;
import gef.example.helloworld.viewers.TabListPropertyDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public abstract class AbstractElementModel extends AbstractModel {
	

	protected List<AbstractElementModel> children = new ArrayList<AbstractElementModel>(); 
	
	public static final String NAME = "name";
	
	public static final String ATTR_FLEX = "flex";
	public static final String ATTR_ID = "id";
	public static final String ATTR_CLASS = "class";
	public static final String ATTR_INSERTBEFORE = "insertbefore";

	
	private Map<Object, ModelProperty> fPropertyMap = new HashMap<Object, ModelProperty>();
	private Map<Object, Object> fAttributeMap = new HashMap<Object, Object>();	
	
	public void AddProperty(String id, IPropertyDescriptor propertyDescriptor, Object obj){
		
		fPropertyMap.put(id, new ModelProperty(id, propertyDescriptor));
		fAttributeMap.put(id, obj);	
	}
	
	public void AddConstProperty(String id, String displayname, Object obj){
		AddProperty(id, new PropertyDescriptor(id, displayname), obj);	
	}
	
	public void AddBoolProperty(String id, String displayname, Boolean defaultvalue){
		AddProperty(id, new ElementComboBoxPropertyDescriptor(id, displayname, new String[]{"true", "false"}), String.valueOf(defaultvalue));	
	}
	
	public void AddAttrProperty(String id, String displayname, Object obj){
		
		AddProperty(id, new TextPropertyDescriptor(id, displayname), obj);	
	}		
	
	public void AddListProperty(String id, String displayname, AbstractElementModel listenermodel, Class _class, AbstractElementModel obj){
		
		AddProperty(id, new ListPropertyDescriptor(id, displayname, listenermodel, _class), obj);	
	}	
	
	public void AddListProperty(String id, String displayname, AbstractElementModel listenermodel, Class _class, List obj){
		
		AddProperty(id, new ListPropertyDescriptor(id, displayname, listenermodel, _class), obj);	
	}
	
	public void AddListProperty(String id, String displayname, Class _class, List obj){
		
		AddProperty(id, new ListPropertyDescriptor(id, displayname, _class), obj);	
	}	
	
	public void AddMenuProperty(String id, String displayname, List obj){
		
		AddProperty(id, new MenuPropertyDescriptor(id, displayname), obj);	
	}
	
	public void AddMenuProperty(String id, String displayname, AbstractElementModel listenermodel, List obj){
		
		AddProperty(id, new MenuPropertyDescriptor(id, displayname, listenermodel), obj);	
	}
	
	public void AddTabListProperty(String id, String displayname, AbstractElementModel listenermodel, Class _class, List obj){
		
		AddProperty(id, new TabListPropertyDescriptor(id, displayname, listenermodel, _class), obj);	
	}
	
	public void AddMultiLineTextProperty(String id, String displayname, String text){
		
		AddProperty(id, new MultiTextPropertyDescriptor(id, displayname), text);	
	}
	
	public void installModelProperty(){
		AddConstProperty(NAME, NAME, getName());
		AddAttrProperty(ATTR_FLEX, ATTR_FLEX, "");
		AddAttrProperty(ATTR_ID, ATTR_ID, "");
	}
	
	public AbstractElementModel getAttrCopy(){
		AbstractElementModel model= null;
		try {
			model = this.getClass().newInstance();
			for (Object id : fPropertyMap.keySet()) {
				ModelProperty prop = fPropertyMap.get(id);
				
				IPropertyDescriptor propdescriptor = prop.getPropertyDescriptor();
				if(propdescriptor instanceof TextPropertyDescriptor){
					Object obj = fAttributeMap.get(id);
					model.AddAttrProperty((String) id, propdescriptor.getDisplayName(), obj);
				}
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
	
	public AbstractElementModel getCopy(){
		AbstractElementModel model= null;
		try {
			model = this.getClass().newInstance();
			for (Object id : fPropertyMap.keySet()) {
				ModelProperty prop = fPropertyMap.get(id);
				
				IPropertyDescriptor propdescriptor = prop.getPropertyDescriptor();
				if(propdescriptor instanceof TextPropertyDescriptor){
					Object obj = fAttributeMap.get(id);
					model.AddAttrProperty((String) id, propdescriptor.getDisplayName(), obj);
				}
			}
			for (Object obj : children) {
				AbstractElementModel child = (AbstractElementModel)obj;
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
		List<IPropertyDescriptor> propertydescriptors = new ArrayList<IPropertyDescriptor>();
		for (ModelProperty val : fPropertyMap.values()) {
			IPropertyDescriptor prop = val.getPropertyDescriptor();
			propertydescriptors.add(prop);
		}
		return propertydescriptors.toArray(new IPropertyDescriptor[propertydescriptors.size()]);
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
		fPropertyMap.remove(id);
		fAttributeMap.remove(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		fAttributeMap.put(id, value);
		firePropertyChange(id.toString(), null, value);
	}
	
	public void setPropertyValue(Object id, Object value, boolean lisen) {
		fAttributeMap.put(id, value);
		//firePropertyChange(id.toString(), null, value);
	}

	private ContentsModel parent;
	public abstract String getName();
	public boolean isVisible(){
		return true;
	}

	public AbstractElementModel(){
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
	
	public void setStyle(IFigure figure){
		
	}
	
	public List getChildren() {
		return children;
	}
	
	public void setChildren(List children) {
		this.children = children; 
	}
	
	public void addChild(AbstractElementModel child){
		children.add(child); 
	}
	
	public void addChild(int index, AbstractElementModel child) {
		children.add(index, child);
	}
	
	public void removeChild(AbstractElementModel child) {
		children.remove(child);
	}
	
	public AbstractElementModel removeChild(int index) {
		return children.remove(index);
	}
	
	public void removeAllChild() {
		children.clear();
	}
	
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
	
	public String toXML(){
		StringBuilder buf= new StringBuilder();
		if(getName() != null){
			buf.append("<");
			buf.append(getName());
			buf.append(getAttributes());
		}
		if(children.size() > 0){
			if(getName() != null){
				buf.append(">");
				buf.append("\n");
			}
			
			for (AbstractElementModel element : children) {
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
