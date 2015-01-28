package org.rzy.service;

public class ServiceFactory
{
	public static Service create()
	{
		return new LocalService();
	}
}
