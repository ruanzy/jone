package com.rz.web;

import java.util.Properties;

public class Env
{
	public static void set(String key, String value)
	{
		System.setProperty(key, value);
	}

	public static void remove(String key)
	{
		System.getProperties().remove(key);
	}

	public static String get(String key)
	{
		return System.getProperty(key);
	}

	public static Properties getAll(String key)
	{
		return System.getProperties();
	}
}
