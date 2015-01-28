package org.rzy.service;


public interface Service
{
	public Object call(String sid, Object... args);
	
	public void setUser(String user);
}

