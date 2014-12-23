package org.rzy.task;

public class Task
{
	private String className;
	private String methodName;
	private String cron;

	public Task(String className, String methodName, String cron)
	{
		this.className = className;
		this.methodName = methodName;
		this.cron = cron;
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
}