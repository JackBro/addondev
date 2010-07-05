package gef.example.helloworld.parser.css;

public class QuotedStringTerm extends Term {

	public QuotedStringTerm(String url) {
		// TODO Auto-generated constructor stub
		setValue(url);
	}

	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}

}
