package jp.addondev.editor.javascript;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;

public class JavaScriptScanner extends RuleBasedScanner {
	private IPreferenceStore fPreferenceStore;
	static String[] fKeywords= {
		"let",
		"abstract",
		"break",
		"case", "catch", "class", "const", "continue",
		"default", "delete", "debugger", "do",
		"else", "export", "extends",
		"final", "finally", "for",  "function",
		"goto", 
		"if", "implements", "in", "instanceof", "interface",
		"new", 
		"package", "private", "protected", "public",
		"static", "super", "switch", "synchronized", 
		"this", "throw", "throws", "transient", "try","typeof",
		"var", "volatile",
		"while"
	};
	public JavaScriptScanner(IPreferenceStore fPreferenceStore) {
		super();
		this.fPreferenceStore = fPreferenceStore;
	}
	
	protected List<IRule> createRules(){
		List<IRule> rules = new ArrayList<IRule>();
		
		
		return rules;
	}
}
