package jone.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Launcher
{
	public static void start(int port)
	{
		String WebRoot = "src/main/webapp";
		Server server = new Server(port);
		server.setStopAtShutdown(true);
		WebAppContext wc = new WebAppContext(WebRoot, "");
		wc.setDescriptor(WebRoot + "/WEB-INF/web.xml");
		wc.setResourceBase(WebRoot);
		wc.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
		wc.setClassLoader(Thread.currentThread().getContextClassLoader());
		wc.setConfigurationDiscovered(true);
		wc.setParentLoaderPriority(true);
		server.setHandler(wc);
		try
		{
			server.start();
			server.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				server.stop();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
