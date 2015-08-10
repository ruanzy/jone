package com.rz.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzStudent implements Job
{

	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		System.out.println("student...");
	}

}
