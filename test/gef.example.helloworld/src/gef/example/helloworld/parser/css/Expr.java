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

	public Expr() {
		super();
	}

	public Expr(List<Term> terms) {
		super();
		this.terms = terms;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (Term term : terms) {
			sb.append(term.toString());
			sb.append(" ");
		}
		return sb.toString();
	}
	
}
