package jone.quartz;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import jone.data.db.DB;

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

public class QuartzUtil
{
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
	
	private static void createTables(DB db)
	{
		try
		{
			boolean exist = db.existTable("QRTZ_JOB_DETAILS");
			if(!exist)
			{
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/jone/quartz/quartz.sql");
				db.runScript(new InputStreamReader(is, "UTF8"));
			}
		}
		catch (Exception e)
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
				scheduler.start();
			}
		}
		catch (SchedulerException e)
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
