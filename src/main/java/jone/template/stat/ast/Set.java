package jone.template.stat.ast;

import java.io.Writer;

import jone.template.Env;
import jone.template.expr.ast.Assign;
import jone.template.expr.ast.Expr;
import jone.template.expr.ast.ExprList;
import jone.template.stat.Location;
import jone.template.stat.ParseException;
import jone.template.stat.Scope;

/**
 * Set 赋值，从内向外作用域查找变量，找到则替换变量值，否则在顶层作用域赋值
 * 
 * 用法：
 * 1：#set(k = v)
 * 2：#set(k1 = v1, k2 = v2, ..., kn = vn)
 * 3：#set(x = 1+2)
 * 4：#set(x = 1+2, y = 3>4, ..., z = c ? a : b)
 */
public class Set extends Stat {
	
	private ExprList exprList;
	
	public Set(ExprList exprList, Location location) {
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #set directive can not be blank", location);
		}
		
		for (Expr expr : exprList.getExprArray()) {
			if ( !(expr instanceof Assign) ) {
				throw new ParseException("#set directive only supports assignment expressions", location);
			}
		}
		this.exprList = exprList;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		scope.getCtrl().setWisdomAssignment();
		exprList.eval(scope);
	}
}

