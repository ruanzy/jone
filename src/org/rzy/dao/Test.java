package org.rzy.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test
{
	public static void main(String[] args)
	{
		String sqlid = "log.selectAll";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("operator", "admin");
		params.put("operator_text", "admin");
		params.put("time1", "2014-08-28");
		params.put("time2", "2014-08-29");
		List<Map<String, Object>> list = SQLMapper.find(sqlid, params);
		System.out.println(list.size());
	}
}
