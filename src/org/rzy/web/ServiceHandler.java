package org.rzy.web;


public interface ServiceHandler
{
	public Object handle(String sid, Object... args);
}
