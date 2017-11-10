package jone.template.stat.ast;

import java.io.Writer;
import jone.template.Env;
import jone.template.stat.Scope;

/**
 * Continue
 */
public class Continue extends Stat {
	
	public static final Continue me = new Continue();
	
	private Continue() {
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		scope.getCtrl().setContinue();
	}
}




