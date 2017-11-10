package jone.template.stat.ast;

import java.io.Writer;
import jone.template.Env;
import jone.template.stat.Scope;

/**
 * Else
 */
public class Else extends Stat {
	
	private Stat stat;
	
	public Else(Stat stat) {
		this.stat = stat;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		stat.exec(env, scope, writer);
	}
}



