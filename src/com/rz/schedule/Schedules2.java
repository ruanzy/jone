package com.rz.schedule;

import it.sauronsoftware.cron4j.Scheduler;

public class Schedules2
{
	private final static Scheduler scheduler = new Scheduler();

	public static boolean start()
	{
		scheduler.setDaemon(true);
		scheduler.start();
		return true;
	}

	public static boolean stop()
	{
		scheduler.stop();
		return true;
	}

	public static void addTask(String cronExpress, Runnable task)
	{
		scheduler.schedule(cronExpress, task);
	}
	
	private Runnable createTask(String jobName, String taskClassName) {
		if (taskClassName == null) {
			throw new RuntimeException("Please set " + jobName + ".className");
		}

		Object temp = null;
		try {
			temp = Class.forName(taskClassName).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Can not create instance of class: " + taskClassName, e);
		}

		Runnable task = null;
		if (temp instanceof Runnable) {
			task = (Runnable) temp;
		} else {
			throw new RuntimeException("Can not create instance of class: " + taskClassName
					+ ". this class must implements Runnable interface");
		}
		return task;
	}
}