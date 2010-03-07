package org.addondev.tools.javadoc;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RefJava {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Class cls;
		try {
			String path = "";
			File dir = new File(path);
		    File[] files = dir.listFiles();
			for (File file : files) {

				cls = Class.forName("org.mozilla.interfaces." + file.getName());//"org.mozilla.interfaces.nsIIOService");
		      
				 Field[] fieldList = cls.getFields();
			      for (int i=0;i<fieldList.length;i++) {
			        Field fld = fieldList[i];
			        // 修飾子を表示
			        System.out.print(Modifier.toString(fld.getModifiers()));
			        // 型を表示
			        System.out.print(" " + fld.getType().getName());
			        // フィールド名を表示
			        System.out.println(" " + fld.getName() + ";");
			      }
				
				// メソッドの分析
		      Method[] methList = cls.getMethods();
		      for (int i=0;i<methList.length;i++) {
		        Method m = methList[i];
		        // 修飾子を表示
		        System.out.print(Modifier.toString(m.getModifiers()));
		        // 戻り値の型を表示
		        System.out.print(" " + m.getReturnType().getName());
		        
		        String pname = m.getDeclaringClass().getName();
		        // メソッド名を表示
		        System.out.print(" pname = " + pname);
		        
		        // メソッド名を表示
		        System.out.print(" " + m.getName());
		        // 引数の型を表示
		        Class[] mparamList = m.getParameterTypes();
		        System.out.print("(");
		        for (int j=0;j<mparamList.length;j++) {
		          System.out.print(" " + mparamList[j].getName());
		        }
		        System.out.println(");");
		      }	
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      System.exit(0);
	}

}
