package org.addondev.ui.editor.xml;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.*;

public class XMLPartitionScanner extends RuleBasedPartitionScanner {
	
	public final static String XML_CDATA = "__xml_cdata";
	public final static String XML_COMMENT = "__xml_comment";
	public final static String XML_TAG = "__xml_tag";
	
	public XMLPartitionScanner() {

		IToken xmlComment = new Token(XML_COMMENT);
		IToken tag = new Token(XML_TAG);

		ArrayList<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		rules.add(new MultiLineRule("<![CDATA[", "]]>", xmlComment));
		rules.add(new MultiLineRule("<!--", "-->", xmlComment));
		rules.add(new TagRule(tag));

		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
	}
}
