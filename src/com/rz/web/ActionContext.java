package com.rz.web;

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
	
	public static ActionContext getActionContext()
	{
		return actionContext.get();
	}

	public ServletContext getServletContext()
	{
		return actionContext.get().servletContext;
	}

	public HttpServletRequest getRequest()
	{
		return actionContext.get().request;
	}

	public HttpServletResponse getResponse()
	{
		return actionContext.get().response;
	}

	public HttpSession getSession(boolean create)
	{
		return getRequest().getSession(create);
	}
}
