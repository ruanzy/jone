package com.rz.monitor;

import java.util.HashMap;
import java.util.Map;
import org.quartz.Job;

public class JobInfo
{
	private String name;
	private String group = "group";
	private Class<? extends Job> jobClass;
	private String cron;
	private Map<String, Object> dataMap = new HashMap<String, Object>();

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public Class<? extends Job> getJobClass()
	{
		return jobClass;
	}

	public void setJobClass(Class<? extends Job> jobClass)
	{
		this.jobClass = jobClass;
	}

	public String getCron()
	{
		return cron;
	}

	public void setCron(String cron)
	{
		this.cron = cron;
	}

	public Map<String, Object> getDataMap()
	{
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap)
	{
		this.dataMap = dataMap;
	}
}
