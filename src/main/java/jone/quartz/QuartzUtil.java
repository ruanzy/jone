package jone.quartz;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jone.R;
import jone.data.db.DB;

import org.apache.commons.io.IOUtils;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
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
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzUtil
{
	static final Logger logger = LoggerFactory.getLogger(QuartzUtil.class);
	private static SchedulerFactory sf;
	
	public static void addJob(String name, String group, String cron, Class<? extends Job> jobClass,
			Map<String, Object> dataMap)
	{
		if (!CronExpression.isValidExpression(cron))
		{
			throw new RuntimeException("CronExpression is invalid.");
		}
		JobKey jobKey = JobKey.jobKey(name, group);
		try
		{
			Scheduler scheduler = sf.getScheduler();
			if (scheduler.checkExists(jobKey))
			{
				return;
			}
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
			if(dataMap != null){
				jobDetail.getJobDataMap().putAll(dataMap);
			}
			Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
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
					r.put("endtime", trigger.getEndTime());
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
	
	public static void start()
	{
		try
		{
			if (sf == null)
			{
				sf = new StdSchedulerFactory();
			}
			Scheduler scheduler = sf.getScheduler();
			if (!scheduler.isStarted())
			{
				logger.debug("Quartz scheduler is starting...");
				scheduler.start();
			}
		}
		catch (SchedulerException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void start(DB db)
	{
		try
		{
			createTables(db);
			if (sf == null)
			{
				Properties props = new Properties();
				String driver = db.getDriver();
				String url = db.getUrl();
				String user = db.getUsername();
				String password = db.getPassword();
				props.put("org.quartz.scheduler.instanceName", "DefaultQuartzScheduler");
				props.put("org.quartz.threadPool.class", org.quartz.simpl.SimpleThreadPool.class.getName());
				props.put("org.quartz.threadPool.threadCount", "25");
				props.put("org.quartz.threadPool.threadPriority", "5");
				props.put("org.quartz.jobStore.misfireThreshold", "60000");
				props.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
				props.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
				props.put("org.quartz.jobStore.dataSource", "DS");
				props.put("org.quartz.dataSource.DS.driver", driver);
				props.put("org.quartz.dataSource.DS.URL", url);
				props.put("org.quartz.dataSource.DS.user", user);
				props.put("org.quartz.dataSource.DS.password", password);
				props.put("org.quartz.dataSource.DS.maxConnections", 30);
				sf = new StdSchedulerFactory(props);
			}
			Scheduler scheduler = sf.getScheduler();
			if (!scheduler.isStarted())
			{
				logger.debug("Quartz scheduler is starting...");
				scheduler.start();
			}
		}
		catch (SchedulerException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void createTables(DB db)
	{
		try
		{
			boolean exist = db.existTable("QRTZ_JOB_DETAILS");
			logger.debug("Quartz dbtables already exist");
			if(!exist)
			{
				InputStream is = QuartzUtil.class.getResourceAsStream("/jone/quartz/quartz.sql");
				db.runScript(new InputStreamReader(is, "UTF8"));
				logger.debug("Creating  quartz dbtables...");
				System.out.println(IOUtils.toString(is));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void shutdown() {  
        try {  
            Scheduler sched = sf.getScheduler();  
            if (!sched.isShutdown()) {  
                sched.shutdown();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
}
