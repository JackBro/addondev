package org.addondev.ui.editor.xml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class XMLScanner extends RuleBasedScanner {

	public XMLScanner(ColorManager manager) {
		IToken procInstr =
			new Token(
				new TextAttribute(
					manager.getColor(IXMLColorConstants.PROC_INSTR)));

		ArrayList<IRule> rules = new ArrayList<IRule>();
		rules.add(new SingleLineRule("<?", "?>", procInstr));
		rules.add(new WhitespaceRule(new XMLWhitespaceDetector()));
		setRules(rules.toArray(new IRule[rules.size()]));
	}
}
