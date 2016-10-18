package com.rz.monitor;

import java.util.Map;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobManager
{
	private static final Logger logger = LoggerFactory.getLogger(JobManager.class);
	private static SchedulerFactory sf = new StdSchedulerFactory();

	public static void addJob(JobInfo job)
	{
		String name = job.getName();
		String group = job.getGroup();
		String cron = job.getCron();
		Class<? extends Job> jobClass = job.getJobClass();
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
		for (Map.Entry<String, Object> entry : job.getDataMap().entrySet())
		{
			jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
		}
		if (!checkCronExpression(job.getCron()))
		{
			throw new RuntimeException("cron expression parser exception.");
		}
		try
		{
			Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
			Scheduler scheduler = sf.getScheduler();
			scheduler.scheduleJob(jobDetail, trigger);
			if (!scheduler.isShutdown())
			{
				scheduler.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void removeJob(JobInfo job)
	{
		JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
		try
		{
			Scheduler scheduler = sf.getScheduler();
			if (scheduler.checkExists(jobKey))
			{
				scheduler.deleteJob(jobKey);
			}
			else
			{
				logger.debug("Job {}  does't exist.", jobKey);
			}
		}
		catch (Exception e)
		{
			logger.error("removeJob job {} fail.", jobKey);
			throw new RuntimeException(e.getMessage());
		}
	}

	public static boolean checkCronExpression(String cron)
	{
		return CronExpression.isValidExpression(cron);
	}

	public static void start()
	{
		try
		{
			Scheduler scheduler = sf.getScheduler();
			if (!scheduler.isShutdown())
			{
				scheduler.start();
			}
		}
		catch (SchedulerException e)
		{
			e.printStackTrace();
		}
	}

	public static void shutdown()
	{
		try
		{
			Scheduler scheduler = sf.getScheduler();
			scheduler.shutdown(true);
		}
		catch (SchedulerException e)
		{
			e.printStackTrace();
		}
	}
}
