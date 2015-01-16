package org.rzy.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfigure implements Configure
{
	Properties prop;

	public PropertiesConfigure(String fileName)
	{
		InputStream is = null;
		prop = new Properties();
		try
		{
			is = PropertiesConfigure.class.getClassLoader().getResourceAsStream(fileName);
			prop.load(is);
		}
		catch (Exception e)
		{

		}
	}

	public String get(String key)
	{
		try
		{
			String value = "";
			if (prop.containsKey(key))
			{
				value = prop.getProperty(key);
				return value;
			}
			else
				return value;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return "";
		}
	}
}
