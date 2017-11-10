package jone.template;

import jone.template.expr.ast.ExprList;
import jone.template.stat.Location;
import jone.template.stat.ast.Output;

/**
 * IOutputDirectiveFactory
 * 用于定制自定义输出指令，替换系统默认输出指令，满足个性化需求
 * 
 * 用法：
 * 1：定义 MyOutput
 * public class MyOutput extends Output {
 *   public MyOutput(ExprList exprList) {
 *     super(exprList);
 *   }
 *   
 *   public void exec(Env env, Scope scope, Writer writer) {
 *     write(writer, exprList.eval(scope));
 *   }
 * }
 * 
 * 2：定义 MyOutputDirectiveFactory
 * public class MyOutputDirectiveFactory implements IOutputDirectiveFactory {
 *   public Output getOutputDirective(ExprList exprList) {
 *     return new MyOutput(exprList);
 *   }
 * }
 * 
 * 3：配置
 * engine.setOutputDirectiveFactory(new MyOutputDirectiveFactory())
 */
public interface IOutputDirectiveFactory {
	
	public Output getOutputDirective(ExprList exprList, Location location);
	
}



