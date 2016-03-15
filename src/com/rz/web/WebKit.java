package com.rz.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class WebKit
{
	public static String getLang(HttpServletRequest request)
	{
		String lang = "zh";
		Cookie[] cks = request.getCookies();
		if (cks != null)
		{
			for (Cookie cookie : cks)
			{
				if ("lang".equals(cookie.getName()))
				{
					lang = cookie.getValue();
					break;
				}
			}
		}
		return lang;
	}
	
	public static String capitalize(String str)
	{
		if ((str == null) || (str.length() == 0))
		{
			return str;
		}
		return Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}
}
