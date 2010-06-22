package gef.example.helloworld.parser.css;

import java.util.ArrayList;
import java.util.List;

public class Expr {
	private List<Term> terms = new ArrayList<Term>();

	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}
	
}
