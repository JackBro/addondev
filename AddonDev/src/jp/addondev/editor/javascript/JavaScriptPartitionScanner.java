package jp.addondev.editor.javascript;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class JavaScriptPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JS_COMMENT = "__js_comment";
	
	public JavaScriptPartitionScanner(){
		IToken comment  = new Token(JS_COMMENT);
		
		IPredicateRule[] rules = new IPredicateRule[1];
		rules[0] = new MultiLineRule("/*" , "*/" ,comment);
		
		setPredicateRules(rules);
		//setRules(rules);
	}
}
