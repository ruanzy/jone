package org.rzy.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ActionContext
{
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private final static ThreadLocal<ActionContext> actionContexts = new ThreadLocal<ActionContext>();

	private ActionContext()
	{
	}

	protected static void create(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res)
	{
		ActionContext ac = new ActionContext();
		ac.servletContext = servletContext;
		ac.request = req;
		ac.response = res;
		actionContexts.set(ac);
	}

	protected static void destroy()
	{
		actionContexts.remove();
	}

	public ServletContext getServletContext()
	{
		return servletContext;
	}

	public HttpServletRequest getHttpServletRequest()
	{
		return request;
	}

	public HttpServletResponse getHttpServletResponse()
	{
		return response;
	}

	public HttpSession getHttpSession(boolean create)
	{
		return request.getSession(create);
	}

	public static ActionContext getActionContext()
	{
		return actionContexts.get();
	}
}