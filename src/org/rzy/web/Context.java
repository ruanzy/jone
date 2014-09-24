package org.rzy.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;

public class Context
{
	private ServletContext servletContext;

	private HttpSession session;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private boolean isAjax;

	private Map<String, String> parameters;

	private final static ThreadLocal<Context> context = new ThreadLocal<Context>();

	public static Context begin(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res) throws UnsupportedEncodingException
	{
		Context ac = new Context();
		ac.servletContext = servletContext;
		req.setCharacterEncoding("UTF-8");
		ac.request = req;
		ac.parameters = new HashMap<String, String>();
		String xhr = req.getHeader("x-requested-with");
		if (StringUtils.isNotBlank(xhr))
		{
			ac.isAjax = true;
		}
		ac.response = res;
		ac.session = req.getSession();
		Enumeration<?> em = ac.request.getParameterNames();
		while (em.hasMoreElements())
		{
			String k = (String) em.nextElement();
			String v = ac.request.getParameter(k);
			if (StringUtils.isNotBlank(v))
			{
				ac.parameters.put(k, v);
			}
		}
		context.set(ac);
		return ac;
	}

	public void end()
	{
		this.servletContext = null;
		this.request = null;
		this.response = null;
		this.session = null;
		context.remove();
	}

	public static ServletContext getServletContext()
	{
		return context.get().servletContext;
	}

	public static HttpSession getSession()
	{
		return context.get().session;
	}

	public static HttpServletRequest getRequest()
	{
		return context.get().request;
	}

	public static HttpServletResponse getResponse()
	{
		return context.get().response;
	}

	public static boolean isAjax()
	{
		return context.get().isAjax;
	}

	public static Map<String, String> getParameters()
	{
		return context.get().parameters;
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
	public static Map<String, String> currentUser()
	{
		return (Map<String, String>) getSession().getAttribute("user");
	}
}