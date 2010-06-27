package gef.example.helloworld.parser.css;

import static org.junit.Assert.*;

import gef.example.helloworld.model.CommandModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CSSTest {
	
	private static String getSource(InputStream in) {
		
		StringBuffer buf = new StringBuffer();
		BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(in));

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				buf.append(line+ "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buf.toString();
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1(){
		CSSParser parser = new CSSParser();
		String src = getSource(CSSTest.class.getResourceAsStream("test.css"));
		try {
			parser.parse(src);
		} catch (CSSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CSS css = parser.getCSS();
		System.out.println(css.toString());
		int s =0;
	}
	
	@Test
	public void test2(){
		CSSParser parser = new CSSParser();
		String src = getSource(CSSTest.class.getResourceAsStream("test2.css"));
		try {
			parser.parse(src);
		} catch (CSSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CSS css = parser.getCSS();
		
//		Declaration declaration = new Declaration();
//		declaration.setName("-list-style-image");
//		
//		FunctionTerm trem = new FunctionTerm();
//		trem.setValue("rect");
//		Expr funcexpr = new Expr();
//		funcexpr.setTerms(new ArrayList<Term>(Arrays.asList(new SymbolTerm("16xp"), new SymbolTerm("16xp"))));
//		trem.setExpr( funcexpr);
//		
//		Expr expr = new Expr();
//		expr.setTerms( new ArrayList<Term>(Arrays.asList(trem)));
//		declaration.setExpr(expr);
		
		Declaration declaration = parser.declaration_stmt("-list-style-image:rect(16xp 16xp);");
		css.addDeclaration("test1", null, null, declaration);
		
		Declaration declaration2 = parser.declaration_stmt("-list-style-image:rect(16xp 16xp 16xp 16xp);");
		css.addDeclaration("test1", null, new ArrayList<String>(Arrays.asList("hover")), declaration2);
		
		Declaration declaration3 = parser.declaration_stmt("-list-style-image3:rect(16xp 16xp);");
		css.addDeclaration("test3", null, null, declaration3);
		
		System.out.println(css.toString());
	}
}
