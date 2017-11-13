package jone.data.db;

import java.util.List;

import jone.R;
import jone.data.db.sql.SQLExecutor;

public class Test
{
	public static void main(String[] args)
	{
		DB db = DBs.getDB("h2");
//		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("rzy.sql");
//		db.runScript(new InputStreamReader(is));
//		boolean f = db.existTable("USERS");
//		System.out.println(f);
		SQLExecutor executor = new SQLExecutor(db);
//		String sqlid1 = "user.count";
//		String sqlid2 = "user.list";
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("operator", "admin");
//		R r = executor.pager(sqlid1, sqlid2, params, 1, 10);
//		System.out.println(r);
//		Map<String, Object> p = new HashMap<String, Object>();
//		p.put("name", "rzy");
//		p.put("age", 18);
//		List<String> kkk = new ArrayList<String>();
//		kkk.add("a");
//		kkk.add("b");
//		kkk.add("c");
//		p.put("kkk", kkk);
//		SqlPara sp = SQLLoader.getSql("b.queryData", p);
//		System.out.println(sp.toString());
		R p = new R();
		p.put("name", "rzy");
		p.put("age", 18);
		List<R> list = executor.find("b.queryData", p);
		for (R r : list)
		{
			System.out.println(r);
		}
	}
}
