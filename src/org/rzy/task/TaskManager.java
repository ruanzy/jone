package org.rzy.task;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.rzy.web.Scheduled;

public class TaskManager
{
	private static ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);

	static
	{
		try
		{
			String pck = "task";
			URL url = Thread.currentThread().getContextClassLoader().getResource(pck);
			if (url == null)
			{
				throw new RuntimeException("task package not found!");
			}
			File dir = new File(url.toURI());
			File[] files = dir.listFiles();
			for (File f : files)
			{
				String name = f.getName().substring(0, f.getName().indexOf(".class"));
				String className = pck + "." + name;
				Class<?> cls = Class.forName(className);
				for (Method m : cls.getDeclaredMethods())
				{
					Scheduled scheduled = m.getAnnotation(Scheduled.class);
					if (scheduled != null)
					{
						String methodName = m.getName();
						String cron = scheduled.value();
						CronExpression cronExpression = new CronExpression(cron);
						schedule(className, methodName, cronExpression);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void start()
	{

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