package jp.addondev.parser.javascript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IJsPrimFunction {
	public String getName();
	public String Call(ArrayList<JsNode> args);
}
