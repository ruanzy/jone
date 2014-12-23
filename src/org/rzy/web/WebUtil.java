package org.rzy.web;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtil
{
	static Logger log = LoggerFactory.getLogger(WebUtil.class);
	static String pck = "service";

	public static Object call(String sid, Object... args)
	{
		String className = substringBeforeLast(sid, ".");
		String methodName = substringAfterLast(sid, ".");
		String fullName = pck + "." + className;
		StringBuffer sb = new StringBuffer();
		sb.append("service=").append(sid).append("(");
		for (int i = 0, len = args.length; i < len; i++)
		{
			sb.append("arg" + i);
			if (i != len - 1)
			{
				sb.append(",");
			}
		}
		sb.append(")");
		// log.debug(sb.toString());
		for (int i = 0, len = args.length; i < len; i++)
		{
			// log.debug("arg" + i + "=" + JSON.toJSONString(args[i]));
		}
		Object result = null;
		StringBuffer logs = new StringBuffer();
		String user = SessionUtil.getUser();
		String ip = RequestUtil.getIP();
		// String op = "";//Util.getOP(sid);
		// String requestBody = JSON.toJSONString(args);
		logs.append(user).append("|");
		logs.append(ip).append("|");
		// logs.append(op).append("|");
		logs.append(sid).append("|");
		// logs.append(requestBody).append("|");
		logs.append("").append("|");
		try
		{
			Object proxy = ServiceProxy.get(fullName);
			result = MethodUtils.invokeMethod(proxy, methodName, args);
			logs.append(1);
		}
		catch (Exception e)
		{
			String error = "";
			if (e instanceof ClassNotFoundException)
			{
				error = e.getMessage() + " Not Found.";
			}
			else if (e instanceof NoSuchMethodException)
			{
				error = e.getMessage();
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				error = t.getMessage();
			}
			logs.append(error).append("|");
			logs.append(0);
			// e.printStackTrace();
			throw new RuntimeException("业务处理异常", e);
		}
		finally
		{
			log.debug(logs.toString());
		}
		return result;
	}

	private static String substringBeforeLast(String str, String separator)
	{
		if ((isEmpty(str)) || (isEmpty(separator)))
		{
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1)
		{
			return str;
		}
		return str.substring(0, pos);
	}

	private static String substringAfterLast(String str, String separator)
	{
		if (isEmpty(str))
		{
			return str;
		}
		if (isEmpty(separator))
		{
			return "";
		}
		int pos = str.lastIndexOf(separator);
		if ((pos == -1) || (pos == str.length() - separator.length()))
		{
			return "";
		}
		return str.substring(pos + separator.length());
	}

	private static boolean isEmpty(String str)
	{
		return (str == null) || (str.length() == 0);
	}
}
