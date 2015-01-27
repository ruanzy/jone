package org.rzy.web;

public class ServiceCallerFactory
{
	public static ServiceCaller create()
	{
		return new LocalServiceCaller();
	}
}
