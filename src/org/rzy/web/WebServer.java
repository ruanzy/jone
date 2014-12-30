package org.rzy.web;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class WebServer
{

	public static void main(String[] args) throws Exception
	{
		Server server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(8088);
		server.addConnector(connector);
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setResourceBase("./WebRoot");
		webapp.setDescriptor("./WebRoot/WEB-INF/web.xml");
		server.setHandler(webapp);
		server.start();
		server.join();
		System.out.println("srart at http://127.0.0.1:8088");
	}
}