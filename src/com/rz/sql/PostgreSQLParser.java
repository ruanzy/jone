package com.rz.sql;

public class PostgreSQLParser extends AbstractParser
{
	public String getPageSql(String sql, int pageNum, int pageSize)
	{
		int start = (pageNum - 1) * pageSize;
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
		sqlBuilder.append(sql);
		sqlBuilder.append(" limit ");
		sqlBuilder.append(start);
		sqlBuilder.append(" offset ");
		sqlBuilder.append(pageSize);
		return sqlBuilder.toString();
	}
}