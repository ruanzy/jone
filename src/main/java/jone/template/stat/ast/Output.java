package jone.template.stat.ast;

import java.io.Writer;
import jone.template.Env;
import jone.template.TemplateException;
import jone.template.expr.ast.ExprList;
import jone.template.stat.Location;
import jone.template.stat.ParseException;
import jone.template.stat.Scope;

/**
 * Output 输出指令
 * 
 * 用法：
 * 1：#(value)
 * 2：#(x = 1, y = 2, x + y)
 * 3：#(seoTitle ?? 'JFinal 极速开发社区')
 */
public class Output extends Stat {
	
	private ExprList exprList;
	
	public Output(ExprList exprList, Location location) {
		if (exprList.length() == 0) {
			throw new ParseException("The expression of output directive like #(expression) can not be blank", location);
		}
		this.exprList = exprList;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		try {
			Object value = exprList.eval(scope);
			if (value != null) {
				String str = value.toString();
				writer.write(str, 0, str.length());
			}
		} catch(TemplateException e) {
			throw e;
		} catch(Exception e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
}




