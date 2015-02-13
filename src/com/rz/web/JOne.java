package com.rz.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.MethodUtils;

public class JOne implements Filter
{
	private ServletContext context;
	private String ActionHandler = "com.rz.web.DefaultActionHandler";

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getServletPath();
		request.setCharacterEncoding("UTF-8");
		boolean isStatic = url.lastIndexOf(".") != -1;
		if (isStatic)
		{
			chain.doFilter(request, response);
			return;
		}
		try
		{
			ActionContext ac = ActionContext.create(context, request, response);
			Class<?> cls = Class.forName(ActionHandler);
			MethodUtils.invokeMethod(cls.newInstance(), "handle", ac);
		}
		catch (Exception e)
		{
			if (e instanceof ClassNotFoundException || e instanceof NoSuchMethodException)
			{
				response.sendError(HttpServletResponse.SC_NOT_FOUND, ActionHandler + "not found");
				return;
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				t.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t.getMessage());
				return;
			}
			else
			{
				throw new ServletException(e);
			}
		}
		finally
		{
			ActionContext.destroy();
		}

	}

	public void destroy()
	{
	}

	public void init(FilterConfig cfg) throws ServletException
	{
		this.context = cfg.getServletContext();
		String _ActionHandler = cfg.getInitParameter("ActionHandler");
		if (_ActionHandler != null)
		{
			this.ActionHandler = _ActionHandler;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("*************************************").append("\r\n");
		sb.append("**                                 **").append("\r\n");
		sb.append("**          JOne Satrting...       **").append("\r\n");
		sb.append("**                                 **").append("\r\n");
		sb.append("*************************************");
		System.out.println(sb);
		Plugins.init(this.context);
	}
}