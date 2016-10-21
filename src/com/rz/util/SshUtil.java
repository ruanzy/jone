package com.rz.util;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshUtil
{
	static JSch jsch = new JSch();

	private static Session connect(String host, int port, String user, String password)
	{
		Session session = null;
		try
		{
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
		}
		catch (JSchException e)
		{
			e.printStackTrace();
		}
		return session;
	}

	public static String execute(String host, int port, String user, String password, String cmd)
	{
		Session session = connect(host, port, user, password);
		String result = null;
		Channel channel = null;
		try
		{
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(cmd);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			channel.connect();
			InputStream is = channel.getInputStream();
			result = IOUtils.toString(is);
			is.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (channel != null)
			{
				channel.disconnect();
			}
			session.disconnect();
		}
		return result;
	}

	public static void disk()
	{
		String host = "11.0.1.83";
		String user = "root";
		String password = "!qaz2wsx";
		int port = 22;
		String cmd = "df --total -k|awk /total/";
		String s = execute(host, port, user, password, cmd);
		System.out.println(s);
		Pattern p = Pattern.compile("total\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+%)");
		Matcher m = p.matcher(s);
		if (m.find())
		{
			long total = Long.parseLong(m.group(1));
			long used = Long.parseLong(m.group(2));
			long available = Long.parseLong(m.group(3));
			double usg = used * 1.0 / available;
			String r = String.format("total:%s, used:%s, available:%s, usg:%s", total, used, available, usg);
			System.out.println(r);
		}
	}

	public static void mem()
	{
		String host = "11.0.1.83";
		String user = "root";
		String password = "!qaz2wsx";
		int port = 22;
		String cmd = "free|awk /Mem/";
		String s = execute(host, port, user, password, cmd);
		System.out.println(s);
		Pattern p = Pattern.compile("Mem:\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
		Matcher m = p.matcher(s);
		if (m.find())
		{
			long total = Long.parseLong(m.group(1));
			long free = Long.parseLong(m.group(3));
			long buffers = Long.parseLong(m.group(5));
			long cached = Long.parseLong(m.group(6));
			double usg = Math.round((0.0d + total - free) / total * 10000) / 100d;
			String r = String.format("total:%s, free:%s, buffers:%s, cached:%s, usg:%s", total, free, buffers, cached,
					usg);
			System.out.println(r);
		}
	}

	public static void netio()
	{
		String host = "11.0.1.83";
		String user = "root";
		String password = "!qaz2wsx";
		int port = 22;
		String cmd = "cat /proc/net/dev|awk /eth0/";
		long startTime = System.currentTimeMillis();
		String s1 = execute(host, port, user, password, cmd);
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
		String s2 = execute(host, port, user, password, cmd);
		Matcher m2 = p.matcher(s2);
		long receive2 = 0, transmit2 = 0;
		if (m2.find())
		{
			receive2 = Long.parseLong(m2.group(1));
			transmit2 = Long.parseLong(m2.group(9));
		}
		double r = (receive2 - receive1) * 1.0 / ((endTime - startTime) / 1000) / 1024;
		double t = (transmit2 - transmit1) * 1.0 / ((endTime - startTime) / 1000) / 1024;
		System.out.println(r);
		System.out.println(t);
	}

	public static void diskio()
	{
		String host = "11.0.1.83";
		String user = "root";
		String password = "!qaz2wsx";
		int port = 22;
		String cmd = "cat /proc/diskstats|sed -n '/ram/!p'|sed -n '/loop/!p'|sed -n '1p'";
		long startTime = System.currentTimeMillis();
		String s1 = execute(host, port, user, password, cmd);
		Pattern p = Pattern
				.compile("\\s*\\d+\\s+\\d+\\s+\\w+\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
		Matcher m1 = p.matcher(s1);
		long read1 = 0, write1 = 0;
		while (m1.find())
		{
			read1 += Long.parseLong(m1.group(3));
			write1 += Long.parseLong(m1.group(7));
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
		String s2 = execute(host, port, user, password, cmd);
		Matcher m2 = p.matcher(s2);
		long read2 = 0, write2 = 0;
		while (m2.find())
		{
			read2 += Long.parseLong(m2.group(3));
			write2 += Long.parseLong(m2.group(7));
		}
		System.out.println(read2);
		System.out.println(write2);
		double r = Math.round((0.0d + (read2 - read1) * 512) / 1024 / ((endTime - startTime) / 1000));
		double t = Math.round((0.0d + (write2 - write1) * 512) / 1024 / ((endTime - startTime) / 1000));
		System.out.println(r);
		System.out.println(t);
	}

	public static void cpu()
	{
		String host = "11.0.1.83";
		String user = "root";
		String password = "!qaz2wsx";
		int port = 22;
		String cmd = "cat /proc/stat|awk /cpu[^0-9]/";
		String s1 = execute(host, port, user, password, cmd);
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
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		String s2 = execute(host, port, user, password, cmd);
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
		System.out.println(idle2);
		System.out.println(total2);
		double cpuusage = ((total2 - total1) - (idle2 - idle1)) * 100.0 / (total2 - total1);
		System.out.println(cpuusage);
	}

	public static void main(String[] args) throws Exception
	{
		// String host = "11.0.1.83";
		// String user = "root";
		// String password = "!qaz2wsx";
		// int port = 22;
		// String cmd = "cat /proc/stat|awk /cpu[^0-9]/";
		// String s = execute(host, port, user, password, cmd);
		// System.out.println(s);
		// disk();
		cpu();
	}
}
