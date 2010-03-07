package org.addondev.tools.javadoc;

import java.util.HashMap;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

public class MyDoclet {
	
	private HashMap<String, String> map;// = new HashMap<String, String>();
	private String fPackage;
	
	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
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

		showDoc(classDoc);
		map.put(fPackage + classDoc.name(), classDoc.getRawCommentText()); 
		
		FieldDoc[] fields = classDoc.fields();
		for (int i = 0; i < fields.length; i++) {
			map.put(fPackage + fields[i].name(), fields[i].getRawCommentText()); 
			showDoc(fields[i]);
		}

		MethodDoc[] methods = classDoc.methods();
		for (int i = 0; i < methods.length; i++) {
			map.put(fPackage + methods[i].name(), methods[i].getRawCommentText()); 
			showDoc(methods[i]);
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
}
