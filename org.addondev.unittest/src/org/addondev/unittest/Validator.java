package org.addondev.unittest;

import jp.aonir.fuzzyxml.FuzzyXMLNode;
import jp.aonir.fuzzyxml.event.FuzzyXMLErrorEvent;
import jp.aonir.fuzzyxml.event.FuzzyXMLErrorListener;


public class Validator implements FuzzyXMLErrorListener {

	@Override
	public void error(FuzzyXMLErrorEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.getMessage());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
