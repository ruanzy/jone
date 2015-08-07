package com.rz.schedule;

import it.sauronsoftware.cron4j.Scheduler;

public class Schedules2
{
	private final static Scheduler scheduler = new Scheduler();

	public static boolean start()
	{
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
}