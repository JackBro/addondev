package gef.example.helloworld.parser.css;

public class Declaration {
	private String name;
	private Expr expr;
	private boolean important = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expr getExpr() {
		return expr;
	}
	public void setExpr(Expr expr) {
		this.expr = expr;
	}
	public boolean isImportant() {
		return important;
	}
	public void setImportant(boolean important) {
		this.important = important;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(":");
		sb.append(expr.toString());
		if(important) sb.append(" !important");
		return sb.toString();
	}
	
}
