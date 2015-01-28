package com.rz.dao;

import java.util.List;

public class Pager
{
	public Pager(int page, int pagesize)
	{
		this.page = page;
		this.pagesize = pagesize;
	}

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

	public List<?> getData()
	{
		return data;
	}

	public void setData(List<?> data)
	{
		this.data = data;
	}

	private int page = 1;
	private int pagesize = 10;
	private long total = 0;
	private List<?> data;
}
