package gef.example.helloworld.parser.css;

public class FunctionTerm extends Term {
	private Expr expr;

	public Expr getExpr() {
		return expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(getValue());
		sb.append("(");
		sb.append(expr.toString());
		sb.append(")");
		return sb.toString();
	}
}
