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
		int port = 8080;
		if(args.length > 0){
			try
			{
				port = Integer.valueOf(args[0].toString());
			}
			catch (Exception e)
			{
				throw new RuntimeException("The server port must be an integer of 0-65535");
			}
		}
		Server server = new Server(port);
		server.setStopAtShutdown(true);
		ProtectionDomain protectionDomain = JarLauncher.class.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		String warFile = location.toExternalForm();
		WebAppContext context = new WebAppContext(warFile, "");
		try
		{
			context.setServer(server);
	        String currentDir = new File(location.getPath()).getParent();
	        File workDir = new File(currentDir, "work");
	        context.setTempDirectory(workDir);
	        context.setExtraClasspath(currentDir + "/conf");
			server.setHandler(context);
			server.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
