package com.rz.sql;

public class Test
{
	public static void main(String[] args)
	{
		String sql = "select a.name,a.age, b.* from a,b where name='abc' order by a.name desc";
		Parser parser = AbstractParser.newParser("mysql");
		String countSql = parser.getCountSql(sql);
		System.out.println(countSql);
		String pageSql = parser.getPageSql(sql);
		System.out.println(pageSql);
	}
}
