package org.addondev.debug.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;
import com.sun.org.apache.xpath.internal.XPathAPI;

@SuppressWarnings("restriction")
public class XMLUtils {
	
	//
	public static String getAddonIDFromRDF(InputStream input) throws ParserConfigurationException, SAXException, IOException, TransformerException
	{
		
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fact.newDocumentBuilder();
		Document doc = builder.parse(input);

        Node n;		        
        String id = null;
        NodeIterator nl = XPathAPI.selectNodeIterator(doc, "/RDF/Description/*");
        while ((n = nl.nextNode()) != null) {
            if(n.getNodeName().equals("em:id"))
            {
            	id = n.getTextContent();
            	break;
            }
        }              
        return id;		
	}
}
