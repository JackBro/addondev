package org.addondev.ui.template;

import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.TemplateContextType;

public class JavaScriptTemplateContextType extends TemplateContextType {
	
	public static final String JAVASCRIPT_CONTEXT_TYPE = "org.addondev.ui.template.javascript";

	public JavaScriptTemplateContextType() {
		// TODO Auto-generated constructor stub
        addResolver(new GlobalTemplateVariables.Cursor());
        addResolver(new GlobalTemplateVariables.WordSelection());
        addResolver(new GlobalTemplateVariables.LineSelection());
        addResolver(new GlobalTemplateVariables.Dollar());
        addResolver(new GlobalTemplateVariables.Date());
        addResolver(new GlobalTemplateVariables.Year());
        addResolver(new GlobalTemplateVariables.Time());
        addResolver(new GlobalTemplateVariables.User());
	}

	public JavaScriptTemplateContextType(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public JavaScriptTemplateContextType(String id, String name) {
		super(id, name);
		// TODO Auto-generated constructor stub
	}

}
