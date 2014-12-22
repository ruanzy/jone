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
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class TaskManager
{
	private static Logger log = LoggerFactory.getLogger(TaskManager.class);
	private static ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);
	private static boolean started = false;
	private static List<Task> tasks = new ArrayList<Task>();

	static
	{
		log.debug("TaskManager satrting...  *");
		try
		{
			URL url = TaskManager.class.getClassLoader().getResource("task.xml");
			File f = new File(url.toURI());
			if (f.exists() && !f.isDirectory())
			{
				SAXReader reader = new SAXReader();
				reader.setEncoding("UTF-8");
				Document doc = reader.read(f);
				List<Element> list = doc.selectNodes("/tasks/task");
				for (Element e : list)
				{
					String className = e.elementTextTrim("class");
					String methodName = e.elementTextTrim("method");
					String cron = e.elementTextTrim("cron");
					String period = e.elementTextTrim("period");
					tasks.add(new Task(className, methodName, cron, period));
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
					String period = task.getPeriod();
					if (cron != null)
					{
						CronExpression cronExpression = new CronExpression(cron);
						schedule(className, methodName, cronExpression);
					}
					else if (period != null)
					{
						long periodL = Long.valueOf(period);
						schedule(className, methodName, periodL);
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

class Job implements Runnable
{

	private String className;

	private String methodName;

	public Job(String className, String methodName)
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

class Task
{
	private String className;
	private String methodName;
	private String cron;
	private String period;

	public Task(String className, String methodName, String cron, String period)
	{
		this.className = className;
		this.methodName = methodName;
		this.cron = cron;
		this.period = period;
	}

	public String getClassName()
	{
		return className;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public String getCron()
	{
		return cron;
	}

	public String getPeriod()
	{
		return period;
	}
}
