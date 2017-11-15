package jone.web;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import jone.util.Cfg;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public class JarLauncher
{
	public static void start()
	{
		int port = Cfg.getInt("server.port", 8080);
		String contextPath = Cfg.getString("server.context", "/");
		Server server = new Server(port);
		server.setStopAtShutdown(true);
		ProtectionDomain protectionDomain = JarLauncher.class.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		String warFile = location.toExternalForm();
		WebAppContext context = new WebAppContext(warFile, contextPath);
		try
		{
			context.setClassLoader(Thread.currentThread().getContextClassLoader());
			context.setServer(server);
			ServletHandler handler = new ServletHandler();
			FilterHolder cors = handler.addFilterWithMapping(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
			context.addFilter(cors, "/*", EnumSet.of(DispatcherType.REQUEST));
			FilterHolder jone = handler.addFilterWithMapping(JOne.class, "/*", EnumSet.of(DispatcherType.REQUEST));
			jone.setInitParameter("basepackage", Cfg.getString("basepackage"));
			context.addFilter(jone, "/*", EnumSet.of(DispatcherType.REQUEST));
	        String currentDir = new File(location.getPath()).getParent();
	        File workDir = new File(currentDir, "work");
	        context.setTempDirectory(workDir);
	        context.setExtraClasspath(currentDir + "/conf");
			server.setHandler(context);
			server.start();
			server.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
