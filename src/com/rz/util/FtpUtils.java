package com.rz.util;

import it.sauronsoftware.ftp4j.FTPClient;

public class FtpUtils
{

	public static FTPClient connect(String host, int port, String username, String password)
	{
		FTPClient client = new FTPClient();
		try
		{
			boolean compressionSupported = client.isCompressionSupported();
			client.setCompressionEnabled(compressionSupported);
			client.setCharset("UTF-8");
			client.setType(FTPClient.TYPE_BINARY);
			client.setPassive(true);
			client.connect(host, port);
			client.login(username, password);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return client;
	}

	public static void close(FTPClient client)
	{
		if (client != null)
		{
			try
			{
				client.logout();
			}
			catch (Exception e)
			{

			}
			finally
			{
				if (client.isConnected())
				{
					try
					{
						client.disconnect(true);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}