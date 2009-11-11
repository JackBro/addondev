package jp.addondev.parser.javascript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueNode {

	private static Pattern pattern = Pattern.compile("@returns\\s+\\{\\s*(.*)\\s*\\}");
	private String returns;
	public String getReturns() {
		if(returns ==null)
		{
			 Matcher m = pattern.matcher(jsDoc);
			 if (m.find()) {
				 returns = m.group(1);
			 }
		}
		return returns;
	}
	private ObjectType objectType;
	private JsNode node;
	private String jsDoc;
	
	public JsNode getNode() {
		return node;
	}

	public void setNode(JsNode node) {
		this.node = node;
	}

	public String getJsDoc() {
		return jsDoc;
	}

	public void setJsDoc(String jsDoc) {
		this.jsDoc = jsDoc;
	}

	public ObjectType getObjectType() {
		return objectType;
	}
	
	public ValueNode()
	{
		this.node = null;
		this.jsDoc = null;		
	}
	public ValueNode(String jsDoc)
	{
		this.node = null;
		this.jsDoc = jsDoc;
	}
	public ValueNode(JsNode node)
	{
		this.node = node;
		this.jsDoc = null;
	}
}
