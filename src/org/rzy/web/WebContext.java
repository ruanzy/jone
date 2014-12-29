package org.rzy.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WebContext
{
	private ServletContext servletContext;

	private HttpSession session;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private final static ThreadLocal<WebContext> webContext = new ThreadLocal<WebContext>();

	protected static WebContext create(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res)
			throws UnsupportedEncodingException
	{
		WebContext wc = new WebContext();
		wc.servletContext = servletContext;
		wc.request = req;
		wc.response = res;
		wc.session = req.getSession();
		webContext.set(wc);
		return wc;
	}

	protected void destroy()
	{
		this.servletContext = null;
		this.request = null;
		this.response = null;
		this.session = null;
		webContext.remove();
	}

	public static ServletContext getServletContext()
	{
		return webContext.get().servletContext;
	}

	public static HttpSession getSession()
	{
		return webContext.get().session;
	}

	public static HttpServletRequest getRequest()
	{
		return webContext.get().request;
	}

	public static HttpServletResponse getResponse()
	{
		return webContext.get().response;
	}
}