package com.rz.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorManger
{
	private static Logger logger = LoggerFactory.getLogger(MonitorManger.class);
	private static Map<String, Monitor> types = new HashMap<String, Monitor>();
	private RrdUtil ru = RrdUtil.getInstance();

	private static class SingletonHolder
	{
		private final static MonitorManger INSTANCE = new MonitorManger();
	}

	public static MonitorManger getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private MonitorManger()
	{

	}

	public void setRrdDir(String rrdDir)
	{
		ru.setRrdDir(rrdDir);
	}

	public void ds(String ds, Monitor monitor)
	{
		types.put(ds, monitor);
	}
	
	public Monitor getMonitor(String ds)
	{
		return types.get(ds);
	}

	public void addMonitor(String ip, String ds, List<Archive> archives)
	{
		String rrdDir = ru.getRrdDir();
		if (null == rrdDir)
		{
			logger.debug("Please set rrdDir!");
			return;
		}
		int step = ru.getStep();
		String rrd = ip + "_" + ds + ".rrd";
		ru.createRrd(rrd, ds, archives);
		JobInfo job = new JobInfo();
		job.setName(ip + "_" + ds);
		job.setGroup("group");
		job.setCron("0/" + step + " * * * * ?");
		job.setJobClass(MonitorJob.class);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("ip", ip);
		dataMap.put("ds", ds);
		job.setDataMap(dataMap);
		JobManager.addJob(job);
	}

	public void pauseMonitor(String ip, String ds)
	{
		JobInfo job = new JobInfo();
		job.setName(ip + "_" + ds);
		job.setGroup("group");
		JobManager.removeJob(job);
	}

	public double[] fetch(String ip, String ds, int size, int steps)
	{
		String rrd = ip + "_" + ds + ".rrd";
		return ru.fetch(rrd, ds, size, steps);
	}
}
