package com.rz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.io.IOUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Config
{
	private static Map<String, Object> cfg;
	static
	{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("cfg.json");
		try
		{
			cfg = JSON.parseObject(IOUtils.toString(is, "UTF8"), new TypeReference<Map<String, Object>>()
			{
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static Object get(String expression)
	{
		try
		{
			return Ognl.getValue(expression, cfg);
		}
		catch (OgnlException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String getString(String expression)
	{
		try
		{
			Object obj = Ognl.getValue(expression, cfg);
			if (null != obj)
			{
				return obj.toString();
			}
		}
		catch (OgnlException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Integer getInt(String expression)
	{
		try
		{
			Object obj = Ognl.getValue(expression, cfg);
			if (null != obj)
			{
				return Integer.valueOf(obj.toString());
			}
		}
		catch (OgnlException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}