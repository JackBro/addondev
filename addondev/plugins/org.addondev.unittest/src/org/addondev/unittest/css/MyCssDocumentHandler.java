package org.addondev.unittest.css;

import org.apache.batik.css.engine.StyleDeclaration;
import org.apache.batik.css.engine.StyleRule;
import org.apache.batik.css.engine.StyleSheet;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SelectorList;

public class MyCssDocumentHandler extends DocumentAdapter{
	 public StyleSheet styleSheet = new StyleSheet();
	 protected StyleRule styleRule;
	 protected StyleDeclaration styleDeclaration;
	 
	public StyleSheet getStyleSheet() {
		return styleSheet;
	}

	@Override
	public void startSelector(SelectorList selectors) throws CSSException {
		// TODO Auto-generated method stub
		//super.startSelector(arg0);
        styleRule = new StyleRule();
        styleRule.setSelectorList(selectors);
        styleDeclaration = new StyleDeclaration();
        styleRule.setStyleDeclaration(styleDeclaration);
        styleSheet.append(styleRule);
	}

	@Override
	public void endSelector(SelectorList arg0) throws CSSException {
		// TODO Auto-generated method stub
		super.endSelector(arg0);
        styleRule = null;
        styleDeclaration = null;
	}

	@Override
	public void property(String arg0, LexicalUnit arg1, boolean arg2)
			throws CSSException {
		// TODO Auto-generated method stub
		super.property(arg0, arg1, arg2);
	}

	@Override
	public void startPage(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub
		super.startPage(arg0, arg1);
	}
	 
}
