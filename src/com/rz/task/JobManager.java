package com.rz.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.common.R;
import com.rz.dao.DB;

public class JobManager
{
	private static final Logger logger = LoggerFactory.getLogger(JobManager.class);
	private static SchedulerFactory sf;

	public static void addJob(String name, String group, String cron, Class<? extends Job> jobClass,
			Map<String, Object> dataMap, boolean start)
	{
		JobKey jobKey = JobKey.jobKey(name, group);
		try
		{
			Scheduler scheduler = sf.getScheduler();
			if (scheduler.checkExists(jobKey))
			{
				logger.debug("Job {} already exists.", jobKey);
				return;
			}
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
			for (Map.Entry<String, Object> entry : dataMap.entrySet())
			{
				jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
			}
			if (!checkCronExpression(cron))
			{
				throw new RuntimeException("cron expression parser exception.");
			}
			Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
			if (start)
			{
				scheduler.scheduleJob(jobDetail, trigger);
			}
			else
			{
				scheduler.scheduleJob(jobDetail, trigger);
				scheduler.pauseJob(jobDetail.getKey());
			}
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

	public static void addJob(String name, String group, String cron, Class<? extends Job> jobClass,
			Map<String, Object> dataMap, JobListener lsn, boolean start)
	{
		JobKey jobKey = JobKey.jobKey(name, group);
		try
		{
			Scheduler scheduler = sf.getScheduler();
			if (scheduler.checkExists(jobKey))
			{
				logger.debug("Job {} already exists.", jobKey);
				return;
			}
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
			for (Map.Entry<String, Object> entry : dataMap.entrySet())
			{
				jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
			}
			if (!checkCronExpression(cron))
			{
				throw new RuntimeException("cron expression parser exception.");
			}
			Matcher<JobKey> matcher = KeyMatcher.keyEquals(new JobKey(name, group));
			scheduler.getListenerManager().addJobListener(lsn, matcher);
			Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
			if (start)
			{
				scheduler.scheduleJob(jobDetail, trigger);
			}
			else
			{
				scheduler.scheduleJob(jobDetail, trigger);
				scheduler.pauseJob(jobDetail.getKey());
			}
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

	public static void pauseJob(String name, String group)
	{
		JobKey jobKey = JobKey.jobKey(name, group);
		try
		{
			Scheduler scheduler = sf.getScheduler();
			if (scheduler.checkExists(jobKey))
			{
				scheduler.pauseJob(jobKey);
			}
			else
			{
				logger.debug("Job {}  does't exist.", jobKey);
			}
		}
		catch (Exception e)
		{
			logger.error("pauseJob job {} fail.", jobKey);
			throw new RuntimeException(e.getMessage());
		}
	}

	public static void resumeJob(String name, String group)
	{
		JobKey jobKey = JobKey.jobKey(name, group);
		try
		{
			Scheduler scheduler = sf.getScheduler();
			if (scheduler.checkExists(jobKey))
			{
				scheduler.resumeJob(jobKey);
			}
			else
			{
				logger.debug("Job {}  does't exist.", jobKey);
			}
		}
		catch (Exception e)
		{
			logger.error("pauseJob job {} fail.", jobKey);
			throw new RuntimeException(e.getMessage());
		}
	}

	public static void removeJob(String name, String group)
	{
		JobKey jobKey = JobKey.jobKey(name, group);
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

	public static List<R> getAllJob()
	{
		try
		{
			Scheduler scheduler = sf.getScheduler();
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			List<R> jobList = new ArrayList<R>();
			for (JobKey jobKey : jobKeys)
			{
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				JobDetail jd = scheduler.getJobDetail(jobKey);
				for (Trigger trigger : triggers)
				{
					R r = new R();
					r.put("name", jobKey.getName());
					r.put("group", jobKey.getGroup());
					r.put("jobclass", jd.getJobClass());
					for (Map.Entry<String, Object> entry : jd.getJobDataMap().entrySet())
					{
						r.put(entry.getKey(), entry.getValue());
					}
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					r.put("status", triggerState.name());
					r.put("starttime", trigger.getStartTime());
					if (trigger instanceof CronTrigger)
					{
						CronTrigger cronTrigger = (CronTrigger) trigger;
						String cronExpression = cronTrigger.getCronExpression();
						r.put("cron", cronExpression);
					}
					jobList.add(r);
				}
			}
			return jobList;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static boolean checkCronExpression(String cron)
	{
		return CronExpression.isValidExpression(cron);
	}

	public static void init(DB db)
	{
		InputStream sqlis = null;
		try
		{
			String sqlfile = "quartz.sql";
			sqlis = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/rz/task/" + sqlfile);
			db.runScript(new InputStreamReader(sqlis, "UTF8"));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				if (sqlis != null)
				{
					sqlis.close();
				}
			}
			catch (IOException e)
			{

			}
		}
	}

	public static void start(DB db)
	{
		InputStream cfgis = null;
		try
		{
			Properties p = new Properties();
			String cfgfile = "quartz.properties";
			cfgis = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/rz/task/" + cfgfile);
			p.load(cfgis);
			p.put("org.quartz.dataSource.DS.driver", db.getDriver());
			p.put("org.quartz.dataSource.DS.URL", db.getUrl());
			p.put("org.quartz.dataSource.DS.user", db.getUsername());
			p.put("org.quartz.dataSource.DS.password", db.getPassword());
			sf = new StdSchedulerFactory(p);
			Scheduler scheduler = sf.getScheduler();
			if (!scheduler.isShutdown())
			{
				scheduler.start();
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				if (cfgis != null)
				{
					cfgis.close();
				}
			}
			catch (IOException e)
			{

			}
		}
	}

	public static void start()
	{
		try
		{
			sf = new StdSchedulerFactory();
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
