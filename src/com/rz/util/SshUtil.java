package com.rz.util;

import java.io.InputStream;
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
	
	
	public static void main(String[] args)
	{
		String host = "11.0.1.83";
		String user = "root";
		String password = "!qaz2wsx";
		int port = 22;
		String cmd = "cat /proc/stat|awk /cpu[^0-9]/";
		String s = execute(host, port, user, password, cmd);
		System.out.println(s);
	}
}
