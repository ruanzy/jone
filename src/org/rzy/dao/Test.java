package org.rzy.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test
{
	public static void main(String[] args)
	{
		String countsqlid = "log.count";
		String pagersqlid = "log.selectAll";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("operator", "admin");
		params.put("operator_text", "admin");
		params.put("time1", "2014-05-28");
		params.put("time2", "2014-08-29");
		params.put("page", 1);
		params.put("pagesize", 10);
		Pager p = SQLMapper.pager(countsqlid, pagersqlid, params);
		System.out.println(p.getTotal());
		List<Map<String, Object>> list = p.getData();
		for (Map<String, Object> map : list)
		{
			System.out.println(map.get("method"));
		}
	}
}
