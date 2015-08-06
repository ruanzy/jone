package com.rz.sql;

import java.util.List;
import com.rz.common.Record;

public class Pager
{
	private int page;

	private int pagesize;

	private long total;

	private List<Record> data;

	private int pages;

	public Pager(int page, int pagesize)
	{
		this.page = page;
		this.pagesize = pagesize;
	}

	public List<Record> getData()
	{
		return data;
	}

	public int getPages()
	{
		return pages;
	}

	public int getPage()
	{
		return page;
	}

	public int getPagesize()
	{
		return pagesize;
	}

	public long getTotal()
	{
		return total;
	}

	public void setTotal(long total)
	{
		this.total = total;
		if (pagesize > 0)
		{
			pages = (int) (total / pagesize + ((total % pagesize == 0) ? 0 : 1));
		}
		else
		{
			pages = 0;
		}
	}

	public void setData(List<Record> data)
	{
		this.data = data;
	}
}
