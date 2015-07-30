package com.rz.sql;

import java.util.List;
import com.rz.common.Record;

public class Page
{
	private int pageNum;

	private int pageSize;

	private int startRow;

	private int endRow;

	private long total;

	private List<Record> data;

	private int pages;

	public Page(int pageNum, int pageSize)
	{
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.startRow = this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
		this.endRow = this.startRow + this.pageSize * (this.pageNum > 0 ? 1 : 0);
	}

	public List<Record> getData()
	{
		return data;
	}

	public int getPages()
	{
		return pages;
	}

	public int getEndRow()
	{
		return endRow;
	}

	public int getPageNum()
	{
		return pageNum;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public int getStartRow()
	{
		return startRow;
	}

	public long getTotal()
	{
		return total;
	}

	public void setTotal(long total)
	{
		this.total = total;
		if (pageSize > 0)
		{
			pages = (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
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
