package org.rzy.test;

import java.util.HashMap;
import java.util.Map;
import org.rzy.dao.SQLMapper;
import org.rzy.util.MD5Util;
import org.rzy.util.Pager;
import org.rzy.util.TimeUtil;

public class Test
{
	public static void main(String[] args)
	{
		String sqlid1 = "user.count";
		String sqlid2 = "user.selectAll";
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("username", "t");
		params.put("state", 1);
		params.put("page", 2);
		params.put("pagesize", 2);
		Pager all = SQLMapper.pager(sqlid1, sqlid2, params);
		System.out.println(all.getTotal());
		for (Map<String, Object> map : all.getData())
		{
			System.out.println(map.get("username"));
		}
		
		
		Map<String, Object> p2 = new HashMap<String, Object>();
		p2.put("id",10);
		p2.put("username","ruanzy");
		String pwd = MD5Util.md5("ruanzy" + "123456");
		p2.put("pwd",pwd);
		String regtime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		p2.put("regtime",regtime);
		SQLMapper.update("user.add", p2);
	}
}
