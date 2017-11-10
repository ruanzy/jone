package jone.template.stat.ast;

import java.io.IOException;
import java.io.Writer;

import jone.template.Env;
import jone.template.TemplateException;
import jone.template.expr.ast.ExprList;
import jone.template.stat.Location;
import jone.template.stat.Scope;

/**
 * Stat
 */
public abstract class Stat {
	
	protected Location location;
	
	public Stat setLocation(Location location) {
		this.location = location;
		return this;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setExprList(ExprList exprList) {
	}
	
	public void setStat(Stat stat) {
	}
	
	public abstract void exec(Env env, Scope scope, Writer writer);
	
	public boolean hasEnd() {
		return false;
	}
	
	protected void write(Writer writer, String str) {
		try {
			writer.write(str, 0, str.length());
		} catch (IOException e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
	
	protected void write(Writer writer, char[] chars) {
		try {
			writer.write(chars, 0, chars.length);
		} catch (IOException e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
}







