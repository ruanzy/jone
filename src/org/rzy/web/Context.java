package org.rzy.web;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Context
{
	private ServletContext servletContext;

	private HttpSession session;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private boolean isAjax;

	private Map<String, String> parameters;

	private final static ThreadLocal<Context> context = new ThreadLocal<Context>();

	protected static Context begin(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res) throws UnsupportedEncodingException
	{
		Context ac = new Context();
		ac.servletContext = servletContext;
		req.setCharacterEncoding("UTF-8");
		ac.request = req;
		ac.parameters = new HashMap<String, String>();
		String xhr = req.getHeader("x-requested-with");
		if (isNotBlank(xhr))
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
			if (isNotBlank(v))
			{
				ac.parameters.put(k, v);
			}
		}
		context.set(ac);
		return ac;
	}

	protected void end()
	{
		this.servletContext = null;
		this.request = null;
		this.response = null;
		this.session = null;
		context.remove();
	}

	protected static ServletContext getServletContext()
	{
		return context.get().servletContext;
	}

	protected static HttpSession getSession()
	{
		return context.get().session;
	}

	protected static HttpServletRequest getRequest()
	{
		return context.get().request;
	}

	protected static HttpServletResponse getResponse()
	{
		return context.get().response;
	}

	protected static boolean isAjax()
	{
		return context.get().isAjax;
	}

	protected static Map<String, String> getParameters()
	{
		return context.get().parameters;
	}
	
	private static boolean isBlank(String str)
	{
		int strLen;
	    if ((str == null) || ((strLen = str.length()) == 0)) {
	      return true;
	    }
	    for (int i = 0; i < strLen; i++) {
	      if (!Character.isWhitespace(str.charAt(i))) {
	        return false;
	      }
	    }
	    return true;
	}

	private static boolean isNotBlank(String str)
	{
		return !isBlank(str);
	}
}