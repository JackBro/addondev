package org.addondev.unittest.json;

import java.util.LinkedHashMap;

import net.arnx.jsonic.JSON;

public class JsonText {

	static class Test
	{
		private String name;
		private LinkedHashMap<String, String> map;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public LinkedHashMap<String, String> getMap() {
			return map;
		}

		public void setMap(LinkedHashMap<String, String> map) {
			this.map = map;
		}

		public Test()
		{
			
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test test = new Test();
		test.setName("test1");
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("key1", "val1");
		map.put("key2", "val2");
		test.setMap(map);
		
		String text = JSON.encode(test);
		System.out.println(text);
		
		String jdata = "{\"map\":{\"jkey1\":\"jval1\",\"jkey2\":\"jval2\"},\"name\":\"jtest\"}";
		Test hoge = JSON.decode(jdata, Test.class);
		
		
		System.exit(0);
	}

}
