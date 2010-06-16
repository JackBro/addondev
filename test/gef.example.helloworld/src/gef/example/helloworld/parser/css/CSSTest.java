package gef.example.helloworld.parser.css;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		
		List<StyleSheet> sl = parser.getStyleSheets();
		for (StyleSheet styleSheet : sl) {
			System.out.println(styleSheet.toCSS());
		}
		int s = sl.size();
	}
}
