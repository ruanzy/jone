package org.rzy.web;

public class SessionUtil
{
	final static String USERKEY = "USER";
	final static String CAPTCHAKEY = "CAPTCHA";

	public static void setUser(String user)
	{
		attr(USERKEY, user);
	}

	public static String getUser()
	{
		Object user = attr(USERKEY);
		return (String) user;
	}

	public static void setCaptcha(String captcha)
	{
		attr(CAPTCHAKEY, captcha);
	}

	public static String getCaptcha()
	{
		Object captcha = attr(CAPTCHAKEY);
		return (String) captcha;
	}

	public static void clear()
	{
		WebContext.getSession().invalidate();
	}

	public static void attr(String key, Object value)
	{
		WebContext.getSession().setAttribute(key, value);
	}

	public static Object attr(String key)
	{
		return WebContext.getSession().getAttribute(key);
	}
}
