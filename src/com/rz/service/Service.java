package com.rz.service;


public interface Service
{
	public Object call(String sid, Object... args);
	
	public void setUser(String user);
}

