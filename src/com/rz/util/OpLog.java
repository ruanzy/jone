package com.rz.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.rz.web.WebUtil;

public class OpLog
{
	private String operator;
	private String ip;
	private String time;
	private String operation;
	private String memo;
	
	public OpLog(String operation, String memo)
	{
		this.operator = WebUtil.getUser();
		this.ip = WebUtil.getIP();
		this.operation = operation;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String t = df.format(new Date());
		this.time = t;
		this.memo = memo;
	}
	
	public OpLog(String operator, String operation, String memo)
	{
		this.operator = operator;
		this.ip = WebUtil.getIP();
		this.operation = operation;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String t = df.format(new Date());
		this.time = t;
		this.memo = memo;
	}

	public String getOperator()
	{
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = WebUtil.getIP();
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String t = df.format(new Date());
		this.time = t;
	}

	public String getOperation()
	{
		return operation;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

}
