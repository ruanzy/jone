package com.rz.service;

import com.rz.util.WebUtil;

public class ServiceFactory
{
	public static Service create()
	{
		Service service = new LocalService();
		service.setUser(WebUtil.getUser());
		return service;
	}
}
