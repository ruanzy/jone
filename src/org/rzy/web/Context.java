package org.rzy.web;

import java.io.UnsupportedEncodingException;
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

	private final static ThreadLocal<Context> context = new ThreadLocal<Context>();

	protected static Context begin(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res)
			throws UnsupportedEncodingException
	{
		Context ac = new Context();
		ac.servletContext = servletContext;
		ac.request = req;
		ac.response = res;
		ac.session = req.getSession();
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
}