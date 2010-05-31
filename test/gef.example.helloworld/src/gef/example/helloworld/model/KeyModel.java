package gef.example.helloworld.model;

import gef.example.helloworld.viewers.KeyModifiersPropertyDescriptor;

public class KeyModel extends AbstractElementModel {

	public static final String ATTR_COMMAND = "command";
	public static final String ATTR_DISABLED = "disabled";
	public static final String ATTR_KEY = "key";
	public static final String ATTR_KEYCODE = "keycode";
	public static final String ATTR_MODIFIERS = "modifiers";
	public static final String ATTR_ONCOMMAND = "oncommand";
	
	static	String[] keycodes = {"VK_CANCEL","VK_BACK","VK_TAB","VK_CLEAR","VK_RETURN",
		"VK_ENTER","VK_SHIFT","VK_CONTROL","VK_ALT","VK_PAUSE",
		"VK_CAPS_LOCK","VK_ESCAPE","VK_SPACE","VK_PAGE_UP",
		"VK_PAGE_DOWN","VK_END","VK_HOME","VK_LEFT","VK_UP",
		"VK_RIGHT","VK_DOWN","VK_PRINTSCREEN","VK_INSERT","VK_DELETE",
		"VK_0","VK_1","VK_2","VK_3","VK_4","VK_5","VK_6","VK_7","VK_8","VK_9",
		"VK_SEMICOLON","VK_EQUALS",
		"VK_A","VK_B","VK_C","VK_D","VK_E","VK_F","VK_G","VK_H","VK_I","VK_J",
		"VK_K","VK_L","VK_M","VK_N","VK_O","VK_P","VK_Q","VK_R","VK_S","VK_T",
		"VK_U","VK_V","VK_W","VK_X","VK_Y","VK_Z",
		"VK_NUMPAD0","VK_NUMPAD1","VK_NUMPAD2","VK_NUMPAD3","VK_NUMPAD4","VK_NUMPAD5",
		"VK_NUMPAD6","VK_NUMPAD7","VK_NUMPAD8","VK_NUMPAD9",
		"VK_MULTIPLY","VK_ADD","VK_SEPARATOR","VK_SUBTRACT","VK_DECIMAL","VK_DIVIDE",
		"VK_F1","VK_F2","VK_F3","VK_F4","VK_F5","VK_F6","VK_F7","VK_F8","VK_F9","VK_F10",
		"VK_F11","VK_F12","VK_F13","VK_F14","VK_F15","VK_F16","VK_F17","VK_F18","VK_F19",
		"VK_F20","VK_F21","VK_F22","VK_F23","VK_F24",
		"VK_NUM_LOCK","VK_SCROLL_LOCK","VK_COMMA","VK_PERIOD","VK_SLASH","VK_BACK_QUOTE",
		"VK_OPEN_BRACKET","VK_BACK_SLASH","VK_CLOSE_BRACKET","VK_QUOTE","VK_HELP"};
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "key";
	}

	@Override
	public void installModelProperty() {
		// TODO Auto-generated method stub
		super.installModelProperty();
		AddAttrTextProperty(ATTR_COMMAND, ATTR_COMMAND, "");
		AddAttrBoolProperty(ATTR_DISABLED, ATTR_DISABLED, false);
		
		String[] keys = {"",""};
		AddAttrStringsProperty(ATTR_KEY, ATTR_KEY, keys, "");
		
		AddAttrStringsProperty(ATTR_KEYCODE, ATTR_KEYCODE, keycodes, "");
		
		KeyModifiersPropertyDescriptor keymoddes = new KeyModifiersPropertyDescriptor(ATTR_MODIFIERS, ATTR_MODIFIERS);
		AddProperty(ATTR_MODIFIERS, keymoddes, "");
		ModelProperty keymoddesprop = getModelProperty(ATTR_MODIFIERS);
		keymoddesprop.setAttr(true);
		keymoddesprop.setIsSerialize(true);
		
		AddAttrMultiLineTextProperty(ATTR_ONCOMMAND, ATTR_ONCOMMAND, "");
	}

}
