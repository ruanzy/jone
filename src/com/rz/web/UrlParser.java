package com.rz.web;

public class UrlParser
{
	public static String getActionName(String url)
	{
		String[] parts = url.split("/");
		String str = parts[0];
		return Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}

	public static String getActionMethod(String url)
	{
		String[] parts = url.split("/");
		return (parts.length > 1) ? parts[1] : "execute";
	}
}
