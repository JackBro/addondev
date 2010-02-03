package org.addondev.ui.editor.javascript;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class JavaScriptPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JS_COMMENT = "__js_comment";
	
	public JavaScriptPartitionScanner(){
		IToken comment  = new Token(JS_COMMENT);
		
		ArrayList<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		rules.add(new MultiLineRule("/*" , "*/" ,comment));
		rules.add(new EndOfLineRule("//", comment));
		
		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
		//setRules(rules);
	}
}
