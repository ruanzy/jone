package rzy.core;

import java.util.List;
import java.util.Map;

public class Pager
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

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public List<Map<String, Object>> getData()
	{
		return data;
	}

	public void setData(List<Map<String, Object>> data)
	{
		this.data = data;
	}

	private int page = 1;
	private int pagesize = 10;
	private int total = 0;
	private List<Map<String, Object>> data;
}
