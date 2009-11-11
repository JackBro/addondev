package jp.addondev.editor.xul;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;

public class XULTagScanner extends RuleBasedScanner {

	public XULTagScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(manager.getColor(IXULColorConstants.STRING)));

		IRule[] rules = new IRule[3];

		// Add rule for double quotes
		rules[0] = new SingleLineRule("\"", "\"", string, '\\');
		// Add a rule for single quotes
		rules[1] = new SingleLineRule("'", "'", string, '\\');
		// Add generic whitespace rule.
		rules[2] = new WhitespaceRule(new XULWhitespaceDetector());

		setRules(rules);
	}
}
