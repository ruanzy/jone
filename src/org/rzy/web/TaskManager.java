package org.rzy.web;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
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

	private static boolean state = false;

	@SuppressWarnings("unchecked")
	public static void start()
	{
		if (!state)
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
						long period = Long.valueOf(task.elementTextTrim("period"));
						scheduled.scheduleAtFixedRate(new Task(className, methodName), 0L, period, TimeUnit.SECONDS);
					}
				}
				state = false;
			}
			catch (Exception e)
			{
				log.debug("TaskManager Satrt Exception!");
				e.printStackTrace();
			}
		}
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
