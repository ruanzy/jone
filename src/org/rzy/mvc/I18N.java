package org.rzy.mvc;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.io.IOUtils;

public class I18N
{

	private final static MyResourceBundleControl ctl = new MyResourceBundleControl();

	public static String get(String key, Object... args)
	{
		try
		{
			ResourceBundle rb = ResourceBundle.getBundle("resource", Locale.getDefault(), I18N.class.getClassLoader(), ctl);
			String text = (rb != null) ? rb.getString(key) : null;
			return (text != null) ? MessageFormat.format(text, args) : null;
		}
		catch (MissingResourceException e)
		{
			return null;
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	public static String loadFromResource(String resource)
	{
		InputStream in = null;
		try
		{
			in = new FileInputStream(resource);
			return IOUtils.toString(in, "utf-8");
		}
		catch (Exception excp)
		{
			throw new RuntimeException(excp);
		}
		finally
		{
			IOUtils.closeQuietly(in);
		}
	}

	private static class MyResourceBundleControl extends ResourceBundle.Control
	{
		@Override
		public long getTimeToLive(String baseName, Locale locale)
		{
			return 10000;
		}

		@Override
		public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime)
		{
			return true;
		}
	}

	public static void main(String[] args)
	{
		String text = get("20000");
		System.out.println(text);
	}
}