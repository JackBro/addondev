package org.addondev.ui.editor.xul.preview;


public enum EnumXULWindow {
	PREFWINDOW("prefwindow"),
	WINDOW("window"),
	DIALOG("dialog"),
	PAGE("page"),
	NONE("none");
	
	private String name;	
	
    public static EnumXULWindow getXULWindow(String name) {
    	if(name == null) return null;
    	
		for (EnumXULWindow window : EnumXULWindow.values()) {
			if (window.name.equals(name)) {
				return window;
			}
		}
		return null;
	}
    
	private EnumXULWindow(String name) {
	      this.name = name;
	}	
}
