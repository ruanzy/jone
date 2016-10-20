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

	public static void main(String[] args) throws Exception
	{
		// String host = "11.0.1.83";
		// String user = "root";
		// String password = "!qaz2wsx";
		// int port = 22;
		// String cmd = "cat /proc/stat|awk /cpu[^0-9]/";
		// String s = execute(host, port, user, password, cmd);
		// System.out.println(s);
		disk();
	}
}
