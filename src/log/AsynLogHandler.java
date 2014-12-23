package log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.rzy.dao.Dao;
import com.alibaba.fastjson.JSON;

public class AsynLogHandler implements LogHandler
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

	private static void write(String log)
	{
		String sql = "insert into log(id,operator,ip,time,method,result,memo) values(?,?,?,?,?,?,?)";
		Dao dao = Dao.getInstance();
		try
		{
			dao.begin();
			int id = dao.getID("log");
			String[] arr = log.split("\\|");
			Object[] params = new Object[] { id, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5] };
			dao.update(sql, params);
			dao.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			dao.rollback();
		}
	}

	public void handler(Log log)
	{
		StringBuffer logstr = new StringBuffer();
		String user = log.getUser();
		String ip = log.getIP();
		String time = log.getSid();
		String sid = log.getTime();
		String op = Util.getOP(sid);
		Object[] args = log.getArgs();
		String requestBody = JSON.toJSONString(args);
		logstr.append(user).append("|");
		logstr.append(ip).append("|");
		logstr.append(time).append("|");
		logstr.append(op).append("|");
		logstr.append(sid).append("|");
		logstr.append(1).append("|");
		logstr.append(requestBody);
		try
		{
			logs.put(logstr.toString());
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}