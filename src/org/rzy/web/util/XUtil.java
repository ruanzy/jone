package org.rzy.web.util;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.MethodUtils;
import org.rzy.log.LogHandler;
import org.rzy.util.StringUtils;
import org.rzy.web.Context;
import org.rzy.web.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class XUtil
{
	static Logger log = LoggerFactory.getLogger(XUtil.class);

	public static Object call(String sid, Object... args)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(sid).append("(");
		for (int i = 0, len = args.length; i < len; i++)
		{
			sb.append("Parameter" + i);
			if(i != len - 1){
				sb.append(",");
			}
		}
		sb.append(")");
		log.debug(sb.toString());
		for (int i = 0, len = args.length; i < len; i++)
		{
			log.debug("Parameter" + i + "==>" + args[i]);
		}
		Object result = null;
		String className = StringUtils.substringBeforeLast(sid, ".");
		String methodName = StringUtils.substringAfterLast(sid, ".");
		try
		{
			Class<?> cls = Class.forName("service." + className);
			Object serviceProxy = ServiceProxy.create(cls);
			result = MethodUtils.invokeMethod(serviceProxy, methodName, args);
			boolean flag = Pattern.compile("^(add|del|mod|set|reg|active|cancel)").matcher(methodName).find();
			if (flag)
			{
				StringBuffer logs = new StringBuffer();
				String username = getUsername();
				String requestBody = JSON.toJSONString(args);
				String ip = Context.getIP();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = df.format(new Date());
				logs.append(username).append("|");
				logs.append(ip).append("|");
				logs.append(time).append("|");
				logs.append(sid).append("|");
				logs.append(1).append("|");
				logs.append(requestBody);
				LogHandler.put(logs.toString());
			}
		}
		catch (Exception e)
		{
			if (e instanceof ClassNotFoundException)
			{
				log.debug(e.getMessage() + " Not Found.");
			}
			else if (e instanceof NoSuchMethodException)
			{
				log.debug(e.getMessage());
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				log.debug(t.getMessage());
			}
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}

	public static Object getUser()
	{
		if (Context.getSession() != null)
		{
			return Context.getSession().getAttribute("user");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static String getUsername()
	{
		Object obj = getUser();
		return obj == null ? null : String.valueOf(((Map<String, String>) obj).get("username"));
	}

	@SuppressWarnings("unchecked")
	public static String getUserid()
	{
		Object obj = getUser();
		return obj == null ? null : String.valueOf(((Map<String, String>) obj).get("id"));
	}

	public static List<Map<String, Object>> toList(String str)
	{
		List<Map<String, Object>> data = null;
		try
		{
			data = JSON.parseObject(str, new TypeReference<List<Map<String, Object>>>()
			{
			});
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return data;
	}

	public static void ok()
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("msg", null);
		try
		{
			HttpServletResponse response = Context.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JSON.toJSONString(result));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void ok(String msg, Object... args)
	{
		// msg = Resource.get(msg)!=null?Resource.get(msg):msg;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("msg", msg);
		try
		{
			HttpServletResponse response = Context.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JSON.toJSONString(result));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void error(String msg, Object... args)
	{
		msg = I18N.get(msg) != null ? I18N.get(msg) : msg;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		result.put("msg", msg);
		try
		{
			HttpServletResponse response = Context.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JSON.toJSONString(result));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void attr(String key, Object value, String scope)
	{
		if ("application".equals(scope))
		{
			Context.getServletContext().setAttribute(key, value);
		}
		else if ("session".equals(scope))
		{
			Context.getSession().setAttribute(key, value);
		}
		else
		{
			Context.getRequest().setAttribute(key, value);
		}
	}

	public static Object attr(String key, String scope)
	{
		if ("application".equals(scope))
		{
			return Context.getServletContext().getAttribute(key);
		}
		else if ("session".equals(scope))
		{
			return Context.getSession().getAttribute(key);
		}
		else
		{
			return Context.getRequest().getAttribute(key);
		}
	}

	public static void main(String[] args)
	{
		String text = I18N.get("20000");
		System.out.println(text);
	}
}