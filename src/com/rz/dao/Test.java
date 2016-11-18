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
//		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("rzy.sql");
//		try
//		{
//			db.runScript(new InputStreamReader(is, "UTF8"));
//		}
//		catch (UnsupportedEncodingException e)
//		{
//			e.printStackTrace();
//		}
		String sql = "insert users values(2, 'admin', 'YWRtaW5fYWRtaW4=', 1, '2016-09-15', 1, 'email', '888888', '1', '2016-08-08 00:00:00', 'admin')";
		db.begin();
		db.update(sql);
		db.commit();
		SQLExecutor executor = new SQLExecutor(db);
		String sqlid = "user.list";
		Map<String, String> params = new HashMap<String, String>();
		params.put("operator", "admin");
		params.put("page", "1");
		params.put("pagesize", "10");
		List<R> list = executor.pager(sqlid, params, 1, 10);
		for (R record : list)
		{
			System.out.println(record);
		}
	}
}
