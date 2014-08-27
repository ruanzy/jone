package rzy.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rzy.core.Dao;

public class Test
{
	public static void main(String[] args)
	{
		String sqlid = "count";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", "a");
		params.put("state", 1);
		String sql = DynamicQuery.getSQL(sqlid, params);
		System.out.println(sql);
		Dao d = Dao.getInstance();
		Object count = d.scalar(sql);
		System.out.println(count);
		
		String sql2 = DynamicQuery.getSQL("selectAll", params);
		List<Map<String, Object>> all = d.find(sql2);
		for (Map<String, Object> map : all)
		{
			System.out.println(map.get("username"));
		}
	}
}
