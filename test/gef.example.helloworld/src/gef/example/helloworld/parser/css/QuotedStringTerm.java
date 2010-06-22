package gef.example.helloworld.parser.css;

public class QuotedStringTerm extends Term {

	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}

}
