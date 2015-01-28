package com.rz.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskUtil
{
	private static ExecutorService es = Executors.newCachedThreadPool();
	private static CompletionService<String> cs = new ExecutorCompletionService<String>(es);
	private static Map<String, Integer> map = new LinkedHashMap<String, Integer>();
	static Logger log = LoggerFactory.getLogger(TaskUtil.class);

	public static Set<String> getAllTaskids()
	{
		return map.keySet();
	}

	public static List<String> getSuccessTaskids()
	{
		List<String> ok = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : map.entrySet())
		{
			String k = entry.getKey();
			Integer v = entry.getValue();
			if (v == 1)
			{
				ok.add(k);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("成功的任务：[");
		for (int i = 0; i < ok.size(); i++)
		{
			sb.append(ok.get(i));
			if (i != ok.size() - 1)
			{
				sb.append(",");
			}
		}
		sb.append("]");
		log.debug(sb.toString());
		return ok;
	}

	public static List<String> getFailureTaskids()
	{
		List<String> fail = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : map.entrySet())
		{
			String k = entry.getKey();
			Integer v = entry.getValue();
			if (v != 1)
			{
				fail.add(k);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("失败的任务：[");
		for (int i = 0; i < fail.size(); i++)
		{
			sb.append(fail.get(i));
			if (i != fail.size() - 1)
			{
				sb.append(",");
			}
		}
		sb.append("]");
		log.debug(sb.toString());
		return fail;
	}

	public static void add(List<BaseTask> tasks)
	{
		for (BaseTask task : tasks)
		{
			cs.submit(task);
			map.put(task.id, 0);
		}
		int len = map.size();
		for (int i = 0; i < len; i++)
		{
			try
			{
				Future<String> future = cs.take();
				map.put(future.get(), 1);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				log.debug(e.getCause().getMessage());
			}
		}
	}
}

abstract class BaseTask implements Callable<String>
{
	String id = UUID.randomUUID().toString();

	public abstract String call();
}