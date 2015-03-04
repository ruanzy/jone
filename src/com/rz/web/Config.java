package com.rz.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config
{
	private static Properties props = new Properties();

	static
	{
		InputStream is = null;
		try
		{
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jone.properties");
			if (is != null)
			{
				props.load(is);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
			}
			catch (IOException e)
			{

			}
		}
	}

	public static String get(String key)
	{
		return props.getProperty(key);
	}

	public static String get(String key, String defaultValue)
	{
		return props.getProperty(key, defaultValue);
	}

}
