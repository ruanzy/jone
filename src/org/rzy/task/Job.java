package org.rzy.task;

import java.lang.reflect.Method;

public class Job implements Runnable
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