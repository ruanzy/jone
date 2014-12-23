package org.rzy.task;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.rzy.web.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager
{
	private static Logger log = LoggerFactory.getLogger(TaskManager.class);
	private static ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);
	private static boolean started = false;
	private static List<Task> tasks = new ArrayList<Task>();

	static
	{
		log.debug("TaskManager Satrting...  ");
		try
		{
			String pck = "task";
			URL url = Thread.currentThread().getContextClassLoader().getResource(pck);
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
						String cron = scheduled.cron();
						tasks.add(new Task(className, methodName, cron));
					}
				}
			}
		}
		catch (Exception e)
		{
			log.debug("TaskManager Satrt Exception!");
			e.printStackTrace();
		}
	}

	public static void start()
	{
		if (!started)
		{
			try
			{

				for (Task task : tasks)
				{
					String className = task.getClassName();
					String methodName = task.getMethodName();
					String cron = task.getCron();
					if (cron != null)
					{
						CronExpression cronExpression = new CronExpression(cron);
						schedule(className, methodName, cronExpression);
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
		Job job = new Job(className, methodName);
		scheduled.scheduleAtFixedRate(job, 0L, period, TimeUnit.SECONDS);
	}

	public static void schedule(final String className, final String methodName, final CronExpression expression)
	{
		final Job job = new Job(className, methodName);
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
						scheduled.schedule(job, time.getTime() - now.getTime(), TimeUnit.MILLISECONDS);
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