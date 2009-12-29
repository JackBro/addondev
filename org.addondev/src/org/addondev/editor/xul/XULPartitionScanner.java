package org.addondev.editor.xul;

import org.eclipse.jface.text.rules.*;

public class XULPartitionScanner extends RuleBasedPartitionScanner {
	public final static String XML_COMMENT = "__xml_comment";
	public final static String XML_TAG = "__xml_tag";

	public XULPartitionScanner() {

		IToken xmlComment = new Token(XML_COMMENT);
		IToken tag = new Token(XML_TAG);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("<!--", "-->", xmlComment);
		rules[1] = new TagRule(tag);

		setPredicateRules(rules);
	}
}
