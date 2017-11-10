package jone.template.stat.ast;

import java.io.Writer;
import java.util.List;

import jone.template.Env;
import jone.template.TemplateException;
import jone.template.stat.Ctrl;
import jone.template.stat.Scope;

/**
 * StatList
 */
public class StatList extends Stat {
	
	public static final Stat[] NULL_STATS =  new Stat[0];
	private Stat[] statArray;
	
	public StatList(List<Stat> statList) {
		if (statList.size() > 0) {
			this.statArray = statList.toArray(new Stat[statList.size()]);
		} else {
			this.statArray = NULL_STATS;
		}
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		Ctrl ctrl = scope.getCtrl();
		for (Stat stat : statArray) {
			if (ctrl.isJump()) {
				break ;
			}
			stat.exec(env, scope, writer);	
		}
	}
	
	public int length() {
		return statArray.length;
	}
	
	public Stat getStat(int index) {
		if (index < 0 || index >= statArray.length) {
			throw new TemplateException("Index out of bounds: index = " + index + ", length = " + statArray.length, location);
		}
		return statArray[index];
	}
}


