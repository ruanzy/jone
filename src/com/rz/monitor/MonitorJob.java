package com.rz.monitor;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MonitorJob implements Job
{

	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		JobDataMap data = context.getJobDetail().getJobDataMap();
		String ip = String.valueOf(data.get("ip"));
		String ds = String.valueOf(data.get("ds"));
		double v = MonitorManger.getInstance().getMonitor(ds).getData(ip);
		System.out.println(v);
		String rrd = ip + "_" + ds + ".rrd";
		RrdUtil.getInstance().writeData(rrd, "cpu", v);
	}
}
