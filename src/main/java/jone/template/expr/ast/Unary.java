package jone.template.expr.ast;

import java.math.BigDecimal;
import jone.template.TemplateException;
import jone.template.expr.Sym;
import jone.template.stat.Location;
import jone.template.stat.ParseException;
import jone.template.stat.Scope;

/**
 * unary : ('!' | '+' | '-'| '++' | '--') expr
 * 
 * 只支持 +expr 与 -expr
 * !expr、 ++expr、 --expr 分别由 Logic、IncDec 支持
 */
public class Unary extends Expr {
	
	private Sym op;
	private Expr expr;
	
	public Unary(Sym op, Expr expr, Location location) {
		if (expr == null) {
			throw new ParseException("The parameter of \"" + op.value() + "\" operator can not be blank", location);
		}
		this.op = op;
		this.expr = expr;
		this.location = location;
	}
	
	/**
	 * unary : ('!' | '+' | '-'| '++' | '--') expr
	 */
	public Object eval(Scope scope) {
		Object value = expr.eval(scope);
		if (value == null) {
			if (scope.getCtrl().isNullSafe()) {
				return null;
			}
			throw new TemplateException("The parameter of \"" + op.value() + "\" operator can not be blank", location);
		}
		if (! (value instanceof Number) ) {
			throw new TemplateException(op.value() + " operator only support int long float double BigDecimal type", location);
		}
		
		switch (op) {
		case ADD:
			return value;
		case SUB:
			Number n = (Number)value;
			if (n instanceof Integer) {
                return Integer.valueOf(-n.intValue());
            }
			if (n instanceof Long) {
                return Long.valueOf(-n.longValue());
            }
			if (n instanceof Float) {
                return Float.valueOf(-n.floatValue());
            }
			if (n instanceof Double) {
                return Double.valueOf(-n.doubleValue());
            }
			if (n instanceof BigDecimal) {
            	return ((BigDecimal)n).negate();
			}
            throw new TemplateException("Unsupported data type: " + n.getClass().getName(), location);
		default :
			throw new TemplateException("Unsupported operator: " + op.value(), location);
		}
	}
}





