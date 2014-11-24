package org.rzy.web;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log
{
	String user;
	String ip;
	String time;
	String sid;
	Object[] args;

	public Log(String sid, Object[] args)
	{
		this.user = WebUtil.getUser();
		this.ip = WebUtil.getIP();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.time = df.format(new Date());
		this.sid = sid;
		this.args = args;
	}

	public String getUser()
	{
		return user;
	}

	public String getIP()
	{
		return ip;
	}

	public String getTime()
	{
		return time;
	}

	public String getSid()
	{
		return sid;
	}

	public Object[] getArgs()
	{
		return args;
	}
}
