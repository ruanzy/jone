package com.rz.sql;

import java.util.List;
import com.rz.common.Record;
import com.rz.dao.Dao;

public class PageHelper
{
	private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();

	public static void startPage(int pageNum, int pageSize)
	{
		LOCAL_PAGE.set(new Page(pageNum, pageSize));
	}

	public static Page pager(String sql, Object... params)
	{
		Page page = LOCAL_PAGE.get();
		Parser parser = AbstractParser.newParser("mysql");
		String countSql = parser.getCountSql(sql);
		String pageSql = parser.getPageSql(sql, page.getPageNum(), page.getPageSize());
		Object obj = Dao.getInstance().scalar(countSql, params);
		Long count = Long.valueOf(obj.toString());
		List<Record> rs = Dao.getInstance().find(pageSql, params);
		page.setTotal(count);
		page.setData(rs);
		return page;

	}
}
