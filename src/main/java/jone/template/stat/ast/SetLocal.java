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
 * SetLocal 设置局部变量
 * 
 * 通常用于 #define #include 指令内部需要与外层作用域区分，以便于定义重用型模块的场景
 * 也常用于 #for 循环内部的临时变量
 */
public class SetLocal  extends Stat {
	
	final ExprList exprList;
	
	public SetLocal(ExprList exprList, Location location) {
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #setLocal directive can not be blank", location);
		}
		
		for (Expr expr : exprList.getExprArray()) {
			if ( !(expr instanceof Assign) ) {
				throw new ParseException("#setLocal directive only supports assignment expressions", location);
			}
		}
		this.exprList = exprList;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		Ctrl ctrl = scope.getCtrl();
		try {
			ctrl.setLocalAssignment();
			exprList.eval(scope);
		} finally {
			ctrl.setWisdomAssignment();
		}
	}
}





