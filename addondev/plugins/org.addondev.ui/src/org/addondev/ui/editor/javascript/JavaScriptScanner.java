package org.addondev.ui.editor.javascript;

import java.util.ArrayList;
import java.util.List;


import org.addondev.preferences.PrefConst;
import org.addondev.preferences.ResourceManager;
import org.eclipse.jdt.internal.ui.text.JavaWordDetector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

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
		List<IRule> rules = createRules();
		setRules(rules.toArray(new IRule[rules.size()]));
	}
	
	protected List<IRule> createRules(){
				
		Color backgroundcolor = ResourceManager.getInstance().getColor(AddonDevUIPrefConst.COLOR_JAVASCRIPT_BACKGROUND);
		Color keywordcolor =  ResourceManager.getInstance().getColor(AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD);

		IToken keywordtoken = new Token(new TextAttribute(keywordcolor, backgroundcolor, SWT.BOLD));

		Color defaultColor = ResourceManager.getInstance().getColor(AddonDevUIPrefConst.COLOR_JAVASCRIPT_FOREGROUND);
		IToken defaulttoken = new Token(new TextAttribute(defaultColor));
		
		List<IRule> rules = new ArrayList<IRule>();
		WordRule wordRule = new WordRule(new JavaWordDetector(), defaulttoken);
		for (String keyword : fKeywords) {
			 wordRule.addWord(keyword, keywordtoken); // ワードの追加
		}
		rules.add(wordRule);
		
		Color stringcolor  = ResourceManager.getInstance().getColor(AddonDevUIPrefConst.COLOR_JAVASCRIPT_STRING);
		IToken stringtoken = new Token(new TextAttribute(stringcolor));
		rules.add(new SingleLineRule("\"", "\"", stringtoken, '\\'));
		rules.add(new SingleLineRule("'", "'", stringtoken, '\\'));
		
		return rules;
	}
}
