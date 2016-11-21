package com.rz.sql;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

public class Test
{
	public static void main(String[] args)
	{
		String sql = "select * from users order by username desc";
//		PageHelper.startPage(1, 3);
//		Pager p = PageHelper.pager(sql);
//		System.out.println(JSON.toJSON(p));
		Statement stmt = null;
		try
		{
			stmt = CCJSqlParserUtil.parse(sql);
		}
		catch (Throwable e)
		{
			
		}
		Select select = (Select) stmt;
		SelectBody selectBody = select.getSelectBody();
		System.out.println(selectBody);
	}
}
