package org.addondev.ui.editor.xml;

import org.addondev.ui.ColorManager;
import org.addondev.ui.IColorManager;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.Color;

public class XMLTagScanner extends RuleBasedScanner {

	private Token fStringToken;
	
	
	public Token getStringToken() {
		return fStringToken;
	}

	public XMLTagScanner(IPreferenceStore store) {
		
		IColorManager colorManager = ColorManager.getInstance();
		colorManager.setPreferenceStore(store);
		Color strcolor = colorManager.getColor(AddonDevUIPrefConst.COLOR_XML_STRING);		
		
		fStringToken = new Token(new TextAttribute(strcolor));
		
//		IToken string =
//			new Token(
//				new TextAttribute(manager.getColor(IXMLColorConstants.STRING)));

		IRule[] rules = new IRule[3];

		//rules[0] = new SingleLineRule("\"", "\"", string, '\\');
		//rules[1] = new SingleLineRule("'", "'", string, '\\');
		rules[0] = new SingleLineRule("\"", "\"", fStringToken, '\\');
		rules[1] = new SingleLineRule("'", "'", fStringToken, '\\');
		rules[2] = new WhitespaceRule(new XMLWhitespaceDetector());

		setRules(rules);
	}
}
