package jone.web;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Banner
{
	public static void print()
	{
		ClassLoader classLoader = Banner.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("/jone/logo.txt");
		try
		{
			String logo = IOUtils.toString(is);
			logo = logo.replaceFirst("\\$\\{version\\}", WebUtil.getVersion());
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
