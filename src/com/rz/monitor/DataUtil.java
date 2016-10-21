package com.rz.monitor;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.rz.util.SshUtil;

public class DataUtil
{
	public static double disk(String host, String user, String password, int port)
	{
		String cmd = "df --total -k|awk /total/";
		String s = SshUtil.execute(host, port, user, password, cmd);
		Pattern p = Pattern.compile("total\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+%)");
		Matcher m = p.matcher(s);
		double diskusg = 0;
		if (m.find())
		{
			long total = Long.parseLong(m.group(1));
			long available = Long.parseLong(m.group(3));
			diskusg = Math.round((0.0d + total - available) / total * 10000) / 100d;
		}
		return diskusg;
	}

	public static double mem(String host, String user, String password, int port)
	{
		String cmd = "free|awk /Mem/";
		String s = SshUtil.execute(host, port, user, password, cmd);
		Pattern p = Pattern.compile("Mem:\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
		Matcher m = p.matcher(s);
		double memusg = 0;
		if (m.find())
		{
			long total = Long.parseLong(m.group(1));
			long free = Long.parseLong(m.group(3));
			memusg = Math.round((0.0d + total - free) / total * 10000) / 100d;
		}
		return memusg;
	}

	public static double[] netio(String host, String user, String password, int port)
	{
		double[] ret = new double[2];
		String cmd = "cat /proc/net/dev|awk /eth0/";
		long startTime = System.currentTimeMillis();
		String s1 = SshUtil.execute(host, port, user, password, cmd);
		Pattern p = Pattern
				.compile("eth0:\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
		Matcher m1 = p.matcher(s1);
		long receive1 = 0, transmit1 = 0;
		if (m1.find())
		{
			receive1 = Long.parseLong(m1.group(1));
			transmit1 = Long.parseLong(m1.group(9));
		}
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		String s2 = SshUtil.execute(host, port, user, password, cmd);
		Matcher m2 = p.matcher(s2);
		long receive2 = 0, transmit2 = 0;
		if (m2.find())
		{
			receive2 = Long.parseLong(m2.group(1));
			transmit2 = Long.parseLong(m2.group(9));
		}
		long receive = receive2 - receive1;
		long transmit = transmit2 - transmit1;
		long time = (endTime - startTime) / 1000;
		BigDecimal bgr = new BigDecimal((0.0d + receive) / 1024 / time);
		BigDecimal bgt = new BigDecimal((0.0d + transmit) / 1024 / time);
		double r = bgr.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double t = bgt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		ret[0] = r;
		ret[1] = t;
		return ret;
	}

	public static double[] diskio(String host, String user, String password, int port)
	{
		double[] ret = new double[2];
		String cmd = "cat /proc/diskstats|sed -n '/ram/!p'|sed -n '/loop/!p'|sed -n '1p'";
		long startTime = System.currentTimeMillis();
		String s1 = SshUtil.execute(host, port, user, password, cmd);
		Pattern p = Pattern
				.compile("\\s*\\d+\\s+\\d+\\s+\\w+\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
		Matcher m1 = p.matcher(s1);
		long read1 = 0, write1 = 0;
		if (m1.find())
		{
			read1 = Long.parseLong(m1.group(3));
			write1 = Long.parseLong(m1.group(7));
		}
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		String s2 = SshUtil.execute(host, port, user, password, cmd);
		Matcher m2 = p.matcher(s2);
		long read2 = 0, write2 = 0;
		if (m2.find())
		{
			read2 = Long.parseLong(m2.group(3));
			write2 = Long.parseLong(m2.group(7));
		}
		long read = read2 - read1;
		long write = write2 - write1;
		long time = (endTime - startTime) / 1000;
		BigDecimal bgr = new BigDecimal((0.0d + read * 512) / 1024 / time);
		BigDecimal bgt = new BigDecimal((0.0d + write * 512) / 1024 / time);
		double r = bgr.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double t = bgt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		ret[0] = r;
		ret[1] = t;
		return ret;
	}

	public static double cpu(String host, String user, String password, int port)
	{
		String cmd = "cat /proc/stat|awk /cpu[^0-9]/";
		String s1 = SshUtil.execute(host, port, user, password, cmd);
		Pattern p = Pattern
				.compile("cpu\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
		Matcher m1 = p.matcher(s1);
		long idle1 = 0, total1 = 0;
		if (m1.find())
		{
			long us = Long.parseLong(m1.group(1));
			long nice = Long.parseLong(m1.group(2));
			long system = Long.parseLong(m1.group(3));
			idle1 = Long.parseLong(m1.group(4));
			long iowait = Long.parseLong(m1.group(5));
			long irq = Long.parseLong(m1.group(6));
			long softirq = Long.parseLong(m1.group(7));
			long stealstolen = Long.parseLong(m1.group(8));
			long guest = Long.parseLong(m1.group(9));
			total1 = us + nice + system + idle1 + iowait + irq + softirq + stealstolen + guest;
		}
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		String s2 = SshUtil.execute(host, port, user, password, cmd);
		Matcher m2 = p.matcher(s2);
		long idle2 = 0, total2 = 0;
		if (m2.find())
		{
			long us = Long.parseLong(m2.group(1));
			long nice = Long.parseLong(m2.group(2));
			long system = Long.parseLong(m2.group(3));
			idle2 = Long.parseLong(m2.group(4));
			long iowait = Long.parseLong(m2.group(5));
			long irq = Long.parseLong(m2.group(6));
			long softirq = Long.parseLong(m2.group(7));
			long stealstolen = Long.parseLong(m2.group(8));
			long guest = Long.parseLong(m2.group(9));
			total2 = us + nice + system + idle2 + iowait + irq + softirq + stealstolen + guest;
		}
		long total = total2 - total1;
		long idle = idle2 - idle1;
		double cpuusage = Math.round((0.0d + total - idle) / total * 10000) / 100d;
		return cpuusage;
	}
}
