package org.rzy.web;


public interface ServiceCaller
{
	public Object call(String sid, Object... args);
}
