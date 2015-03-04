package com.rz.service;

import com.rz.web.Config;


public class ServiceUtil
{
	private static ServiceCaller caller;
	static
	{
		try
		{
			String _serviceCaller = Config.get("ServiceCaller");
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
