package jone.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Cfg
{
	private static Map<String, Object> cfg;
	static
	{
		ClassLoader classLoader = Cfg.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("cfg.json");
		try
		{
			String jsonStr = "";
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer, 0, buffer.length)) != -1)
			{
				out.write(buffer, 0, len);
			}
			jsonStr = new String(out.toByteArray());
			cfg = JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {});
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
	
	public static String getString(String expression, String defaultVal)
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
		return defaultVal;
	}

	public static Integer getInt(String expression, int defaultVal)
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
		return defaultVal;
	}
}