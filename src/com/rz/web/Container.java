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
import com.rz.web.interceptor.Interceptor;
import com.rz.web.interceptor.InterceptorPath;

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
		log.debug("Loading jone.properties");
		InputStream is = null;
		try
		{
			String fileName = "jone.properties";
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
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
					InterceptorPath interceptorpath = cls.getAnnotation(InterceptorPath.class);
					if (interceptorpath != null)
					{
						String path = interceptorpath.value();
						interceptors.put(path, (Interceptor) cls.newInstance());
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Object getAction(String actionName)
	{
		return actions.get(actionName);
	}
	
	public static Interceptor[] getInterceptor(String url)
	{
		List<Interceptor> list = new ArrayList<Interceptor>();
		Set<String> keys = interceptors.keySet();
		for (String key : keys)
		{
			if (url.startsWith("/" + key))
			{
				list.add(interceptors.get(key));
			}
		}
		return list.toArray(new Interceptor[list.size()]);
	}
}
