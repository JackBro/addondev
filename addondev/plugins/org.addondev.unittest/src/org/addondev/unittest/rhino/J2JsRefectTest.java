package org.addondev.unittest.rhino;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class J2JsRefectTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int i, j;

			// クラスの取得
			Class cls = Class.forName("MyObject");

			// フィールドの分析
			Field[] fieldList = cls.getFields();
			for (i = 0; i < fieldList.length; i++) {
				Field fld = fieldList[i];
				// 修飾子を表示
				System.out.print(Modifier.toString(fld.getModifiers()));
				// 型を表示
				System.out.print(" " + fld.getType().getName());
				// フィールド名を表示
				System.out.println(" " + fld.getName() + ";");
			}
			System.out.println("");

			// コンストラクタの分析
			Constructor[] ctorList = cls.getConstructors();
			for (i = 0; i < ctorList.length; i++) {
				Constructor ct = ctorList[i];
				// 修飾子を表示
				System.out.print(Modifier.toString(ct.getModifiers()));
				// クラス名(コンストラクタ名)を表示
				System.out.print(" " + ct.getDeclaringClass().getName());
				// 引数の型を表示
				Class[] cparamList = ct.getParameterTypes();
				System.out.print("(");
				for (j = 0; j < cparamList.length; j++) {
					System.out.print(" " + cparamList[j].getName());
				}
				System.out.println(");");
			}
			System.out.println("");

			// メソッドの分析
			Method[] methList = cls.getMethods();
			for (i = 0; i < methList.length; i++) {
				Method m = methList[i];
				// 修飾子を表示
				System.out.print(Modifier.toString(m.getModifiers()));
				// 戻り値の型を表示
				System.out.print(" " + m.getReturnType().getName());
				// メソッド名を表示
				System.out.print(" " + m.getName());
				// 引数の型を表示
				Class[] mparamList = m.getParameterTypes();
				System.out.print("(");
				for (j = 0; j < mparamList.length; j++) {
					System.out.print(" " + mparamList[j].getName());
				}
				System.out.println(");");
			}
			System.out.println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
