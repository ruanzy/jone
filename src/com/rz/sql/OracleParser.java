package com.rz.sql;

public class OracleParser extends AbstractParser
{
	public String getPageSql(String sql, int pageNum, int pageSize)
	{
		int start = (pageNum - 1) * pageSize;
		int end = pageNum * pageSize;
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
		sqlBuilder.append("select * from ( select tmp_page.*, rownum row_id from ( ");
		sqlBuilder.append(sql);
		sqlBuilder.append(" ) tmp_page where rownum <= ");
		sqlBuilder.append(end);
		sqlBuilder.append(" ) where row_id > ");
		sqlBuilder.append(start);
		return sqlBuilder.toString();
	}
}