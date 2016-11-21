package com.rz.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JOneServer
{
	static final Logger logger = LoggerFactory.getLogger(JOneServer.class);

	public static void start(String webRoot, int port) throws Exception
	{
		Server server = new Server(port);
		WebAppContext webAppContext = new WebAppContext(webRoot, "");
		webAppContext.setDescriptor(webRoot + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(webRoot);
		webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		webAppContext.setConfigurationDiscovered(true);
		webAppContext.setParentLoaderPriority(true);
		server.setHandler(webAppContext);
		server.start();
		logger.info("JOneServer is started in port {}", port);
	}
}
