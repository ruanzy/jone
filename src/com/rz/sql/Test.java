package com.rz.sql;

import com.alibaba.fastjson.JSON;

public class Test
{
	public static void main(String[] args)
	{
		String sql = "select * from users order by username desc";
		PageHelper.startPage(1, 3);
		Page p = PageHelper.pager(sql);
		System.out.println(JSON.toJSON(p));
	}
}
