package org.rzy.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;

public class CookieManager
{
	private static Map<String, Cookie> cookies = new HashMap<String, Cookie>();

	public static List<Cookie> getAll()
	{
		List<Cookie> list = null;
		if (cookies != null)
		{
			list = new ArrayList<Cookie>();
			for (Map.Entry<String, Cookie> entry : cookies.entrySet())
			{
				list.add(entry.getValue());
			}
		}
		return list;
	}

	public static void add(Cookie cookie)
	{
		cookies.put(cookie.getName(), cookie);
	}

	public static void remove(String cookieName)
	{
		cookies.remove(cookieName);
	}

	public static Cookie get(String cookieName)
	{
		return cookies.get(cookieName);
	}

	public static void clear()
	{
		cookies.clear();
	}
}
