package com.rz.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.util.ClassUtil;

public class Container
{
	private static Logger log = LoggerFactory.getLogger(Container.class);
	private static String BASEPACKAGE;
	private static Map<String, Object> actions = new HashMap<String, Object>();
	private static Map<String, Interceptor> interceptors = new HashMap<String, Interceptor>();
	private static List<Plugin> plugins = new ArrayList<Plugin>();

	public static void init(String basePackage)
	{
		BASEPACKAGE = basePackage;
		loadActions();
		loadInterceptors();
		initPlugins();
	}
	
	private static String getPackage(String subpackage)
	{
		if (BASEPACKAGE == null || BASEPACKAGE.length() == 0)
		{
			return subpackage;
		}
		return BASEPACKAGE + "." + subpackage;
	}

	private static void loadActions()
	{
		String pck = getPackage("action");
		try
		{
			Set<Class<?>> _actions = ClassUtil.scan(pck);
			if (_actions.size() > 0)
			{
				log.debug("Loading actions in " + pck);
				for (Class<?> cls : _actions)
				{
					String name = cls.getName();
					actions.put(name, cls.newInstance());
					log.debug("Loading action " + name);
				}
				log.debug("Actions loaded!");
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Actions load error!", e);
		}
	}

	private static void loadInterceptors()
	{
		String pck = getPackage("interceptor");
		try
		{
			Set<Class<?>> _interceptors = ClassUtil.scan(pck);
			if (_interceptors.size() > 0)
			{
				log.debug("Loading interceptors in " + pck + " package");
				for (Class<?> cls : _interceptors)
				{
					String name = cls.getName();
					Expression expression = cls.getAnnotation(Expression.class);
					if (expression != null)
					{
						String exp = expression.value();
						interceptors.put(exp, (Interceptor) cls.newInstance());
					}
					log.debug("Loading interceptor " + name);
				}
				log.debug("Interceptors loaded!");
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Interceptors load error!", e);
		}
	}

	private static void initPlugins()
	{
		String pck = getPackage("plugin");
		try
		{
			Set<Class<?>> _plugins = ClassUtil.scan(pck);
			if (_plugins.size() > 0)
			{
				log.debug("Loading plugins in " + pck);
				for (Class<?> cls : _plugins)
				{
					plugins.add((Plugin) cls.newInstance());
				}
				for (Plugin plugin : plugins)
				{
					String name = plugin.getClass().getName();
					log.debug("initing plugin " + name);
					plugin.start();
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Plugins load error!", e);
		}
	}

	public static Object findAction(String actionName)
	{
		String pck = getPackage("action");
		return actions.get(pck + "." + actionName);
	}

	public static List<Interceptor> findInterceptor(String action, String method)
	{
		List<Interceptor> list = new ArrayList<Interceptor>();
		String express1 = action;
		String express2 = action + "/" + method;
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

	public static List<Plugin> findPlugins()
	{
		return plugins;
	}
}
