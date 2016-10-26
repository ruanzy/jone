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
		String name = ip + "_" + ds;
		String group = "group";
		String cron = "0/" + step + " * * * * ?";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("ip", ip);
		dataMap.put("ds", ds);
		JobManager.addJob(name, group, cron, MonitorJob.class, dataMap);
	}

	public void pauseMonitor(String ip, String ds)
	{
		String name = ip + "_" + ds;
		String group = "group";
		JobManager.removeJob(name, group);
	}

	public double[] fetch(String ip, String ds, int size, int steps)
	{
		String rrd = ip + "_" + ds + ".rrd";
		return ru.fetch(rrd, ds, size, steps);
	}
}
