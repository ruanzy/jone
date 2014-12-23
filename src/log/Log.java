package log;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.rzy.web.RequestUtil;
import org.rzy.web.SessionUtil;

public class Log
{
	String user;
	String ip;
	String time;
	String sid;
	boolean result;
	Object[] args;

	public Log(String sid, Object[] args)
	{
		this.user = SessionUtil.getUser();
		this.ip = RequestUtil.getIP();
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
	
	public boolean getResult()
	{
		return result;
	}
	
	public void setResult(boolean result)
	{
		this.result = result;
	}
}
