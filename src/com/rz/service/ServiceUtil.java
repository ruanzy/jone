package com.rz.service;



public class ServiceUtil
{
	private static ServiceCaller caller;
	static
	{
		try
		{
			String serviceCaller = "com.rz.service.DefaultServiceCaller";
//			String _serviceCaller = Config.get("ServiceCaller");
//			if(_serviceCaller != null && _serviceCaller.length() > 0){
//				serviceCaller = _serviceCaller;
//			}
			Class<?> cls = Class.forName(serviceCaller);
			caller = (ServiceCaller) (cls.newInstance());
		}
		catch (Exception e)
		{
			throw new RuntimeException("Get ServiceCaller Exception!");
		}
	}
	
	public static Object call(String sid, Object... args){
		return caller.call(sid, args);
	}
}
