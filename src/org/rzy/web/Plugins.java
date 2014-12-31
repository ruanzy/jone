package org.rzy.web;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Plugins
{
	private static Logger log = LoggerFactory.getLogger(Plugins.class);
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

	public static void init()
	{
		if (plugins != null)
		{
			for (Plugin plugin : plugins)
			{
				plugin.init();
				log.debug("{} init...", plugin.getClass().getName());
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
				log.debug("{} destroy...", plugin.getClass().getName());
			}
		}
	}
}
