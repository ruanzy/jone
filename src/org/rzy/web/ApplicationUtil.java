package org.rzy.web;

public class ApplicationUtil
{
	final static String USERRESKEY = "USERRES";

	public static String getWebRoot()
	{
		return WebContext.getServletContext().getRealPath("/");
	}

	public static void setUserres(String user)
	{
		attr(USERRESKEY, user);
	}

	public static String getUserres()
	{
		Object user = attr(USERRESKEY);
		return (String) user;
	}

	public static void attr(String key, Object value)
	{
		WebContext.getServletContext().setAttribute(key, value);
	}

	public static Object attr(String key)
	{
		return WebContext.getServletContext().getAttribute(key);
	}
}
