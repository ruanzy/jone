package com.rz.sql;

import java.util.Date;
import com.alibaba.fastjson.JSON;
import com.rz.schedule.Schedules2;

public class Test
{
	public static void main(String[] args)
	{
		String sql = "select * from users order by username desc";
		PageHelper.startPage(1, 3);
		Pager p = PageHelper.pager(sql);
		System.out.println(JSON.toJSON(p));

		Schedules2.addTask("* * * * *", new Runnable()
		{
			public void run()
			{
				System.out.println(new Date());
				System.out.println("A minute ticked away...");
			}
		});
		Schedules2.addTask("*/2 * * * *", new Runnable()
		{
			public void run()
			{
				System.out.println(new Date());
				System.out.println("Another minute ticked away...");
			}
		});
		Schedules2.start();
//		try
//		{
//			Thread.sleep(1000L * 60L * 5);
//		}
//		catch (InterruptedException e)
//		{
//			;
//		}
//		s.stop();
	}
}
