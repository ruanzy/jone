package jone.web;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import jone.util.Cfg;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;
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
		ServletHandler handler = new ServletHandler();
		FilterHolder fh = handler.addFilterWithMapping(JOne.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        fh.setInitParameter("basepackage", Cfg.getString("basepackage"));
        wc.addFilter(fh, "/*", EnumSet.of(DispatcherType.REQUEST));
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
	}
}
