package com.rz.dao;

import java.util.HashMap;
import java.util.Map;
import com.rz.common.R;
import com.rz.dao.sql.SQLExecutor;

public class Test
{
	public static void main(String[] args)
	{
		DB db = DBs.getDB("mysql");
		SQLExecutor executor = new SQLExecutor(db);
		String sqlid1 = "user.count";
		String sqlid2 = "user.list";
		Map<String, String> params = new HashMap<String, String>();
		params.put("operator", "admin");
		R r = executor.pager(sqlid1, sqlid2, params, 1, 10);
		System.out.println(r);

	}
}
