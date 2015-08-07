package com.rz.sql;

import it.sauronsoftware.cron4j.Scheduler;
import com.alibaba.fastjson.JSON;

public class Test
{
	public static void main(String[] args)
	{
		String sql = "select * from users order by username desc";
		PageHelper.startPage(1, 3);
		Pager p = PageHelper.pager(sql);
		System.out.println(JSON.toJSON(p));

		Scheduler s = new Scheduler();
		s.schedule("* * * * *", new Runnable()
		{
			public void run()
			{
				System.out.println("Another minute ticked away...");
			}
		});
		s.start();
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
