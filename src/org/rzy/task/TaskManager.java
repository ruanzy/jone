package org.rzy.task;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager
{
	private static Logger log = LoggerFactory.getLogger(TaskManager.class);

	private static ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);

	private static boolean started = false;

	@SuppressWarnings("unchecked")
	public static void start()
	{
		if (!started)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("*************************************").append("\r\n");
			sb.append("*          TaskManager satrting...  *").append("\r\n");
			sb.append("*************************************").append("\r\n");
			System.out.println(sb);
			try
			{
				URL url = TaskManager.class.getClassLoader().getResource("task.xml");
				File f = new File(url.toURI());
				if (f.exists() && !f.isDirectory())
				{
					SAXReader reader = new SAXReader();
					reader.setEncoding("UTF-8");
					Document doc = reader.read(f);
					List<Element> tasks = doc.selectNodes("/tasks/task");
					for (Element task : tasks)
					{
						String className = task.elementTextTrim("class");
						String methodName = task.elementTextTrim("method");
						String cron = task.elementTextTrim("cron");
						String period = task.elementTextTrim("period");
						if (cron != null)
						{
							CronExpression cronExpression = new CronExpression(cron);
							schedule(className, methodName, cronExpression);
						}
						else if (period != null)
						{
							long periodL = Long.valueOf(task.elementTextTrim("period"));
							schedule(className, methodName, periodL);
						}
					}
				}
				started = false;
			}
			catch (Exception e)
			{
				log.debug("TaskManager Satrt Exception!");
				e.printStackTrace();
			}
		}
	}

	public static void schedule(final String className, final String methodName, long period)
	{
		Task task = new Task(className, methodName);
		scheduled.scheduleAtFixedRate(task, 0L, period, TimeUnit.SECONDS);
	}

	public static void schedule(final String className, final String methodName, final CronExpression expression)
	{
		final Task task = new Task(className, methodName);
		Runnable scheduleTask = new Runnable()
		{
			public void run()
			{
				Date now = new Date();
				Date time = expression.getNextValidTimeAfter(now);
				try
				{
					while (time != null)
					{
						scheduled.schedule(task, time.getTime() - now.getTime(), TimeUnit.MILLISECONDS);
						while (now.before(time))
						{
							Thread.sleep(time.getTime() - now.getTime());
							now = new Date();
						}
						time = expression.getNextValidTimeAfter(now);
					}
				}
				catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
				catch (Exception e)
				{

				}
			}
		};
		scheduled.execute(scheduleTask);
	}

	public static void stop()
	{
		scheduled.shutdownNow();
		scheduled = null;
	}
}

class Task implements Runnable
{

	private String className;

	private String methodName;

	public Task(String className, String methodName)
	{
		this.className = className;
		this.methodName = methodName;
	}

	public void run()
	{
		try
		{
			Class<?> cls = Class.forName(className);
			Method m = cls.getMethod(methodName);
			m.invoke(cls.newInstance());

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
