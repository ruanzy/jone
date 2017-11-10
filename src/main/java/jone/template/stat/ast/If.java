package jone.template.stat.ast;

import java.io.Writer;

import jone.template.Env;
import jone.template.expr.ast.ExprList;
import jone.template.expr.ast.Logic;
import jone.template.stat.Location;
import jone.template.stat.ParseException;
import jone.template.stat.Scope;

/**
 * If
 */
public class If extends Stat {
	
	private ExprList cond;
	private Stat stat;
	private Stat elseIfOrElse;
	
	public If(ExprList cond, Stat stat, Location location) {
		if (cond.length() == 0) {
			throw new ParseException("The condition expression of #if statement can not be blank", location);
		}
		this.cond = cond;
		this.stat = stat;
	}
	
	/**
	 * take over setStat(...) method of super class
	 */
	public void setStat(Stat elseIfOrElse) {
		this.elseIfOrElse = elseIfOrElse;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		if (Logic.isTrue(cond.eval(scope))) {
			stat.exec(env, scope, writer);
		} else if (elseIfOrElse != null) {
			elseIfOrElse.exec(env, scope, writer);
		}
	}
}



