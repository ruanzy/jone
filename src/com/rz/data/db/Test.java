package com.rz.data.db;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.rz.common.R;
import com.rz.data.db.sql.SQLExecutor;

public class Test
{
	public static void main(String[] args)
	{
		DB db = DBs.getDB("dbone");;
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("rzy.sql");
		db.runScript(new InputStreamReader(is));
		boolean f = db.existTable("USERS");
		System.out.println(f);
		SQLExecutor executor = new SQLExecutor(db);
		String sqlid1 = "user.count";
		String sqlid2 = "user.list";
		Map<String, String> params = new HashMap<String, String>();
		params.put("operator", "admin");
		R r = executor.pager(sqlid1, sqlid2, params, 1, 10);
		System.out.println(r);

	}
}
