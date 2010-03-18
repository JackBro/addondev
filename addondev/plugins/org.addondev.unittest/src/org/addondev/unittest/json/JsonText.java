package org.addondev.unittest.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

public class JsonText {

	static class Test
	{
		private String name;
		//private LinkedHashMap<String, String> map;
		private List<Map<String, String>> maplist;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Map<String, String>> getMaplist() {
			return maplist;
		}

		public void setMaplist(List<Map<String, String>> maplist) {
			this.maplist = maplist;
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
		LinkedHashMap<String, String> map1 = new LinkedHashMap<String, String>();
		map1.put("1key1", "1val1");
		map1.put("1key2", "1val2");
		
		LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>();
		map2.put("2key1", "2val1");
		map2.put("2key2", "2val2");
		
		ArrayList<Map<String, String>> maplist = new ArrayList<Map<String,String>>();
		maplist.add(map1);
		maplist.add(map2);
		
		test.setMaplist(maplist);
		
		String text = JSON.encode(test);
		System.out.println(text);
		
		//String jdata = "{\"maplist\":[{\"1key1\":\"1val1\",\"1key2\":\"1val2\"},{\"2key1\":\"2val1\",\"2key2\":\"2val2\"}],\"name\":\"test1\"}";
//		try
//		{		
		String jdata = "{\"";
		Test hoge = JSON.decode(jdata, Test.class);
//		}catch (JSONException e) {
//			// TODO: handle exception
//			System.out.println(e);
//		}		
		
		System.exit(0);
	}

}
