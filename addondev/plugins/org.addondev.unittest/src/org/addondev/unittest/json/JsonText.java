package org.addondev.unittest.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

public class JsonText {

	static class JsonData {
		private String cmd;
		//private String name;
		private List<Map<String, String>> propertylist;
		
		public String getCmd() {
			return cmd;
		}

		public void setCmd(String cmd) {
			this.cmd = cmd;
		}

		public List<Map<String, String>> getPropertylist() {
			return propertylist;
		}

		public void setPropertylist(
				List<Map<String, String>> propertylist) {
			this.propertylist = propertylist;
		}

		public JsonData(){}
		
		public void SetProperty(Map<String, String> map)
		{		
			if(propertylist == null)
			{
				propertylist = new ArrayList<Map<String,String>>();
			}
			
			propertylist.add(map);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonData test = new JsonData();
		//test.setName("test1");
		LinkedHashMap<String, String> map1 = new LinkedHashMap<String, String>();
		map1.put("1key1", "1val1");
		map1.put("1key2", "1val2");
		
		LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>();
		map2.put("2key1", "2val1");
		map2.put("2key2", "2val2");
		
		ArrayList<Map<String, String>> maplist = new ArrayList<Map<String,String>>();
		maplist.add(map1);
		maplist.add(map2);
		
		test.setPropertylist(maplist);
		
		String text = JSON.encode(test);
		System.out.println(text);
		
		//String jdata = "{\"maplist\":[{\"1key1\":\"1val1\",\"1key2\":\"1val2\"},{\"2key1\":\"2val1\",\"2key2\":\"2val2\"}],\"name\":\"test1\"}";
//		try
//		{		
		String jdata = "{\"";
		JsonData hoge = JSON.decode(jdata, JsonData.class);
//		}catch (JSONException e) {
//			// TODO: handle exception
//			System.out.println(e);
//		}		
		
		System.exit(0);
	}

}
