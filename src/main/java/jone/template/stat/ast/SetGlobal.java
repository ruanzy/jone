package jone.template.stat.ast;

import java.io.Writer;

import jone.template.Env;
import jone.template.expr.ast.Assign;
import jone.template.expr.ast.Expr;
import jone.template.expr.ast.ExprList;
import jone.template.stat.Ctrl;
import jone.template.stat.Location;
import jone.template.stat.ParseException;
import jone.template.stat.Scope;

/**
 * SetLocal 设置全局变量，全局作用域是指本次请求的整个 template
 * 
 * 适用于极少数的在内层作用域中希望直接操作顶层作用域的场景
 */
public class SetGlobal  extends Stat {
	
	private ExprList exprList;
	
	public SetGlobal(ExprList exprList, Location location) {
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #setGlobal directive can not be blank", location);
		}
		
		for (Expr expr : exprList.getExprArray()) {
			if ( !(expr instanceof Assign) ) {
				throw new ParseException("#setGlobal directive only supports assignment expressions", location);
			}
		}
		this.exprList = exprList;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		Ctrl ctrl = scope.getCtrl();
		try {
			ctrl.setGlobalAssignment();
			exprList.eval(scope);
		} finally {
			ctrl.setWisdomAssignment();
		}
	}
}





