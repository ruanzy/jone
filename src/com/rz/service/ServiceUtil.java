package com.rz.service;

import java.io.InputStream;
import java.util.Properties;


public class ServiceUtil
{
	private static ServiceCaller caller;
	static Properties prop = new Properties();
	static
	{
		try
		{
			InputStream is = ServiceUtil.class.getClassLoader().getResourceAsStream("service.properties");
			prop.load(is);
			String _serviceCaller = prop.getProperty("ServiceCaller");
			Class<?> cls = Class.forName(_serviceCaller);
			caller = (ServiceCaller) (cls.newInstance());
		}
		catch (Exception e)
		{
			
		}
	}
	
	public static Object call(String sid, Object... args){
		return caller.call(sid, args);
	}
}
