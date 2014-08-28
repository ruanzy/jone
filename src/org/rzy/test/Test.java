package org.rzy.test;

import java.util.HashMap;
import java.util.Map;
import org.rzy.dao.Dao;
import org.rzy.util.Pager;

public class Test
{
	public static void main(String[] args)
	{
		String sqlid1 = "user.count";
		String sqlid2 = "user.selectAll";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", "a");
		params.put("state", 1);
		Dao d = Dao.getInstance();
		Pager all = d.dynamicPager(sqlid1, sqlid2, params, 2, 2);
		System.out.println(all.getTotal());
		for (Map<String, Object> map : all.getData())
		{
			System.out.println(map.get("username"));
		}
	}
}
