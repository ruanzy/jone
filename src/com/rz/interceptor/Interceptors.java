package com.rz.interceptor;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Interceptors
{
	private static Map<String, Interceptor> interceptors = new HashMap<String, Interceptor>();
	private static String pck = "interceptor";

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

	public static void init()
	{

	}

	public static Interceptor[] match(String url)
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
