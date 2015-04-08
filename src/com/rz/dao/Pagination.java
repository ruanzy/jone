package com.rz.dao;

import java.util.List;

public class Pagination
{
	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getPagesize()
	{
		return pagesize;
	}

	public void setPagesize(int pagesize)
	{
		this.pagesize = pagesize;
	}

	public long getTotal()
	{
		return total;
	}

	public void setTotal(long total)
	{
		this.total = total;
	}
	
	public int getPagecount()
	{
		return pagecount;
	}

	public void setPagecount(int pagecount)
	{
		this.pagecount = pagecount;
	}

	public List<?> getRows()
	{
		return rows;
	}

	public void setRows(List<?> rows)
	{
		this.rows = rows;
	}

	private int page = 1;
	private int pagesize = 10;
	private long total = 0;
	private int pagecount = 0;
	private List<?> rows;
}
