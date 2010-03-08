package org.addondev.tools.javadoc;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import org.addondev.tools.jsjava.JsClass;
import org.addondev.tools.jsjava.JsData;
import org.addondev.tools.jsjava.JsElement;

public class RefJava {

	@SuppressWarnings("unchecked")
	public JsData makeXML(String path, Map<String, JsElement> map) {
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
				
				JsClass classdata = new JsClass(fname);
				
				cls = Class.forName("org.mozilla.interfaces." + fname);// "org.mozilla.interfaces.nsIIOService");
				//cls = Class.forName("org.mozilla.interfaces.nsISupportsChar");
				
				Field[] fieldList = cls.getFields();
				for (int i = 0; i < fieldList.length; i++) {
					Field fld = fieldList[i];

					String fldname = fld.getName();
					String returntype = getType(fld.getType());
					if(!fldname.startsWith("org.mozilla.interfaces."))
					{
						fldname = "org.mozilla.interfaces." + fname + "." + fldname;
					}
					JsElement jsdocelm = map.get(fldname);
					if(jsdocelm == null)
					{
						try {
							Class[] classes = cls.getInterfaces();
							for (Class class1 : classes) {
								fldname = fld.getName();
								//if(!fldname.startsWith("org.mozilla.interfaces."))
								//{
									fldname =  class1.getName() + "." + fldname;
								//}
								if(map.containsKey(fldname))
								{
									jsdocelm = map.get(fldname);
									break;
								}
							}
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					jsdocelm.setReturntype(returntype);
					classdata.addElement(jsdocelm);
				}

				// メソッドの分析
				Method[] methList = cls.getMethods();
				for (int i = 0; i < methList.length; i++) {
					Method m = methList[i];

					JsElement jsdocelm = map.get(m.getDeclaringClass().getName() + "." + m.getName());
					if(jsdocelm == null)
					{
						try {
							Class[] classes = cls.getInterfaces();
							for (Class class1 : classes) {
								String mthname = m.getName();
								//if(!fldname.startsWith("org.mozilla.interfaces."))
								//{
								mthname =  class1.getName() + "." + mthname;
								//}
								if(map.containsKey(mthname))
								{
									jsdocelm = map.get(mthname);
									break;
								}
							}
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(jsdocelm != null)
					{
						String returntype = getType(m.getReturnType());
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
	
	private String getType(Class type)
	{
		if(type.getName().equals("java.lang.String"))
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
