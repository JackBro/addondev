package org.addondev.unittest.css;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.Rule;
import org.apache.batik.css.engine.StyleDeclaration;
import org.apache.batik.css.engine.StyleRule;
import org.apache.batik.css.engine.StyleSheet;
import org.apache.batik.css.parser.CSSSelectorList;
import org.apache.batik.css.parser.DefaultConditionalSelector;
import org.apache.batik.css.parser.DefaultDescendantSelector;
import org.apache.batik.css.parser.Parser;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

public class CSSParserTest2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Parser p = null;
        FileReader cssReader = null;
        MyCssDocumentHandler cssDocHandle = new MyCssDocumentHandler();
       
        try {
        	String URIPath = "D:/data/src/PDE/src/cssparser/stylesheets/preference.css";
            p = new Parser();
            cssReader = new FileReader(URIPath);
           
            p.setDocumentHandler(cssDocHandle);
            p.parseStyleSheet(new InputSource(cssReader));
            
            StyleSheet styleSheet = cssDocHandle.getStyleSheet();
            //styleSheet.
            for (int i = 0; i < styleSheet.getSize(); i++) {
            	StyleRule rule = (StyleRule) styleSheet.getRule(i);
            	
            	StyleDeclaration styleDeclaration = rule.getStyleDeclaration();
            	
            	for (int j = 0; j < rule.getSelectorList().getLength(); j++) {
            		Selector sele = rule.getSelectorList().item(j);
            		
            		String item = rule.getSelectorList().item(j).toString();
            		System.out.println ( "rule.getSelectorList().item:" + item);
            		
				}
			}
            
            CSSSelectorList list = new CSSSelectorList();

            //DefaultDescendantSelector sel = new 
           // list.append(item)
            StyleDeclaration styleDeclaration = new StyleDeclaration();

            StyleRule mystyleRule = new StyleRule();
            //mystyleRule.setStyleDeclaration(sd)
//
//            
//            FileOutputStream fo = new FileOutputStream("D:/data/src/PDE/src/cssparser/stylesheets/preference2.css");
//            ObjectOutput oo = new ObjectOutputStream(fo);
//            oo.writeObject(stylesheet);
//            oo.flush();
        } catch (CSSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
