package org.addondev.parser.javascript;

public class Scope {
	private int startOffset;
	private int endOffset;
	private JsNode node;
	public int getStart() {
		return startOffset;
	}
	public void setStart(int start) {
		this.startOffset = start;
	}
	public int getEnd() {
		return endOffset;
	}
	public void setEnd(int end) {
		this.endOffset = end;
	}
	public JsNode getNode() {
		return node;
	}
	public void setNode(JsNode node) {
		this.node = node;
	}
	public Scope(int startOffset, int endOffset, JsNode node) {
		super();
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.node = node;
	}
	public Scope(int startOffset, JsNode node) {
		super();
		this.startOffset = startOffset;
		this.node = node;
	}
}
