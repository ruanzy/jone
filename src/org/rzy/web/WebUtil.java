package org.rzy.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtil
{
	final static String USERKEY = "USER";
	static Logger log = LoggerFactory.getLogger(WebUtil.class);
	static String pck = "service";

	public static Object call(String sid, Object... args)
	{
		String className = substringBeforeLast(sid, ".");
		String methodName = substringAfterLast(sid, ".");
		String fullName = pck + "." + className;
		StringBuffer sb = new StringBuffer();
		sb.append("service=").append(sid).append("(");
		for (int i = 0, len = args.length; i < len; i++)
		{
			sb.append("arg" + i);
			if (i != len - 1)
			{
				sb.append(",");
			}
		}
		sb.append(")");
		//log.debug(sb.toString());
		for (int i = 0, len = args.length; i < len; i++)
		{
			//log.debug("arg" + i + "=" + JSON.toJSONString(args[i]));
		}
		Object result = null;
		StringBuffer logs = new StringBuffer();
		try
		{
			Object proxy = ServiceProxy.get(fullName);
			result = MethodUtils.invokeMethod(proxy, methodName, args);
			String user = WebUtil.getUser();
			String ip = WebUtil.getIP();
			//String op = "";//Util.getOP(sid);
			//String requestBody = JSON.toJSONString(args);
			logs.append(user).append("|");
			logs.append(ip).append("|");
			//logs.append(op).append("|");
			logs.append(sid).append("|");
			//logs.append(requestBody).append("|");
			logs.append("").append("|");
			logs.append(1);
		}
		catch (Exception e)
		{
			String error = "";
			if (e instanceof ClassNotFoundException)
			{
				error = e.getMessage() + " Not Found.";
			}
			else if (e instanceof NoSuchMethodException)
			{
				error = e.getMessage();
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				error = t.getMessage();
			}
			logs.append(error).append("|");
			logs.append(0);
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}finally
		{
			log.debug(logs.toString());
		}
		return result;
	}

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

	public static String getHeader(String name)
	{
		return getRequest().getHeader(name);
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

	public static void setUser(String user)
	{
		getSession().setAttribute(USERKEY, user);
	}

	public static String getUser()
	{
		Object user = getSession().getAttribute(USERKEY);
		return (String) user;
	}

	public static boolean isAdmin(String username, String password)
	{
		return ("admin").equals(username) && ("162534").equals(password);
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
	
	public static String getWebRoot()
	{
		return getServletContext().getRealPath("/");
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
