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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class WebUtil {
	
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
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
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
	public static Map<String, String> getCurrentUser()
	{
		return (Map<String, String>) getSession().getAttribute("user");
	}
	
	public static void setCurrentUser(Object user)
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
	
	public static void setVC(String vc)
	{
		getSession().setAttribute("vc", vc);
	}

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
		String methodName = StringUtils.substringAfterLast(sid, ".");
		try
		{	
			Object serviceProxy = ServiceProxy.create(sid);
			result = MethodUtils.invokeMethod(serviceProxy, methodName, args);
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
		Map<String, String> obj = getCurrentUser();
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

	public static void ok()
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("msg", null);
		try
		{
			HttpServletResponse response = getResponse();
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
			HttpServletResponse response = getResponse();
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
			HttpServletResponse response = getResponse();
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
}
