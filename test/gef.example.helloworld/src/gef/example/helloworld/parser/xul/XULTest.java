package gef.example.helloworld.parser.xul;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;


public class XULTest {
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

	@Test
	public void test1(){
		String src =  getSource(XULTest.class.getResourceAsStream("test.xul"));
		XULParser parser = new XULParser();
		parser.parse(src);
		int i=0;
		i++;
	}
}
