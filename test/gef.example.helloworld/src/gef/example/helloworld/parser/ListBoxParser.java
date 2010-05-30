package gef.example.helloworld.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.ListBoxModel;
import gef.example.helloworld.model.ListColModel;
import gef.example.helloworld.model.ListColsModel;
import gef.example.helloworld.model.ListHeadModel;
import gef.example.helloworld.model.ListItemModel;

public class ListBoxParser extends AbstractXULParser {

	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new ListBoxModel();
	}

	@Override
	protected void parseChildren(AbstractElementModel model, Element e) {
		ListBoxModel listbox = (ListBoxModel)model;
		// TODO Auto-generated method stub
		//super.parseChildren(model, e);
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				//parseChildElement(model, (Element)node);
				if(node.getNodeName().equals("listhead")){
					ListHeadModel listhead = new ListHeadModel();
					NodeList nodelist = node.getChildNodes();
					for (int n = 0; n < nodelist.getLength(); n++) {
						Node item = nodelist.item(n);
						if (item instanceof Element) {
							XULLoader.parseElement(listhead, (Element)item);
						}
					}	
					listbox.setListHead(listhead);					
				}
				else if(node.getNodeName().equals("listcols")){
					ListColsModel listcols = new ListColsModel();
					NodeList nodelist = node.getChildNodes();
					for (int n = 0; n < nodelist.getLength(); n++) {
						Node item = nodelist.item(n);
						if (item instanceof Element) {
							XULLoader.parseElement(listcols, (Element)item);
						}
					}	
					listbox.setListcols(listcols);
				}else if(node.getNodeName().equals("listitem")){
					ListItemModel listitem = new ListItemModel();
					parseAttribute(listitem, (Element) node);
					NodeList nodelist = node.getChildNodes();
					for (int n = 0; n < nodelist.getLength(); n++) {
						Node listcell = nodelist.item(n);
						if (listcell instanceof Element) {
							XULLoader.parseElement(listitem, (Element)listcell);
						}
					}
					listbox.getListitems().add(listitem);
				}
			}
		}
	}

	@Override
	protected void parseChildElement(AbstractElementModel model, Element e) {
		// TODO Auto-generated method stub
		super.parseChildElement(model, e);
		
	}

}
