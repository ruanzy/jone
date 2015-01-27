package org.rzy.web;

public class ServiceHandlerFactory
{
	public static ServiceHandler create()
	{
		return new LocalServiceHandler();
	}
}
