package com.rz.util;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import com.rz.dao.DB;
import com.rz.dao.DBs;

public class LogHandle
{
	static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(5000);
	static DB db = DBs.getDB("dbone");;
	static
	{
		new Thread(new Runnable()
		{
			LinkedList<String> logs = new LinkedList<String>();

			public void run()
			{
				while (true)
				{
					try
					{
						String log = queue.poll();
						if (log == null)
						{
							if (logs.size() > 0)
							{
								write(logs);
							}
						}
						else
						{
							logs.add(log);
						}

					}
					catch (Exception e)
					{

					}
				}
			}

		}).start();
	}

	private static void write(LinkedList<String> logs)
	{
		try
		{
			int rows = 0;
			db.begin();
			db.beginBatch("insert into logs(operator, time, operation, memo) values(?,?,?,?)");
			for (String log : logs)
			{
				String[] arr = log.split("\\|");
				db.addBatch(new Object[] { arr[0], arr[1], arr[2], arr[3] });
				rows++;
				if (0 == rows % 200)
				{
					db.excuteBatch();
				}
			}
			db.excuteBatch();
			db.endBatch();
			db.commit();
			logs.clear();
		}
		catch (Exception e)
		{
			db.rollback();
			e.printStackTrace();
		}
	}

	public static void put(String log)
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
