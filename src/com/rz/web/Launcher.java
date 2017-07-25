package com.rz.web;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Launcher
{
	public static void main(String[] args) throws Exception
	{
		int port = Integer.parseInt(args[0]);
		Server server = new Server(port);
		ProtectionDomain domain = Launcher.class.getProtectionDomain();
		URL warLocation = domain.getCodeSource().getLocation();
		String classpath = new File(warLocation.getFile()).getParentFile().getAbsolutePath();
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setDescriptor(warLocation.toExternalForm() + "/WEB-INF/web.xml");
		webapp.setServer(server);
		webapp.setWar(warLocation.toExternalForm());
		webapp.setTempDirectory(new File("./"));
		webapp.setExtraClasspath(classpath);
		server.setHandler(webapp);
		server.start();
		System.out.println("server is started in port " + port);
	}
}
