package com.rz.web;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;

public class Plugins
{
	private static List<Plugin> plugins;

	static
	{
		try
		{
			String pck = "plugin";
			URL url = Thread.currentThread().getContextClassLoader().getResource(pck);
			if (url != null)
			{
				File dir = new File(url.toURI());
				File[] files = dir.listFiles();
				if (files != null && files.length > 0)
				{
					plugins = new ArrayList<Plugin>();
					for (File f : files)
					{
						String name = f.getName().substring(0, f.getName().indexOf(".class"));
						String className = pck + "." + name;
						Class<?> cls = Class.forName(className);
						if (Plugin.class.isAssignableFrom(cls))
						{
							plugins.add((Plugin) cls.newInstance());
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void regist(String plugin)
	{
		try
		{
			Class<?> cls = Class.forName(plugin);
			if (Plugin.class.isAssignableFrom(cls))
			{
				plugins.add((Plugin) cls.newInstance());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void init(ServletContext context)
	{
		if (plugins != null)
		{
			for (Plugin plugin : plugins)
			{
				plugin.init(context);
				System.out.println(plugin.getClass().getName() + " init...");
			}
		}
	}

	public static void destroy()
	{
		if (plugins != null)
		{
			for (Plugin plugin : plugins)
			{
				plugin.destroy();
			}
		}
	}
}
