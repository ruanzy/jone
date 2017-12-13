package jone.data.db;

import java.util.HashMap;
import java.util.Map;

import jone.data.db.sql.SQLLoader;
import jone.data.db.sql.SqlPara;

public class Test
{
	public static void main(String[] args)
	{
		//DB db = DBs.getDB("h2");
//		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("rzy.sql");
//		db.runScript(new InputStreamReader(is));
//		boolean f = db.existTable("USERS");
//		System.out.println(f);
		//SQLExecutor executor = new SQLExecutor(db);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "admin");
		SqlPara sql1 = SQLLoader.getSql("user.selectAll", params);
		System.out.println(sql1.getSql());
		SqlPara sql2 = SQLLoader.getSql("user.delete", params);
		System.out.println(sql2.getSql());
		SqlPara sql3 = SQLLoader.getSql("role.selectAll", params);
		System.out.println(sql3.getSql());
		Object[] _params1 = sql1.getPara();
		for (Object p : _params1)
		{
			
			System.out.println(p);
		}
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
//		R p = new R();
//		p.put("name", "rzy");
//		p.put("age", 18);
//		List<R> list = executor.find("b.queryData", p);
//		for (R r : list)
//		{
//			System.out.println(r);
//		}
	}
}
