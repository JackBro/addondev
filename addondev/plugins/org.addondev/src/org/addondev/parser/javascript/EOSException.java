package org.addondev.parser.javascript;

public class EOSException extends Exception {
	// コンストラクタ
	EOSException() {
		super("EOF");
	}
}
