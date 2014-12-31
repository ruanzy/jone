package org.rzy.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JOneListener implements ServletContextListener
{

	public void contextDestroyed(ServletContextEvent arg0)
	{
		Plugins.destroy();
	}

	public void contextInitialized(ServletContextEvent arg0)
	{
		Plugins.init();
	}
}
