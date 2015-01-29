package com.rz.service;


public class ServiceFactory
{
	public static Service create()
	{
		return new LocalService();
	}
}
