package jone.data.db.sql;

import java.io.Writer;
import java.util.Map;

import jone.kit.StrKit;
import jone.template.Directive;
import jone.template.Env;
import jone.template.Template;
import jone.template.expr.ast.Const;
import jone.template.expr.ast.Expr;
import jone.template.expr.ast.ExprList;
import jone.template.stat.ParseException;
import jone.template.stat.Scope;

public class SqlDirective extends Directive {
	
	private String id;
	
	public void setExprList(ExprList exprList) {
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #sql directive can not be blank", location);
		}
		if (exprList.length() > 1) {
			throw new ParseException("Only one parameter allowed for #sql directive", location);
		}
		Expr expr = exprList.getExpr(0);
		if (expr instanceof Const && ((Const)expr).isStr()) {
		} else {
			throw new ParseException("The parameter of #sql directive must be String", location);
		}
		
		this.id = ((Const)expr).getStr();
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		String namespace = (String)scope.get("NAMESPACE_KEY");
		String key = StrKit.isBlank(namespace) ? id : namespace + "." + id;
		Map<String, Template> sqlTemplateMap = SQLLoader.sqlTemplateMap;
		if (sqlTemplateMap.containsKey(key)) {
			throw new ParseException("Sql already exists with key : " + key, location);
		}
		sqlTemplateMap.put(key, new Template(env, stat));
	}
	
	public boolean hasEnd() {
		return true;
	}
}
