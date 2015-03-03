package com.rz.interceptor;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
}
