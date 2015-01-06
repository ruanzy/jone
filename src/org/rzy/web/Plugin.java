package org.rzy.web;

import javax.servlet.ServletContext;


public interface Plugin
{
	public void init(ServletContext context);

	public void destroy();
}
