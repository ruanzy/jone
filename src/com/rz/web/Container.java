package com.rz.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Container
{
	private static Logger log = LoggerFactory.getLogger(Container.class);
	private static Properties props = new Properties();
	private static Map<String, Object> actions = new HashMap<String, Object>();
	private static Map<String, Interceptor> interceptors = new HashMap<String, Interceptor>();

	public static void init()
	{
		properties();
		actions();
		interceptors();
	}

	private static void properties()
	{
		InputStream is = null;
		try
		{
			String fileName = "jone.properties";
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (is != null)
			{
				log.debug("Loading jone.properties");
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

	private static void actions()
	{
		String pck = props.getProperty("action.package", "action");
		log.debug("Loading actions in " + pck + " package");
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

	private static void interceptors()
	{
		String pck = props.getProperty("interceptor.package", "interceptor");
		log.debug("Loading interceptors in " + pck + " package");
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
					Expression expression = cls.getAnnotation(Expression.class);
					if (expression != null)
					{
						String exp = expression.value();
						interceptors.put(exp, (Interceptor) cls.newInstance());
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Object findAction(String actionName)
	{
		return actions.get(actionName);
	}
	
	public static List<Interceptor> findInterceptor(String action, String method)
	{
		List<Interceptor> list = new ArrayList<Interceptor>();
		String express1 = action + ".*";
		String express2 = action + "." + method;
		Set<String> keys = interceptors.keySet();
		for (String key : keys)
		{
			if (key.equals("*") || key.equals(express1) || key.equals(express2))
			{
				list.add(interceptors.get(key));
			}
		}
		return list;
	}
}
