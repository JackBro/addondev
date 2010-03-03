package org.addondev.ui.editor.xml;

import java.util.ArrayList;
import java.util.List;

import org.addondev.preferences.ResourceManager;
import org.addondev.ui.editor.javascript.JavaScriptScanner;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class XMLCommentScanner extends RuleBasedScanner {

//	public XMLCommentScanner(IPreferenceStore fPreferenceStore) {
//		super(fPreferenceStore);
//		// TODO Auto-generated constructor stub
//	}
	private static class JavaWordDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}
		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}
	}
	//public XMLCommentScanner(ColorManager manager) {
	public XMLCommentScanner() {
//		super();
//	
//		IToken string =
//			new Token(
//				new TextAttribute(manager.getColor(IXMLColorConstants.XML_COMMENT)));
//	
//		IRule[] rules = new IRule[1];
//	
//		// Add rule for double quotes
//		//rules[0] = new MultiLineRule("<!--", "-->", string);
//		// Add a rule for single quotes
//		//rules[1] = new SingleLineRule("'", "'", string, '\\');
//		// Add generic whitespace rule.
//		//rules[2] = new WhitespaceRule(new XMLWhitespaceDetector());
//	
//		setRules(rules);
		
		//Color backgroundcolor = ;
		//Color keywordcolor =  ResourceManager.getInstance().getColor(fPreferenceStore, AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD);

		//IToken keywordtoken = new Token(new TextAttribute(keywordcolor, backgroundcolor, SWT.BOLD));

//		Color defaultColor = manager.getColor(IXMLColorConstants.XML_COMMENT);
//		IToken defaulttoken = new Token(new TextAttribute(defaultColor));
//		
//		List<IRule> rules = new ArrayList<IRule>();
//		WordRule wordRule = new WordRule(new JavaWordDetector(), defaulttoken);
//		rules.add(wordRule);
		IToken xmlComment = new Token(XMLPartitionScanner.XML_COMMENT);
		ArrayList<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		//rules.add(new TagRule(tag));
		//rules.add(new MultiLineRule("<![CDATA[", "]]>", xmlCDATA));
		rules.add(new MultiLineRule("<!--", "-->", xmlComment));
		
		setRules(rules.toArray(new IRule[rules.size()]));
		
	}
}
