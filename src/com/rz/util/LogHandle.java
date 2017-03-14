package com.rz.util;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import com.rz.dao.DB;

public class LogHandle
{
	private static LinkedBlockingQueue<OpLog> queue = new LinkedBlockingQueue<OpLog>(5000);
	private DB db;

	private static class SingletonHolder
	{
		private final static LogHandle INSTANCE = new LogHandle();
	}

	public static LogHandle getInstance(DB db)
	{
		SingletonHolder.INSTANCE.db = db;
		return SingletonHolder.INSTANCE;
	}

	public LogHandle()
	{
		new Thread(new Runnable()
		{
			LinkedList<OpLog> logs = new LinkedList<OpLog>();

			public void run()
			{
				while (true)
				{
					try
					{
						queue.drainTo(logs, 500);
						write(logs);
					}
					catch (Exception e)
					{

					}
				}
			}

		}).start();
	}

	private void write(LinkedList<OpLog> logs)
	{
		try
		{
			int rows = 0;
			if (logs.size() > 0)
			{
				db.begin();
				db.beginBatch("insert into logs(operator, ip, time, operation, memo) values(?,?,?,?,?)");
				for (OpLog log : logs)
				{
					try
					{
						String operator = log.getOperator();
						String ip = log.getIp();
						String time = log.getTime();
						String operation = log.getOperation();
						String memo = log.getMemo();
						db.addBatch(new Object[] { operator, ip, time, operation, memo });
						rows++;
						if (0 == rows % 200)
						{
							db.excuteBatch();
							db.commit();
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				try
				{
					db.excuteBatch();
					db.commit();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				db.endBatch();
				db.close();
				logs.clear();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void put(OpLog log)
	{
		try
		{
			queue.put(log);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
