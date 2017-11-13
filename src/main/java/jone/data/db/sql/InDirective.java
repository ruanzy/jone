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

public class InDirective extends Directive {

    private int index = -1;

    public void setExprList(ExprList exprList) {
        if (exprList.length() == 1) {
            Expr expr = exprList.getExpr(0);
            if (expr instanceof Const && ((Const) expr).isInt()) {
                index = ((Const) expr).getInt();
                if (index < 0) {
                    throw new ParseException("The index of para array must greater than -1", location);
                }
            }
        }
        this.exprList = exprList;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        SqlPara sqlPara = (SqlPara) scope.get("_SQL_PARA_");
        if (sqlPara == null) {
            throw new TemplateException("#in invoked by getSqlPara(...) method only", location);
        }
        StringBuffer inPlaceholder = new StringBuffer();
        String valueStr = "";
        if (index == -1) {
            Expr[] exprArray = exprList.getExprArray();
            Object ret = null;
            for (Expr expr : exprArray) {
                boolean hasPara = scope.getData().containsKey(expr.toString());
                if (hasPara) {
                    ret = expr.eval(scope);
                    if (ret==null) {//in类的参数值不可为空
                        throw new TemplateException(
                                "The value of parameter '"+expr.toString()+"' must not be null",
                                location);
                    }
                } else {//没有传参数抛异常
                    throw new TemplateException(
                            "The parameter '"+expr.toString()+"' must be define",
                            location);
                }
            }
            valueStr = String.valueOf(ret);
//            sqlPara.addPara(exprList.eval(scope));
        } else {
            Object[] paras = (Object[]) scope.get("_PARA_ARRAY_");
            if (paras == null) {
                throw new TemplateException(
                        "The #in(" + index + ") directive must invoked by getSqlPara(String, Object...) method",
                        location);
            }
            if (index >= paras.length) {
                throw new TemplateException("The index of #in directive is out of bounds: " + index, location);
            }
            valueStr  = String.valueOf(paras[index]);
//            sqlPara.addPara(paras[index]);
        }
        Object[] retArrray = valueStr.split(",");
        for (int i = 0; i < retArrray.length; i++) {
            inPlaceholder.append("?");
            if (i != retArrray.length - 1) {
                inPlaceholder.append(",");
            }
            sqlPara.addPara(retArrray[i]);
        }
        write(writer, inPlaceholder.toString());
    }
}
