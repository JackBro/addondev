package gef.example.helloworld.model.parser;

import java.util.ArrayList;
import java.util.List;

import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;

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
	protected void parseChildElement(AbstractElementModel model,
			FuzzyXMLElement e) {
		// TODO Auto-generated method stub
		ListBoxModel listbox = (ListBoxModel)model;
		if(e.getName().equals("listhead")){
			ListHeadModel listhead = new ListHeadModel();
			FuzzyXMLNode[] nodelist = e.getChildren();
			for (int i = 0; i < nodelist.length; i++) {
				FuzzyXMLNode item = nodelist[i];
				if (item instanceof FuzzyXMLElement) {
					XULLoader.parseElement(listhead, (FuzzyXMLElement)item);
				}
			}	
			listbox.setListHead(listhead);				
		}else if(e.getName().equals("listcols")){
			ListColsModel listcols = new ListColsModel();
			FuzzyXMLNode[] nodelist = e.getChildren();
			for (int i = 0; i < nodelist.length; i++) {
				FuzzyXMLNode item = nodelist[i];
				if (item instanceof FuzzyXMLElement) {
					XULLoader.parseElement(listcols, (FuzzyXMLElement)item);
				}
			}	
			listbox.setListcols(listcols);			
		}else if(e.getName().equals("listitem")){
			ListItemModel listitem = new ListItemModel();
			parseAttribute(listitem, (FuzzyXMLElement) e);
			FuzzyXMLNode[] nodelist = e.getChildren();
			for (int i = 0; i < nodelist.length; i++) {
				FuzzyXMLNode listcell = nodelist[i];
				if (listcell instanceof FuzzyXMLElement) {
					XULLoader.parseElement(listitem, (FuzzyXMLElement)listcell);
				}
			}
			listbox.getListitems().add(listitem);
		}else{
			super.parseChildElement(model, e);
		}
	}
	
}
