package rzy.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogHandler
{
	static int size = 100;
	static int poolSize = 3;
	private static ArrayBlockingQueue<String> logs = new ArrayBlockingQueue<String>(size * 10);
	private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(poolSize);

	static
	{
		for (int i = 0; i < size; i++)
		{
			pool.scheduleAtFixedRate(new Runnable()
			{
				public void run()
				{
					try
					{
						String log = logs.take();
						write(log);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}, 0, 5, TimeUnit.MILLISECONDS);
		}
	}

	public static void put(String log)
	{
		try
		{
			logs.put(log);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private static void write(String log)
	{
		String sql = "insert into log(id,operator,ip,time,method,result,memo) values(?,?,?,?,?,?,?)";
		try
		{
			Dao.begin();
			int id = Dao.getID("log");
			String[] arr = log.split("\\|");
			Object[] params = new Object[] { id, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5] };
			Dao.update(sql, params);
			Dao.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Dao.rollback();
		}
	}
}