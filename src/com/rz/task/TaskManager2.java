package com.rz.task;

import java.io.File;
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

@SuppressWarnings("unchecked")
public class TaskManager2
{
	private static Logger log = LoggerFactory.getLogger(TaskManager2.class);
	private static ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);

	static
	{
		log.debug("TaskManager Satrting...  ");
		try
		{
			URL url = Thread.currentThread().getContextClassLoader().getResource("task.xml");
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
					CronExpression cronExpression = new CronExpression(cron);
					schedule(className, methodName, cronExpression);
				}
			}
		}
		catch (Exception e)
		{
			log.debug("TaskManager Satrt Exception!");
			e.printStackTrace();
		}
	}

	public static void schedule(final String className, final String methodName, final CronExpression expression)
	{
		final Task job = new Task(className, methodName);
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