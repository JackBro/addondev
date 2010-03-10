package org.addondev.tools.javadoc;

import java.util.ArrayList;
import java.util.HashMap;

import org.addondev.parser.javascript.EnumNode;
import org.addondev.parser.javascript.serialize.JsElement;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

public class MyDoclet {
	
	private HashMap<String, JsElement> map;// = new HashMap<String, String>();
	private String fPackage;
	
	public HashMap<String, JsElement> getMap() {
		return map;
	}

	public void setMap(HashMap<String, JsElement> map) {
		this.map = map;
	}

	public MyDoclet(String Package) {
		super();
		this.fPackage = Package;
	}

//	public static boolean start(RootDoc root) {
//		MyDoclet doclet = new MyDoclet();
//		doclet.list(root);
//		return true;
//	}

	public void list(RootDoc root) {
		ClassDoc[] classes = root.classes();
		for (int i = 0; i < classes.length; ++i) {
			listClass(classes[i]);
		}
	}

	private void listClass(ClassDoc classDoc) {

		//showDoc(classDoc);
		map.put(fPackage + "." + classDoc.name(), new JsElement(classDoc.name(), EnumNode.OBJECT.name(), classDoc.getRawCommentText())); 
		
		FieldDoc[] fields = classDoc.fields();
		for (int i = 0; i < fields.length; i++) {
			//String nnn = fields[i].getRawCommentText();
			map.put(fPackage + "." + classDoc.name() + "." + fields[i].name(), new JsElement(fields[i].name(), EnumNode.VALUE_PROP.name(), fields[i].getRawCommentText())); 
			//showDoc(fields[i]);
		}

		MethodDoc[] methods = classDoc.methods();
		for (int i = 0; i < methods.length; i++) {
			
			Parameter[] params = methods[i].parameters();
			ArrayList<JsElement> parammlist = new ArrayList<JsElement>();
			for (Parameter parameter : params) {
				//parammlist.add(parameter.name());
				//String mm = parameter.name();
				parammlist.add(new JsElement(parameter.name(), EnumNode.VALUE.name(), null, getReturnType(parameter.typeName())));
				//System.out.println("parameter name = [" + parameter.name() + "]");
			}
			map.put(fPackage + "." + classDoc.name() + "." + methods[i].name(), new JsElement(methods[i].name(), EnumNode.FUNCTION_PROP.name(), methods[i].getRawCommentText(), parammlist)); 
			//showDoc(methods[i]);
		}
	}

	private void showDoc(Doc doc) {
		//String yyy = doc.getRawCommentText();
		System.out.println("[" + doc.name() + "]");
		System.out.println(doc.commentText());
		showTags(doc.tags());
	}

	private void showTags(Tag[] tags) {
		for (int i = 0; i < tags.length; i++) {
			showTag(tags[i]);
		}
	}

	private void showTag(Tag tag) {
		System.out.println(tag);
	}
	
	private String getReturnType(String typename)
	{
		if(typename.equals("java.lang.String")
				|| typename.equals("char"))
		{
			return "String";
		}
		else if(typename.equals("int") 
				|| typename.equals("long")
				|| typename.equals("double")
				|| typename.equals("float")
		)
		{
			return "Number";
		}
		else if(typename.equals("void"))
		{
			return null;
		}
		else if(typename.equals("boolean"))
		{
			return "Boolen";
		}
		else if(typename.startsWith("org.mozilla.interfaces."))
		{
			int index = typename.lastIndexOf(".");
			String n = typename.substring(index+1);
			return n;
		}
		else
		{
			return typename;
		}
	}
}
