package com.rz.sql;

import java.util.ArrayList;
import java.util.List;
import com.rz.common.Record;
import com.rz.dao.Dao;

public class PageHelper
{
	private static final ThreadLocal<Pager> LOCAL_PAGE = new ThreadLocal<Pager>();

	public static void startPage(int pageNum, int pageSize)
	{
		LOCAL_PAGE.set(new Pager(pageNum, pageSize));
	}

	public static Pager pager(String sql, Object... params)
	{
		Pager pager = LOCAL_PAGE.get();
		Parser parser = AbstractParser.newParser("mysql");
		String countSql = parser.getCountSql(sql);
		String pageSql = parser.getPageSql(sql, pager.getPageNum(), pager.getPageSize());
		Long count = 0L;
		Object scalar = Dao.getInstance().scalar(countSql, params);
		if (scalar != null)
		{
			count = Long.valueOf(scalar.toString());
			pager.setTotal(count);
		}
		if (count > 0)
		{
			List<Record> rs = Dao.getInstance().find(pageSql, params);
			pager.setData(rs);
		}
		else
		{
			pager.setData(new ArrayList<Record>());
		}
		return pager;
	}
}
