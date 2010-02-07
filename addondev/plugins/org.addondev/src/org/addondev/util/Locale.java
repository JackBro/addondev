package org.addondev.util;

import org.eclipse.core.resources.IProject;

public enum Locale {
	bg_BG("bg-BG"),
	ca_AD("ca-AD"),
	cs_CZ("cs-CZ"),
	da_DK("da-DK"),
	de_DE("de-DE"),
	en_US("en-US"),
	es_AR("es-AR"),
	es_ES("es-ES"),
	fr_FR("fr-FR"),
	hu_HU("hu-HU"),
	it_IT("it-IT"),
	ja_JP("ja-JP"),
	ko_KR("ko-KR"),
	nl_NL("nl-NL"),
	pl_PL("pl-PL"),
	pt_BR("pt-BR"),
	ro_RO("ro-RO"),
	ru_RU("ru-RU"),
	sk_SK("sk-SK"),
	sv_SE("sv-SE"),
	tr_TR("tr-TR"),
	uk_UA("uk-UA"),
	zh_CN("zh-CN"),
	zh_TW("zh-TW");

    private final String name;	
	
    public String getName()
    {
    	return this.name;
    }
    
    public static Locale getLocale(String name) {
    	if(name == null) return null;
    	
		for (Locale locale : Locale.values()) {
			if (locale.name.equals(name)) {
				return locale;
			}
		}
		return null;
	}
    
	private Locale(String name) {
	      this.name = name;
	}	
}
