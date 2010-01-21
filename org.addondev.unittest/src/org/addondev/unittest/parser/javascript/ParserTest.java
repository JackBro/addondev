package org.addondev.unittest.parser.javascript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.addondev.parser.javascript.JsNode;
import org.addondev.parser.javascript.JsNodeHelper;
import org.addondev.parser.javascript.Lexer;
import org.addondev.parser.javascript.NodeManager;
import org.addondev.parser.javascript.Parser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ParserTest {

	private static String src;
	
	private static String getSource(InputStream in) throws IOException {
		StringBuffer buf = new StringBuffer();
		BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(in));

		String line;
		//String res = "";
		while ((line = reader.readLine()) != null) {
			//doSomething(line);
			buf.append(line+ "\r\n");
			//res += line + "\n";
		}
		
		//return res;
		return buf.toString();
	}
	
	@BeforeClass
	public static void be() throws IOException
	{
		//src = getSource(ParserTest.class.getResourceAsStream("test01.js"));
	}
	
	@Test
	public void testParser00() {

		Lexer lex = null;
		try {
			lex = new Lexer(getSource(ParserTest.class.getResourceAsStream("test00.js")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // スキャナを作成。
		Parser parser = new Parser(); // パーサーを作成。
		parser.parse(lex);
		JsNode node = parser.root;
		node.dump("");
		
		int offset = 183;
		//String t = getAssistTarget(src, offset);
		
		//JsNode node = JsNodeHelper.findChildNode(parser.root, t);
		JsNode node2 = parser.root.getNodeFromOffset(offset);
		JsNode offsetnode = JsNodeHelper.findChildNode(node2, "f");
		
		System.out.println(node2.getId() + " : " + node2.getImage());
		
		node2.dump("");
	}
	
	@Test
	public void testParser01() throws IOException {
		
		Lexer lex = null;
		String src = getSource(ParserTest.class.getResourceAsStream("test00.js"));
		lex = new Lexer(src);
		Parser parser = new Parser(); // パーサーを作成。
		parser.parse(lex);
		JsNode node = parser.root;
		node.dump("");
	}
	
	@Test
	public void testParser02() throws IOException {
		
		Lexer lex = null;
		String src = getSource(ParserTest.class.getResourceAsStream("test02.js"));
		lex = new Lexer(src);
		Parser parser = new Parser(); // パーサーを作成。
		parser.parse(lex);
		JsNode node = parser.root;
		node.dump("");
	}
	
	@Test
	public void testParser03() throws IOException {
		
		{
		String src = getSource(ParserTest.class.getResourceAsStream("system.js"));
		Lexer lex = new Lexer(src);
		Parser parser = new Parser(); // パーサーを作成。
		parser.parse(lex);
		JsNode node = parser.root;
		NodeManager.getInstance().SetNode("system", node);
		node.dump("");
		}
		{
		Lexer lex = null;
		String src = getSource(ParserTest.class.getResourceAsStream("test03.js"));
		lex = new Lexer(src);
		Parser parser = new Parser(); // パーサーを作成。
		parser.parse(lex);
		JsNode node = parser.root;
		node.dump("");
		}
	}

	@Test
	public void testParser04() throws IOException {
		
		Lexer lex = null;
		String src = getSource(ParserTest.class.getResourceAsStream("test04.js"));
		lex = new Lexer(src);
		Parser parser = new Parser(); // パーサーを作成。
		parser.parse(lex);
		JsNode node = parser.root;
		node.dump("");
	}
	
	@Test
	public void testJSDOC() throws IOException {
		String text = "/**\n"
					+ "* function createInstance() \n"
					+ "* @type    interface \n"
					+ "* @memberOf   Object \n"
					+ "* @returns {interface} \n"
					+ "*/";
		 Pattern p = Pattern.compile("@returns\\s+\\{\\s*(.*)\\s*\\}");
		 Matcher m = p.matcher(text);
		 if (m.find()) {
		     String key = m.group(1); // key = "Name";
		     //String value = m.group(2); // value = "Regular Expressions";
		     System.out.println("@returns = " + key);
		     //System.out.println("value = ");
			 
		 }
		 
		 Pattern p2 = Pattern.compile("@type\\s+(\\w+)");
		 Matcher m2 = p2.matcher(text);
		 if (m2.find()) {
		     String key = m2.group(1); // key = "Name";
		     System.out.println("@type  = " + key);			 
		 }
	}	
	
	@Test
	public void testTarceCode() throws IOException {

		//var m;aa.charAt("test \" code", test()).
		String src = getSource(ParserTest.class.getResourceAsStream("testtrace.js"));
		int offset = src.length();
		offset = 14;
		StringBuffer buf = new StringBuffer();
		for (int i = offset-1; i >=0; i--) {
			char c = src.charAt(i);
			while((c == '\n') || (c == '\r'))
			{
				i--;
				c = src.charAt(i);
			}
			if(c == ')')
			{
				int n = skipr(src, i);
				i=n-1;
			}
			c = src.charAt(i);
			if (!Character.isJavaIdentifierPart((char) c) && c !='.') {	
				break;
			}
			buf.append(c);
			
		}
		System.out.println("buf = " + buf.reverse());
	}
	private int skipr(String src, int offset)
	{
		Stack<String> rl = new Stack<String>();
		char c = src.charAt(offset);

		while( offset >0 )
		{
			if(c == '(')
			{
				rl.pop();
				if(rl.isEmpty())
					return offset;
			}
			else if(c == ')')
			{
				rl.push(")");
			}
			offset--;
			offset = skipsreing(src, offset);
			c = src.charAt(offset);
		}		
		
		return offset;
	}
	
	private int skipsreing(String src, int offset)
	{
		char c = src.charAt(offset);
		if(c == '"')
		{
			offset--;
		}
		else return offset;
		
		c = src.charAt(offset);

		while( offset >0 )
		{
			if(c == '"' && src.charAt(offset-1) != '\\')
			{
				return offset-1;
			}
			offset--;
			c = src.charAt(offset);	
		}			
		return offset;
	}

	@Test
	public void testNode() throws IOException {

		String src = getSource(ParserTest.class.getResourceAsStream("system.js"));
		Lexer lex = new Lexer(src);
		Parser parser = new Parser(); // パーサーを作成。
		parser.parse(lex);
		JsNode node = parser.root;
		NodeManager.getInstance().SetNode("system", node);
		node.dump("");
		
		src = getSource(ParserTest.class.getResourceAsStream("test00.js"));
		lex = new Lexer(src);
		parser = new Parser(); // パーサーを作成。
		parser.parse(lex);
		NodeManager.getInstance().SetNode("test00", parser.root);
		parser.root.dump("");
	}
	

}
