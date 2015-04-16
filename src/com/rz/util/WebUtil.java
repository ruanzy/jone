package com.rz.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.service.ServiceUtil;
import com.rz.web.ActionContext;

public class WebUtil
{
	static Logger log = LoggerFactory.getLogger(WebUtil.class);

	private WebUtil()
	{
	}

	public static Object call(String sid, Object... args)
	{
		return ServiceUtil.call(sid, args);
	}

	public static class Request
	{
		public static HttpServletRequest get()
		{
			return ActionContext.get().getRequest();
		}

		public static void setCharacterEncoding(String encoding)
		{
			try
			{
				get().setCharacterEncoding(encoding);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		public static void attr(String key, Object value)
		{
			get().setAttribute(key, value);
		}

		public static Object attr(String key)
		{
			return get().getAttribute(key);
		}
	}

	public static class Response
	{
		public static HttpServletResponse get()
		{
			return ActionContext.get().getResponse();
		}

		public static void setContentType(String type)
		{
			get().setContentType(type);
		}

		public static void setCharacterEncoding(String encoding)
		{
			get().setCharacterEncoding(encoding);
		}

		public static OutputStream getOutputStream()
		{
			try
			{
				return get().getOutputStream();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		public static PrintWriter getWriter()
		{
			try
			{
				return get().getWriter();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		public static void write(String txt)
		{
			getWriter().print(txt);
		}
	}

	public static class Cookies
	{
		public static void add(Cookie cookie)
		{
			HttpServletResponse response = WebUtil.Response.get();
			response.addCookie(cookie);
		}

		public static String get(String cookieName)
		{
			String cookieValue = null;
			Cookie[] cks = WebUtil.Request.get().getCookies();
			if (cks != null)
			{
				for (Cookie cookie : cks)
				{
					if (cookieName.equals(cookie.getName()))
					{
						cookieValue = cookie.getValue();
						break;
					}
				}
			}
			return cookieValue;
		}

		public static List<Cookie> getAll()
		{
			List<Cookie> all = null;
			Cookie[] cks = WebUtil.Request.get().getCookies();
			if (cks != null)
			{
				all = Arrays.asList(cks);
			}
			return all;
		}

		public static void clear(String cookieName)
		{
			Cookie[] cks = WebUtil.Request.get().getCookies();
			if (cks != null)
			{
				for (Cookie cookie : cks)
				{
					if (cookieName.equals(cookie.getName()))
					{
						cookie.setPath("/");
						cookie.setMaxAge(0);
						WebUtil.Response.get().addCookie(cookie);
						break;
					}
				}
			}
		}
	}

	public static class Session
	{
		public static HttpSession get()
		{
			return get(false);
		}

		public static HttpSession get(boolean create)
		{
			return WebUtil.Request.get().getSession(create);
		}

		public static void clear()
		{
			get().invalidate();
		}

		public static void attr(String key, Object value)
		{
			get().setAttribute(key, value);
		}

		public static Object attr(String key)
		{
			return get().getAttribute(key);
		}
	}

	public static class Application
	{
		public static ServletContext get()
		{
			return ActionContext.get().getServletContext();
		}

		public static void attr(String key, Object value)
		{
			get().setAttribute(key, value);
		}

		public static Object attr(String key)
		{
			return get().getAttribute(key);
		}

		public static void setUserres(String user)
		{
			attr("USERRES", user);
		}

		public static String getUserres()
		{
			Object user = attr("USERRES");
			return (String) user;
		}
	}

	public static String getWebRoot()
	{
		return WebUtil.Application.get().getRealPath("/");
	}

	public static String getMethod()
	{
		return WebUtil.Request.get().getMethod();
	}

	public static Map<String, String> getParameters()
	{
		Map<String, String> ps = new HashMap<String, String>();
		Enumeration<?> em = WebUtil.Request.get().getParameterNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				String v = getParameter(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	public static String getParameter(String name)
	{
		return WebUtil.Request.get().getParameter(name);
	}

	public static String getIP()
	{
		return WebUtil.Request.get().getRemoteAddr();
	}

	public static String getHeader(String key)
	{
		return WebUtil.Request.get().getHeader(key);
	}

	public static boolean isAjax()
	{
		String xhr = getHeader("x-requested-with");
		if (xhr != null && xhr.trim().length() > 0)
		{
			return true;
		}
		return false;
	}

	public static boolean isAdmin(String username, String password)
	{
		return ("admin").equals(username) && ("162534").equals(password);
	}

	public static void forward(String url)
	{
		RequestDispatcher rd = WebUtil.Request.get().getRequestDispatcher(url);
		try
		{
			rd.forward(WebUtil.Request.get(), WebUtil.Response.get());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void redirect(String url)
	{
		try
		{
			WebUtil.Response.get().sendRedirect(url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void setUserinfo(String userinfo)
	{
		WebUtil.Session.attr("RZY_USER", userinfo.split("_")[0]);
		WebUtil.Session.attr("user", userinfo.split("_")[0]);
		// String domain = WebUtil.Request.get().getServerName();
		Cookie token = new Cookie("SSOTOKEN", userinfo);
		token.setMaxAge(30 * 60);
		token.setPath("/");
		WebUtil.Cookies.add(token);
	}

	public static String getUserinfo()
	{
		Object userinfo = (String) WebUtil.Session.attr("RZY_USER");
		if (userinfo != null)
		{
			return (String) userinfo;
		}
		return null;
	}

	public static String getUser()
	{
		String ssotoken = null;
		Cookie[] cks = WebUtil.Request.get().getCookies();
		if (cks != null)
		{
			for (Cookie cookie : cks)
			{
				if ("SSOTOKEN".equals(cookie.getName()))
				{
					ssotoken = cookie.getValue();
					break;
				}
			}
		}
		if (ssotoken != null)
		{
			String[] arr = ssotoken.split("_");
			return arr[0];
		}
		return null;
	}
}