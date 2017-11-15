package jone.web;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Banner
{
	public static void print()
	{
		ClassLoader classLoader = JOne.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("/jone/logo.txt");
		try
		{
			String logo = IOUtils.toString(is);
			String version = JOne.class.getPackage().getImplementationVersion();
			logo = logo.replaceFirst("\\$\\{version\\}", version);
			System.out.println("\n" + logo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}
}
