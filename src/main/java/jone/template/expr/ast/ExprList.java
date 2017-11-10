package jone.template.expr.ast;

import java.util.List;

import jone.template.TemplateException;
import jone.template.stat.Scope;

/**
 * ExprList
 */
public class ExprList extends Expr {
	
	public static final Expr[] NULL_EXPR_ARRAY = new Expr[0];
	public static final Object[] NULL_OBJECT_ARRAY =  new Object[0];
	public static final ExprList NULL_EXPR_LIST = new ExprList();
	
	private Expr[] exprArray;
	
	private ExprList() {
		this.exprArray = NULL_EXPR_ARRAY;
	}
	
	public ExprList(List<Expr> exprList) {
		if (exprList != null && exprList.size() > 0) {
			exprArray = exprList.toArray(new Expr[exprList.size()]);
		} else {
			exprArray = NULL_EXPR_ARRAY;
		}
	}
	
	public Expr[] getExprArray() {
		return exprArray;
	}
	
	public Expr getExpr(int index) {
		if (index < 0 || index >= exprArray.length) {
			throw new TemplateException("Index out of bounds: index = " + index + ", length = " + exprArray.length, location);
		}
		return exprArray[index];
	}
	
	public int length() {
		return exprArray.length;
	}
	
	/**
	 * 对所有表达式求值，只返回最后一个表达式的值
	 */
	public Object eval(Scope scope) {
		Object ret = null;
		for (Expr expr : exprArray) {
			ret = expr.eval(scope);
		}
		return ret;
	}
	
	/**
	 * 对所有表达式求值，并返回所有表达式的值
	 */
	public Object[] evalExprList(Scope scope) {
		if (exprArray.length == 0) {
			return NULL_OBJECT_ARRAY;
		}
		
		Object[] ret = new Object[exprArray.length];
		for (int i=0; i<exprArray.length; i++) {
			ret[i] = exprArray[i].eval(scope);
		}
		return ret;
	}
}



