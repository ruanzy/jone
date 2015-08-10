package com.rz.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rz.util.StringUtils;

public class TaskUtil
{
	private static Scheduler sched;

	public TaskUtil()
	{

	}

	public static boolean start()
	{
		try
		{
			sched = StdSchedulerFactory.getDefaultScheduler();
		}
		catch (SchedulerException e)
		{

		}
		String configName = "quartz-config.js";
		JSONArray jsonArray = loadJobs(configName);
		if (jsonArray == null)
		{
			return false;
		}

		for (int i = 0, size = jsonArray.size(); i < size; i++)
		{
			JSONObject json = jsonArray.getJSONObject(i);
			String className = json.getString("className");
			String cron = json.getString("cron");
			boolean enable = json.getBooleanValue("enable");
			if (enable == false || StringUtils.isBlank(className) || StringUtils.isBlank(cron))
			{
				continue;
			}

			Class<? extends Job> jobClass = getJobClass(className);

			JobDetail jobDetail = JobBuilder.newJob(jobClass).build();
			CronScheduleBuilder builder = null;
			try
			{
				builder = CronScheduleBuilder.cronSchedule(cron);
			}
			catch (ParseException e1)
			{
				e1.printStackTrace();
			}
			Trigger trigger = TriggerBuilder.newTrigger().startNow().withSchedule(builder).build();
			try
			{
				sched.scheduleJob(jobDetail, trigger);
			}
			catch (SchedulerException e)
			{

			}
		}

		try
		{
			sched.start();
		}
		catch (SchedulerException e)
		{

		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends Job> getJobClass(String className)
	{
		try
		{
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(className);
			if (Job.class.isAssignableFrom(jobClass) == false)
			{
				throw new IllegalArgumentException("Job class must implement the Job interface.");
			}
			return jobClass;
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException(className + " ClassNotFoundException!");
		}
	}

	private static JSONArray loadJobs(String configName)
	{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(configName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
				sb.append("\n");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		JSONArray jsonArray = JSONArray.parseArray(sb.toString());
		return jsonArray;
	}

	public static boolean stop()
	{
		try
		{
			sched.shutdown();
		}
		catch (SchedulerException e)
		{

		}
		return true;
	}
}
