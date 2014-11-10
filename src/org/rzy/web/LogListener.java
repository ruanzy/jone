package org.rzy.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LogListener implements ServletContextListener
{

	public void contextDestroyed(ServletContextEvent arg0)
	{
		System.getProperties().remove("logDir");
	}

	public void contextInitialized(ServletContextEvent arg0)
	{
		String log4jdir = arg0.getServletContext().getRealPath("/") + "log";
		System.setProperty("logDir", log4jdir);
	}

}
