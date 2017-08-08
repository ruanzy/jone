package jone.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ActionContext
{
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private final static ThreadLocal<ActionContext> actionContext = new ThreadLocal<ActionContext>();

	private ActionContext()
	{
	}

	static ActionContext create(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res)
	{
		ActionContext ac = new ActionContext();
		ac.servletContext = servletContext;
		ac.request = req;
		ac.response = res;
		actionContext.set(ac);
		return ac;
	}

	static void destroy()
	{
		actionContext.remove();
	}
	
	static ActionContext get()
	{
		return actionContext.get();
	}

	public static ServletContext getServletContext()
	{
		return get().servletContext;
	}

	public static HttpServletRequest getRequest()
	{
		return get().request;
	}

	public static HttpServletResponse getResponse()
	{
		return get().response;
	}
	
	public static Map<String, Object> getScopeMap()
	{
		ServletContext servletContext = getServletContext();
		HttpServletRequest request = getRequest();
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
