package org.rzy.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.MethodUtils;
import org.rzy.web.result.Ftl;
import org.rzy.web.result.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class WebUtil
{

	static Logger log = LoggerFactory.getLogger(WebUtil.class);

	public static ServletContext getServletContext()
	{
		return Context.getServletContext();
	}

	public static HttpSession getSession()
	{
		return Context.getSession();
	}

	public static HttpServletRequest getRequest()
	{
		return Context.getRequest();
	}

	public static HttpServletResponse getResponse()
	{
		return Context.getResponse();
	}

	public static boolean isAjax()
	{
		return Context.isAjax();
	}

	public static Map<String, String> getParameters()
	{
		return Context.getParameters();
	}

	public static String getParameter(String name)
	{
		return getRequest().getParameter(name);
	}

	public static void redirect(String url)
	{
		try
		{
			HttpServletRequest request = getRequest();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/";
			getResponse().sendRedirect(basePath + url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void forward(String url)
	{
		RequestDispatcher rd = getRequest().getRequestDispatcher(url);
		try
		{
			rd.forward(getRequest(), getResponse());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getUser()
	{
		return (Map<String, String>) getSession().getAttribute("user");
	}

	public static void setUser(Object user)
	{
		getSession().setAttribute("user", user);
	}

	public static String getIP()
	{
		return getRequest().getRemoteAddr();
	}

	public static void clearSession()
	{
		getSession().invalidate();
	}

	public static String getVC()
	{
		return (String) getSession().getAttribute("vc");
	}

	public static Object call(String sid, Object... args)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(sid).append("(");
		for (int i = 0, len = args.length; i < len; i++)
		{
			sb.append("Parameter" + i);
			if (i != len - 1)
			{
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
		String className = substringBeforeLast(sid, ".");
		String methodName = substringAfterLast(sid, ".");
		try
		{
			Object proxy = ServiceProxy.get(className);
			result = MethodUtils.invokeMethod(proxy, methodName, args);
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

	public static String getUserid()
	{
		Map<String, String> obj = getUser();
		return obj == null ? null : String.valueOf(obj.get("id"));
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

	public static Json json(String msg)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("msg", msg);
		return new Json(result);
	}

	public static Ftl ftl(String ftl, Map<String, Object> map)
	{
		return new Ftl(ftl, map);
	}

	public static void attr(String key, Object value, String scope)
	{
		if ("application".equals(scope))
		{
			getServletContext().setAttribute(key, value);
		}
		else if ("session".equals(scope))
		{
			getSession().setAttribute(key, value);
		}
		else
		{
			getRequest().setAttribute(key, value);
		}
	}

	public static Object attr(String key, String scope)
	{
		if ("application".equals(scope))
		{
			return getServletContext().getAttribute(key);
		}
		else if ("session".equals(scope))
		{
			return getSession().getAttribute(key);
		}
		else
		{
			return getRequest().getAttribute(key);
		}
	}

	public static String i18n(String key, Object... args)
	{
		return I18N.get(key, args);
	}

	private static String substringBeforeLast(String str, String separator)
	{
		if ((isEmpty(str)) || (isEmpty(separator)))
		{
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1)
		{
			return str;
		}
		return str.substring(0, pos);
	}

	private static String substringAfterLast(String str, String separator)
	{
		if (isEmpty(str))
		{
			return str;
		}
		if (isEmpty(separator))
		{
			return "";
		}
		int pos = str.lastIndexOf(separator);
		if ((pos == -1) || (pos == str.length() - separator.length()))
		{
			return "";
		}
		return str.substring(pos + separator.length());
	}

	private static boolean isEmpty(String str)
	{
		return (str == null) || (str.length() == 0);
	}
}
