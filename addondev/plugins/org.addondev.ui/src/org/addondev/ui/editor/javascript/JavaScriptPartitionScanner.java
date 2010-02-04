package org.addondev.ui.editor.javascript;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class JavaScriptPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JS_COMMENT = "__js_comment";
	public final static String JS_STRING = "__js_string";
	
	public JavaScriptPartitionScanner(){
		
		ArrayList<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		
		IToken comment  = new Token(JS_COMMENT);
		rules.add(new MultiLineRule("/*" , "*/" ,comment));
		rules.add(new EndOfLineRule("//", comment));
		
		IToken string = new Token(JS_STRING);
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("\'", "\'", string, '\\'));
		
		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
		//setRules(rules);
	}
}
