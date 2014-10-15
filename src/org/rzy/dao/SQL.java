package org.rzy.dao;

import java.util.List;

public class SQL
{
	private String sql;
	private List<Object> ps;

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public List<Object> getPs()
	{
		return ps;
	}

	public void setPs(List<Object> ps)
	{
		this.ps = ps;
	}
}
