package com.rz.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.sql.Pager;

public class Test
{
	public static void main(String[] args)
	{
		String sqlid = "user.selectAll";
		Map<String, String> params = new HashMap<String, String>();
		params.put("operator", "admin");
		params.put("page", "1");
		params.put("pagesize", "10");
		Pager p = SQLMapper.pager(sqlid, params);
		System.out.println(p.getTotal());
		List<Record> list = p.getData();
		for (Record r : list)
		{
			System.out.println(r.get("username"));
		}
		
//        String map = "function() { emit(this.name, {count:1});}";
//        String reduce = "function(key, values) {";  
//        reduce=reduce+"var total = 0;";  
//        reduce=reduce+"for(var i=0;i<values.length;i++){total += values[i].count;}";  
//        reduce=reduce+"return {count:total};}";         
	}
}
