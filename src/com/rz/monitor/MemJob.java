package com.rz.monitor;

import java.util.Date;
import java.util.Random;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MemJob implements Job
{

	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		JobDataMap data = context.getJobDetail().getJobDataMap();
		String ip = String.valueOf(data.get("ip"));
		Random r = new Random();
		double v = r.nextDouble() * 100;
		long t = new Date().getTime();
		MonitorUtil.writeData(ip, "cpu", v, t);
	}

}
