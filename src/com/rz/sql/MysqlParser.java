package com.rz.sql;

public class MysqlParser extends AbstractParser
{
	public String getPageSql(String sql, int pageNum, int pageSize)
	{
		int start = (pageNum - 1) * pageSize;
		int end = pageNum * pageSize;
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
		sqlBuilder.append(sql);
		sqlBuilder.append(" limit ");
		sqlBuilder.append(start);
		sqlBuilder.append(",");
		sqlBuilder.append(end);
		return sqlBuilder.toString();
	}
}