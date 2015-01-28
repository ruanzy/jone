package com.rz.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServerInfo
{
	public static Map<String, Object> base()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			Properties props = System.getProperties();
			InetAddress addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress();
			String os = System.getProperty("os.name");
			int processors = Runtime.getRuntime().availableProcessors();
			int kb = 1024;
			DecimalFormat d_f = new DecimalFormat("0.00");
			long m = Runtime.getRuntime().maxMemory();
			String max = d_f.format(m * 1.0 / kb / kb / kb);
			map.put("ip", ip);
			map.put("os", os);
			map.put("version", props.getProperty("java.version"));
			map.put("processors", processors);
			map.put("max", max);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> mem()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		int kb = 1024;
		DecimalFormat d_f = new DecimalFormat("0.00");
		long t = Runtime.getRuntime().totalMemory();
		long f = Runtime.getRuntime().freeMemory();
		String total = d_f.format(t * 1.0 / kb / kb);
		String use = d_f.format((t - f) * 1.0 / kb / kb);
		String free = d_f.format(f * 1.0 / kb / kb);
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
		while (group != null)
		{
			topGroup = group;
			group = group.getParent();
		}
		int totalThread = topGroup.activeCount();
		map.put("total", total);
		map.put("free", free);
		map.put("use", use);
		map.put("totalThread", totalThread);
		return map;
	}

	public static void main(String[] args)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		double d = 0.9078;
		String db = df.format(d);
		System.out.println(db);
	}
}
