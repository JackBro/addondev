package org.addondev.tools.javadoc;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.addondev.parser.javascript.serialize.JsClass;
import org.addondev.parser.javascript.serialize.JsData;
import org.addondev.parser.javascript.serialize.JsElement;

import sun.tools.tree.NullExpression;

public class ReflectJava {

	@SuppressWarnings("unchecked")
	public JsData makeXML(String path, Map<String, JsElement> map, HashMap<String, HashMap<String, Java2JS>> jjsmap) {
		// TODO Auto-generated method stub
		ArrayList<JsClass> classlist = new ArrayList<JsClass>();
		JsData data = new JsData();
		Class cls;
		try {
			File dir = new File(path);
			File[] files = dir.listFiles();
			for (File file : files) {
				String fname = file.getName();
				fname = fname.substring(0, fname.lastIndexOf("."));
				
				HashMap<String, Java2JS> propmap = jjsmap.get(fname);
				
				JsClass classdata = new JsClass(fname);
				
				cls = Class.forName("org.mozilla.interfaces." + fname);// "org.mozilla.interfaces.nsIIOService");
				
				//Fields
				Field[] fieldList = cls.getFields();
				for (int i = 0; i < fieldList.length; i++) {
					Field fld = fieldList[i];

					String fldname = fld.getName();
					
					String returntype = getReturnType(fld.getType());
					if(!fldname.startsWith("org.mozilla.interfaces."))
					{
						fldname = "org.mozilla.interfaces." + fname + "." + fldname;
					}
					JsElement jsdocelm = map.get(fldname);
					if(jsdocelm == null)
					{
						try {
							Class[] classes = cls.getInterfaces();
							jsdocelm = getElement(classes, map, fld.getName());
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if(jsdocelm != null)
					{
						if(propmap.containsKey(fname))
						{
							String jspropname = propmap.get(fname).getJsname();
							String jstype =  propmap.get(fname).getType();
							jsdocelm.setName(jspropname);
							jsdocelm.setNodeType(jstype);
						}		
						else
						{
							throw new IllegalArgumentException("Fields : not find key : " + fname);
						}
						
						jsdocelm.setReturntype(returntype);
						classdata.addElement(jsdocelm);
					}
				}

				// Methods
				Method[] methList = cls.getMethods();
				for (int i = 0; i < methList.length; i++) {
					Method m = methList[i];

					JsElement jsdocelm = map.get(m.getDeclaringClass().getName() + "." + m.getName());
					if(jsdocelm == null)
					{
						try {
							Class[] classes = cls.getInterfaces();
							jsdocelm = getElement(classes, map, m.getName());
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(jsdocelm != null)
					{
						if(propmap.containsKey(fname))
						{
							String jspropname = propmap.get(fname).getJsname();
							String jstype =  propmap.get(fname).getType();
							jsdocelm.setName(jspropname);
							jsdocelm.setNodeType(jstype);
						}	
						else
						{
							throw new IllegalArgumentException("Methods : not find key : " + fname);
						}
						
						String returntype = getReturnType(m.getReturnType());
						jsdocelm.setReturntype(returntype);
						classdata.addElement(jsdocelm);
					}
				}
				classlist.add(classdata);
			}
			
			data.setClasses(classlist);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	private JsElement getElement(Class[] classes, Map<String, JsElement> map, String name)
	{
		for (Class class1 : classes) {
			String pname =  class1.getName() + "." + name;
			//}
			if(map.containsKey(pname))
			{
				JsElement jsdocelm = map.get(pname);
				return jsdocelm;
			}
			return getElement(class1.getInterfaces(), map, name);
		}
		
		return null;
	}
	
	private String getReturnType(Class type)
	{
		if(type.getName().equals("java.lang.String")
				|| type.getName().equals("char"))
		{
			return "String";
		}
		else if(type.getName().equals("int") 
				|| type.getName().equals("long")
				|| type.getName().equals("double")
				|| type.getName().equals("float")
		)
		{
			return "Number";
		}
		else if(type.getName().equals("void"))
		{
			return null;
		}
		else if(type.getName().equals("boolean"))
		{
			return "Boolen";
		}
		else if(type.getName().startsWith("org.mozilla.interfaces."))
		{
			int index = type.getName().lastIndexOf(".");
			String n = type.getName().substring(index+1);
			return n;
		}
		else
		{
			return type.getName();
		}
	}

}
