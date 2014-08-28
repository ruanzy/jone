package org.rzy.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.rzy.dao.Dao;
import org.rzy.util.Pager;

public final class Query
{
	public static Dao dao = Dao.getInstance();

	private Query()
	{

	}

	public static Map<String, Object> findOne(String sqlid, Map<String, ?> params)
	{
		String sql = DynamicSQL.getSQL(sqlid, params);
		List<Map<String, Object>> list = dao.find(sql);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public static List<Map<String, Object>> find(String sqlid, Map<String, ?> params)
	{
		String sql = DynamicSQL.getSQL(sqlid, params);
		return dao.find(sql);
	}

	public static Pager pager(String countsqlid, String pagersqlid, Map<String, ?> params)
	{
		int page = org.apache.commons.lang.math.NumberUtils.toInt(params.get("page").toString(), 1);
		int pagesize = org.apache.commons.lang.math.NumberUtils.toInt(params.get("pagesize").toString(), 10);
		Pager pager = new Pager(page, pagesize);
		String sql1 = DynamicSQL.getSQL(countsqlid, params);
		String sql2 = DynamicSQL.getSQL(pagersqlid, params);
		int total = 0;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Object scalar = dao.scalar(sql1);
		if (scalar != null)
		{
			total = Integer.valueOf(scalar.toString());
			pager.setTotal(total);
		}
		if (total > 0)
		{
			int pagecount = (total % pagesize == 0) ? (total / pagesize) : (total / pagesize + 1);
			if (pagecount > 0)
			{
				page = (pagecount < page) ? pagecount : page;
			}
			data = dao.pager(sql2, page, pagesize);
			pager.setData(data);
		}
		return pager;
	}

}