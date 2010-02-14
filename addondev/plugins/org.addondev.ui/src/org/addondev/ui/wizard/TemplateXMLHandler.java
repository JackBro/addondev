package org.addondev.ui.wizard;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TemplateXMLHandler extends DefaultHandler{
	static SAXParserFactory parserFactory = SAXParserFactory.newInstance();
	
	static SAXParser getSAXParser() throws ParserConfigurationException, SAXException {
		SAXParser parser = null;
		 synchronized(parserFactory) {
			 parser = parserFactory.newSAXParser();
		 }
		 return parser;
	}	
	
	public void parse()
	{
		
	}
	
}
