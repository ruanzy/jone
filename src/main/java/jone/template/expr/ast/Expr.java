package jone.template.expr.ast;

import jone.template.stat.Location;
import jone.template.stat.Scope;

/**
 * Expr
 */
public abstract class Expr {
	
	protected Location location;
	
	public abstract Object eval(Scope scope);
}




