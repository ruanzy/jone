package org.rzy.web;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil
{
	public static void setCharacterEncoding(String encoding)
	{
		try
		{
			Context.getRequest().setCharacterEncoding(encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isAjax()
	{
		String xhr = Context.getRequest().getHeader("x-requested-with");
		if (xhr != null && xhr.trim().length() > 0)
		{
			return true;
		}
		return false;
	}

	public static boolean isAdmin(String username, String password)
	{
		return ("admin").equals(username) && ("162534").equals(password);
	}

	public static Map<String, String> getParameters()
	{
		Map<String, String> ps = null;
		Enumeration<?> em = Context.getRequest().getParameterNames();
		if (em.hasMoreElements())
		{
			ps = new HashMap<String, String>();
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				String v = getParameter(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	public static String getParameter(String name)
	{
		return Context.getRequest().getParameter(name);
	}

	public static String getUserAgent()
	{
		return getHeader("User-Agent");
	}

	public static String getIP()
	{
		return Context.getRequest().getRemoteAddr();
	}

	public static String getHeader(String key)
	{
		return Context.getRequest().getHeader(key);
	}

	public static void attr(String key, Object value)
	{
		Context.getRequest().setAttribute(key, value);
	}

	public static Object attr(String key)
	{
		return Context.getRequest().getAttribute(key);
	}
}
