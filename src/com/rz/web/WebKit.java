package com.rz.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebKit
{
	public static String getLang(HttpServletRequest request)
	{
		String lang = "zh";
		Cookie[] cks = request.getCookies();
		if (cks != null)
		{
			for (Cookie cookie : cks)
			{
				if ("lang".equals(cookie.getName()))
				{
					lang = cookie.getValue();
					break;
				}
			}
		}
		return lang;
	}
	
	public static Map<String, Object> getScopeMap()
	{
		ServletContext servletContext = ActionContext.getServletContext();
		HttpServletRequest request = ActionContext.getRequest();
		Map<String, Object> ps = new HashMap<String, Object>();
		Map<String, Object> applicationMap = getApplicationMap(servletContext);
		Map<String, Object> sessionMap = getSessionMap(request.getSession());
		Map<String, Object> requestMap = getRequestMap(request);
		Map<String, Object> parametersMap = getParametersMap(request);
		ps.putAll(applicationMap);
		ps.putAll(sessionMap);
		ps.putAll(requestMap);
		ps.putAll(parametersMap);
		ps.put("Application", applicationMap);
		ps.put("Session", sessionMap);
		ps.put("Request", requestMap);
		ps.put("Parameters", parametersMap);
		return ps;
	}

	private static Map<String, Object> getParametersMap(HttpServletRequest request)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		Enumeration<?> em = request.getParameterNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				String v = request.getParameter(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	private static Map<String, Object> getRequestMap(HttpServletRequest request)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		Enumeration<?> em = request.getAttributeNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				Object v = request.getAttribute(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	private static Map<String, Object> getSessionMap(HttpSession session)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		Enumeration<?> em = session.getAttributeNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				Object v = session.getAttribute(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	private static Map<String, Object> getApplicationMap(ServletContext servletContext)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		Enumeration<?> em = servletContext.getAttributeNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				Object v = servletContext.getAttribute(k);
				ps.put(k, v);
			}
		}
		return ps;
	}
}
