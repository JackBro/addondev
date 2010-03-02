package org.addondev.ui.editor.javascript;

import java.util.ArrayList;
import java.util.List;


import org.addondev.ui.ColorManager;
import org.addondev.ui.IColorManager;
import org.addondev.ui.preferences.AddonDevUIPrefConst;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class JavaScriptScanner extends RuleBasedScanner {
	
	private static class JavaWordDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}
		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}
	}
	
	private IPreferenceStore fStore;
	private IColorManager fColorManager;
	
	private Token fKeywordToken;
	
	
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
	
	
	
	public Token getKeywordToken() {
		return fKeywordToken;
	}

	public JavaScriptScanner(IPreferenceStore fPreferenceStore) {
		super();
		this.fStore = fPreferenceStore;
		fColorManager = ColorManager.getInstance();//.setPreferenceStore(fStore);
		fColorManager.setPreferenceStore(fStore);
		List<IRule> rules = createRules();
		setRules(rules.toArray(new IRule[rules.size()]));
	}
	
	protected List<IRule> createRules(){
				
		//Color backgroundcolor = ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_JAVASCRIPT_BACKGROUND);
		//Color keywordcolor =  ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD);
		Color backgroundcolor = fColorManager.getColor(AddonDevUIPrefConst.COLOR_JAVASCRIPT_BACKGROUND);
		Color keywordcolor =  fColorManager.getColor(AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD);
		
		boolean bold = fStore.getBoolean(AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD + AddonDevUIPrefConst.BOLD_SUFFIX);
		boolean italic = fStore.getBoolean(AddonDevUIPrefConst.COLOR_JAVASCRIPT_KEYWORD + AddonDevUIPrefConst.ITALIC_SUFFIX);
		
		int keywordstyle = (bold?SWT.BOLD:SWT.NORMAL) | (italic?SWT.ITALIC:SWT.NORMAL);
		fKeywordToken = new Token(new TextAttribute(keywordcolor, backgroundcolor, keywordstyle));//SWT.BOLD));

		//Color defaultColor = ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_JAVASCRIPT_FOREGROUND);
		Color defaultColor = fColorManager.getColor(AddonDevUIPrefConst.COLOR_JAVASCRIPT_FOREGROUND);
		IToken defaulttoken = new Token(new TextAttribute(defaultColor));
		
		List<IRule> rules = new ArrayList<IRule>();
		WordRule wordRule = new WordRule(new JavaWordDetector(), defaulttoken);
		for (String keyword : fKeywords) {
			 wordRule.addWord(keyword, fKeywordToken); // ワードの追加
		}
		rules.add(wordRule);
		
		//Color stringcolor  = ResourceManager.getInstance().getColor(fPreferenceStore, AddonDevUIPrefConst.COLOR_JAVASCRIPT_STRING);
		//IToken stringtoken = new Token(new TextAttribute(stringcolor));
		
		//Color commnetcolor  = ResourceManager.getInstance().getColor(fStore, AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT);
		Color commnetcolor  = fColorManager.getColor(AddonDevUIPrefConst.COLOR_JAVASCRIPT_COMMENT);
		IToken commnettoken = new Token(new TextAttribute(commnetcolor));
		
		rules.add(new EndOfLineRule("//", commnettoken));
		//rules.add(new SingleLineRule("\"", "\"", stringtoken, '\\'));
		//rules.add(new SingleLineRule("'", "'", stringtoken, '\\'));

		return rules;
	}
}
