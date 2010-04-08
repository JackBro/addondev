package org.addondev.unittest.parser.javascript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.addondev.core.AddonDevPlugin;
import org.addondev.parser.javascript.JsNode;
import org.addondev.parser.javascript.Lexer;
import org.addondev.parser.javascript.Parser;
import org.addondev.parser.javascript.Scope;
import org.addondev.parser.javascript.ScopeManager;
import org.addondev.parser.javascript.ScopeStack;
import org.addondev.parser.javascript.serialize.NodeSerializer;
import org.addondev.parser.javascript.util.JavaScriptParserManager;
import org.addondev.util.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
	public void testParser00() throws IOException {

		String src = getSource(ParserTest.class.getResourceAsStream("test00.js"));
		
		//Parser parser = new Parser("test00.js");
		ScopeManager sm = new ScopeManager();
		Parser parser = new Parser("test00.js", sm);
		parser.parse(src);
		JsNode node = parser.root;
		node.dump("");
	}
	
	@Test
	public void testParser01() throws IOException {
		
		String src = getSource(ParserTest.class.getResourceAsStream("test01.js"));
		
		//Parser parser = new Parser("test01.js");
		ScopeManager sm = new ScopeManager();
		Parser parser = new Parser("test01.js", sm);
		parser.parse(src);
		JsNode node = parser.root;
		node.dump("");
	}
	
	@Test
	public void testParser02() throws IOException {
		
		Lexer lex = null;
		String src = getSource(ParserTest.class.getResourceAsStream("test02.js"));
		lex = new Lexer(src);
		//Parser parser = new Parser("test02.js");
		ScopeManager sm = new ScopeManager();
		Parser parser = new Parser("test02.js", sm);
		parser.parse(lex);
		JsNode node = parser.root;
		node.dump("");
	}
	
	@Test
	public void testParser03() throws IOException {
		
		ArrayList<String> jslist = new ArrayList<String>();
		ScopeManager sm = new ScopeManager();

//		{
//			jslist.add("system.js");
//			String src = getSource(ParserTest.class.getResourceAsStream("system.js"));					
//			Parser parser = new Parser("system.js", sm);
//			parser.parse(src);
//			//JsNode node = parser.root;
//			//node.dump("");
//		}
//		{
//			String dataxml = "D:/data/src/PDE/workrepository/plugins/addondev/plugins/org.addondev.unittest/tmp/text.xml";
//			JsNode root = NodeSerializer.read(dataxml);
//			String name = "xpcomxml";
//			ScopeStack fScopeStack = new ScopeStack();
//			fScopeStack.pushScope(new Scope(0, 1, root));
//			ScopeManager.instance().setScopeStack(name, fScopeStack);
//		}
		
		
		
//		{
////			String src = getSource(ParserTest.class.getResourceAsStream("xpcom.js"));		
////			Parser parser = new Parser("xpcom.js");
////			parser.parse(src);
//			
//			String src = getSource(ParserTest.class.getResourceAsStream("xpcom.js"));
//			
//			Parser parser = new Parser("xpcom.js", sm);
//			parser.parse(src);
//			//JsNode node = parser.root;
//			//node.dump("");
//		}

//		{
//			jslist.add("firefox.js");
//			String src = getSource(ParserTest.class.getResourceAsStream("firefox.js"));					
//			Parser parser = new Parser("firefox.js", sm);
//			parser.parse(src);
//			JsNode node = parser.root;
//			node.dump("");
//		}
		{

			//jslist.add("xpcom.js");
			sm.setJsLis(jslist);
			String src = getSource(ParserTest.class.getResourceAsStream("test03.js"));
			//ScopeManager sm = new ScopeManager();
			Parser parser = new Parser("test03.js", sm);
			parser.parse(src);
			JsNode node = parser.root;
			node.dump("");
		}
	}

	@Test
	public void testParser04() throws IOException {
		
		Lexer lex = null;
		String src = getSource(ParserTest.class.getResourceAsStream("test04.js"));
		lex = new Lexer(src);
		//Parser parser = new Parser("test04.js"); // パーサーを作成。
		ScopeManager sm = new ScopeManager();
		Parser parser = new Parser("test04.js", sm);
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
	public void testNode() throws IOException, CoreException {
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] pps = root.getProjects();
		IProject pro = root.getProject("stacklink");
		boolean ppd = pro.exists();
		IFile ppfile = pro.getFile("chrome.manifest");
		boolean ppdd =  ppfile.exists();
		
		IPath pp = AddonDevPlugin.getWorkspace().getRoot().getLocation();
		IProject[] s = AddonDevPlugin.getWorkspace().getRoot().getProjects();
		IProject project = AddonDevPlugin.getWorkspace().getRoot().getProject("stacklink");
		boolean pd = project.exists();
		IFile file = project.getFile("stacklink/chrome/content/test2.js");
		IFile file2 = project.getFile("chrome.manifest");
		boolean dd =  file2.exists();
		String text = FileUtil.getContent(file2.getContents());
		
		JavaScriptParserManager.instance().parse(project, file, text);
		String path = file.getFullPath().toPortableString();
		ScopeManager scopemanager = JavaScriptParserManager.instance().getScopeManager(project);
		Scope scope = scopemanager.getScope(path, 0);
		JsNode tnode = scope.getNode();
	}
	

}
