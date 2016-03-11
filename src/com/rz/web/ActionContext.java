package com.rz.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	public static ActionContext get()
	{
		return actionContext.get();
	}

	public ServletContext getServletContext()
	{
		return get().servletContext;
	}

	public HttpServletRequest getRequest()
	{
		return get().request;
	}

	public HttpServletResponse getResponse()
	{
		return get().response;
	}
}
