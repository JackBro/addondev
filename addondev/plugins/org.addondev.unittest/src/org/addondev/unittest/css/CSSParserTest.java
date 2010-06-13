package org.addondev.unittest.css;
import java.io.IOException;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList ;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import com.steadystate.css.parser.CSSOMParser;

public class CSSParserTest 
{
	public static void main (String [] args) 
	{
		String URIPath = "file:///D:/data/src/PDE/src/cssparser/stylesheets/preference.css ";
		CSSOMParser cssparser = new CSSOMParser ();
		cssparser.setErrorHandler(new ErrorHandler() {
			
			@Override
			public void warning(CSSParseException arg0) throws CSSException {
				// TODO Auto-generated method stub
				int i=0;
			}
			
			@Override
			public void fatalError(CSSParseException arg0) throws CSSException {
				// TODO Auto-generated method stub
				int i=0;
			}
			
			@Override
			public void error(CSSParseException arg0) throws CSSException {
				// TODO Auto-generated method stub
				int i=0;
			}
		});
		CSSStyleSheet css = null;
		try {
			css = cssparser.parseStyleSheet (new InputSource (URIPath), null, null);
		} catch (IOException e) {
			System.out.println ( "Exception parsing css file:" + e);
		}

		if (css != null) {

			
			CSSRuleList cssrules = css.getCssRules ();
			for (int i = 0; i <cssrules.getLength ();i++) {
				CSSRule rule = cssrules.item (i);
				if (rule instanceof CSSStyleRule) {
					CSSStyleRule cssrule = (CSSStyleRule) rule;
					System.out.println ( "cssrule.getCssText:" + cssrule.getCssText ());
					System.out.println ( "cssrule.getSelectorText:" + cssrule.getSelectorText ());
					
					CSSStyleDeclaration styles = cssrule.getStyle ();
					for (int j = 0, n = styles.getLength (); j<n;j++) {
						System.out.println ("PropertyValue = " + styles.item (j)+":"+ styles.getPropertyValue (styles.item (j)));
					}
				} else if (rule instanceof CSSImportRule) {
					CSSImportRule cssrule = (CSSImportRule) rule;
					System.out.println ("cssrule.getHref () = "+ cssrule.getHref ());
				}
			}
		}
	}
} 