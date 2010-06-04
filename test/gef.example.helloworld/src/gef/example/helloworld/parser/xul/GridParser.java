package gef.example.helloworld.parser.xul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.aonir.fuzzyxml.FuzzyXMLElement;
import jp.aonir.fuzzyxml.FuzzyXMLNode;


import gef.example.helloworld.model.AbstractElementModel;
import gef.example.helloworld.model.ColumnModel;
import gef.example.helloworld.model.ColumnsModel;
import gef.example.helloworld.model.GridModel;
import gef.example.helloworld.model.RowModel;

public class GridParser extends AbstractXULParser {

	@Override
	protected AbstractElementModel createModel() {
		// TODO Auto-generated method stub
		return new GridModel();
	}

	@Override
	protected void parseChildElement(AbstractElementModel model, FuzzyXMLElement e) {
		// TODO Auto-generated method stub
		// super.parseChildElement(model, e);
		if ("columns".equals(e.getName())) {

			List list = new ArrayList();

			FuzzyXMLNode[] columnnodelist = e.getChildren();
			for (int i = 0; i < columnnodelist.length; i++) {
				FuzzyXMLNode node = columnnodelist[i];
				if (node instanceof FuzzyXMLElement) {
					list.add(new ColumnModel());
				}
			}
			((GridModel) model).getColumns().setChildren(list);

		} else if ("rows".equals(e.getName())) {
			List rows = new ArrayList();
			FuzzyXMLNode[] rownodelist = e.getChildren();

			for (int i = 0; i < rownodelist.length; i++) {
				FuzzyXMLNode node = rownodelist[i];
				if (node instanceof FuzzyXMLElement) {
					RowModel row = new RowModel();
					rows.add(row);
				}
			}
			((GridModel) model).getRows().setChildren(rows);

			for (int i = 0; i < rownodelist.length; i++) {
				FuzzyXMLNode node = rownodelist[i];
				if (node instanceof FuzzyXMLElement) {
					FuzzyXMLNode[] nodelist = ((FuzzyXMLElement)node).getChildren();
					for (int j = 0; j < nodelist.length; j++) {
						FuzzyXMLNode rowchildnode = nodelist[j];
						if (rowchildnode instanceof FuzzyXMLElement) {
							XULLoader.parseElement(model,
									(FuzzyXMLElement) rowchildnode);
						}
					}
				}
			}

		}
	}

}
