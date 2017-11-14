package jone.web;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class JarLauncher
{
	public static void main(String[] args)
	{
		int port = 8088;
		String contextPath = "/";
		Server server = new Server(port);
		server.setStopAtShutdown(true);
		ProtectionDomain protectionDomain = JarLauncher.class.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		String warFile = location.toExternalForm();
		WebAppContext context = new WebAppContext(warFile, contextPath);
		context.setServer(server);
		String currentDir = new File(location.getPath()).getParent();
		File workDir = new File(currentDir, "work");
		context.setTempDirectory(workDir);
		context.setExtraClasspath(currentDir + "/conf");
		server.setHandler(context);
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
