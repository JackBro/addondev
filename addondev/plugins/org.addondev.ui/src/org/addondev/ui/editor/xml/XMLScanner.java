package org.addondev.ui.editor.xml;

import java.util.ArrayList;
import java.util.List;

import org.addondev.ui.ColorManager;
import org.addondev.ui.IColorManager;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;
import org.eclipse.swt.graphics.Color;

public class XMLScanner extends RuleBasedScanner {

	private Token fProcInstrToken;
	
	public Token getProcInstrToken() {
		return fProcInstrToken;
	}

	public XMLScanner(IPreferenceStore store) {
		IColorManager colorManager = ColorManager.getInstance();//.setPreferenceStore(fStore);
		colorManager.setPreferenceStore(store);
		Color procinstrcolor = colorManager.getColor(AddonDevUIPrefConst.COLOR_XML_COMMENT);
		
		fProcInstrToken = new Token(new TextAttribute(procinstrcolor));
//		IToken procInstr =
//			new Token(
//				new TextAttribute(
//					manager.getColor(IXMLColorConstants.PROC_INSTR)));

		ArrayList<IRule> rules = new ArrayList<IRule>();
		//rules.add(new SingleLineRule("<?", "?>", procInstr));
		rules.add(new SingleLineRule("<?", "?>", fProcInstrToken));
		rules.add(new WhitespaceRule(new XMLWhitespaceDetector()));
		setRules(rules.toArray(new IRule[rules.size()]));
	}
}
