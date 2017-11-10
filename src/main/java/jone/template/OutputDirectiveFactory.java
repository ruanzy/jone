package jone.template;

import jone.template.expr.ast.ExprList;
import jone.template.stat.Location;
import jone.template.stat.ast.Output;

/**
 * OutputDirectiveFactory
 */
public class OutputDirectiveFactory implements IOutputDirectiveFactory {
	
	public static final OutputDirectiveFactory me = new OutputDirectiveFactory();
	
	public Output getOutputDirective(ExprList exprList, Location location) {
		return new Output(exprList, location);
	}
}

