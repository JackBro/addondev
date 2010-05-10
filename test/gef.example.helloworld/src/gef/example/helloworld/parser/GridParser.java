package gef.example.helloworld.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gef.example.helloworld.model.ElementModel;
import gef.example.helloworld.model.GridModel;

public class GridParser extends AbstractXULParser {
	
	@Override
	protected ElementModel createModel() {
		// TODO Auto-generated method stub
		return new GridModel();
	}

	@Override
	protected void parseChildren(ElementModel model, Element e) {
		// TODO Auto-generated method stub
		//super.parseChildren(model, e);
		parseChildElement(model, e);
	}

	@Override
	protected void parseChildElement(ElementModel model, Element e) {
		// TODO Auto-generated method stub
		//super.parseChildElement(model, e);
		NodeList nodeList = e.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if("columns".equals(childNode.getNodeName())){
				
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();
				
				NodeList columnnodelist = childNode.getChildNodes();
				for (int j = 0; j < columnnodelist.getLength(); j++) {
					Node node = columnnodelist.item(j);
					//Map<String, String> map = new HashMap<String, String>();
					//map.put("flex", "0");
					if (node instanceof Element) {
						Map<String, String> map = getAttribute((Element)node);
						if(map.isEmpty()){
							Map<String, String> defaultmap = new HashMap<String, String>();
							defaultmap.put("flex", "");
							list.add(defaultmap);
						}else{
							list.add(map);
						}
					}
				}
				model.setPropertyValue(GridModel.ATTR_COLUMS, list);
				
			}else if("rows".equals(childNode.getNodeName())){
				NodeList rownodelist = childNode.getChildNodes();
				for (int j = 0; j < rownodelist.getLength(); j++) {
					Node node = rownodelist.item(j);
					if (node instanceof Element) {
						NodeList nodelist = node.getChildNodes();
						for (int n = 0; n < nodelist.getLength(); n++) {
							Node rowchildnode = nodelist.item(n);
							if (rowchildnode instanceof Element) {
								XULLoader.parseElement(model, (Element)rowchildnode);
							}
						}
					}
				}
			}
		}
	}

}
