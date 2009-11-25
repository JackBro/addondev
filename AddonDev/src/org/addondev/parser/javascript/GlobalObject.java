package org.addondev.parser.javascript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GlobalObject {
	private static GlobalObject instance;
	private ArrayList<JsNode> nodeList;
	
	private GlobalObject(){
		nodeList = new ArrayList<JsNode>();
	}
	
	public static GlobalObject getInstance()
	{
		if(instance == null)
			instance = new GlobalObject();
		
		return instance;
	}

	private void load()
	{
		String src = getSource(GlobalObject.class.getResourceAsStream("system.js"));
		Lexer lex = null;
		lex = new Lexer(src);
		Parser parser = new Parser(); // パーサーを作成。
		parser.parse(lex);		
		JsNode node = parser.root;
		nodeList.add(node);
	}
	
	private String getSource(InputStream in) {
		StringBuffer buf = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		String line;
		//String res = "";
		try {
			while ((line = reader.readLine()) != null) {
				buf.append(line+ "\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buf.toString();
	}
}
