package jone.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Launcher
{
	public static void start(int port, String context)
	{
		String WebRoot = "src/main/webapp";
		Server server = new Server(port);
		server.setStopAtShutdown(true);
		WebAppContext wc = new WebAppContext(WebRoot, context);
		wc.setDescriptor(WebRoot + "/WEB-INF/web.xml");
		wc.setResourceBase(WebRoot);
		wc.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
		wc.setConfigurationDiscovered(true);
		server.setHandler(wc);
		try
		{
			server.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
