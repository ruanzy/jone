package jone.data.db.sql;

import java.io.Writer;

import jone.template.Directive;
import jone.template.Env;
import jone.template.TemplateException;
import jone.template.expr.ast.Const;
import jone.template.expr.ast.Expr;
import jone.template.expr.ast.ExprList;
import jone.template.stat.ParseException;
import jone.template.stat.Scope;

public class ParaDirective extends Directive {
	
	private int index = -1;
	
	public void setExprList(ExprList exprList) {
		if (exprList.length() == 1) {
			Expr expr = exprList.getExpr(0);
			if (expr instanceof Const && ((Const)expr).isInt()) {
				index = ((Const)expr).getInt();
				if (index < 0) {
					throw new ParseException("The index of para array must greater than -1", location);
				}
			}
		}
		this.exprList = exprList;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		SqlPara sqlPara = (SqlPara)scope.get("_SQL_PARA_");
		if (sqlPara == null) {
			throw new TemplateException("#para or #p directive invoked by getSqlPara(...) method only", location);
		}
		
		write(writer, "?");
		if (index == -1) {
			sqlPara.addPara(exprList.eval(scope));
		} else {
			Object[] paras = (Object[])scope.get("_PARA_ARRAY_");
			if (paras == null) {
				throw new TemplateException("The #para(" + index + ") directive must invoked by getSqlPara(String, Object...) method", location);
			}
			if (index >= paras.length) {
				throw new TemplateException("The index of #para directive is out of bounds: " + index, location);
			}
			sqlPara.addPara(paras[index]);
		}
	}
}



