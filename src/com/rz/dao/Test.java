package com.rz.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rz.common.R;
import com.rz.dao.sql.SQLExecutor;

public class Test
{
	public static void main(String[] args)
	{
		DB db = DBPool.getDB("mysql");
		SQLExecutor executor = new SQLExecutor(db);
		String sqlid = "user.select1";
		Map<String, String> params = new HashMap<String, String>();
		params.put("operator", "admin");
		params.put("page", "1");
		params.put("pagesize", "10");
		List<R> list = executor.pager(sqlid, params, 2, 3);
		for (R record : list)
		{
			System.out.println(record);
		}
	}
}
