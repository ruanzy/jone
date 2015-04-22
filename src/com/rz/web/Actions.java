package com.rz.web;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Actions
{
	private static Map<String, Object> actions = new HashMap<String, Object>();
	private static String pck = "action";

	static
	{
		try
		{
			URL url = Thread.currentThread().getContextClassLoader().getResource(pck);
			if (url != null)
			{
				File dir = new File(url.toURI());
				File[] files = dir.listFiles();
				for (File f : files)
				{
					String name = f.getName().substring(0, f.getName().indexOf(".class"));
					String className = pck + "." + name;
					Class<?> cls = Class.forName(className);
					actions.put(name, cls.newInstance());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void init()
	{

	}

	public static Object get(String name)
	{
		return actions.get(name);
	}
}
