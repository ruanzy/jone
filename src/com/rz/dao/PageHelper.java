package com.rz.dao;

import java.util.List;

public class PageHelper
{
	private int page = 1;
	private int limit = 0;
	private long total = 0;
	private List<?> data;
	private final static ThreadLocal<PageHelper> pageHelper = new ThreadLocal<PageHelper>();

	private PageHelper()
	{
	}

	public static PageHelper create(int page, int limit)
	{
		PageHelper ph = new PageHelper();
		ph.page = page;
		ph.limit = limit;
		pageHelper.set(ph);
		return ph;
	}

	public int getPage()
	{
		return pageHelper.get().page;
	}

	public int getLimit()
	{
		return pageHelper.get().limit;
	}

	public void setTotal(long total)
	{
		pageHelper.get().total = total;
	}
	
	public long getTotal()
	{
		return pageHelper.get().total;
	}

	public List<?> getData()
	{
		return pageHelper.get().data;
	}
}
