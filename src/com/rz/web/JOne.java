package com.rz.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JOne implements Filter
{
	private ServletContext context;
	private Starter starter;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		ActionContext.create(context, request, response);
		String url = request.getServletPath().substring(1);
		request.setCharacterEncoding("UTF-8");
		boolean isStatic = (url.lastIndexOf(".") != -1);
		boolean isHtml = url.endsWith(".html") || url.endsWith(".htm");
		try
		{
			if (isHtml)
			{
				new Html(url).handle();
				return;
			}
			if (isStatic)
			{
				chain.doFilter(request, response);
				return;
			}
			new ActionInvocation().invoke();
		}
		catch (Exception e)
		{
			throw new ServletException(e);
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
		StringBuffer sb = new StringBuffer();
		sb.append("*************************************").append("\r\n");
		sb.append("**                                 **").append("\r\n");
		sb.append("**          JOne Satrting...       **").append("\r\n");
		sb.append("**                                 **").append("\r\n");
		sb.append("*************************************");
		System.out.println(sb);
		try
		{
			String _starter = cfg.getInitParameter("starter");
			this.context = cfg.getServletContext();
			if (_starter != null)
			{
				Class<?> startercls = Class.forName(_starter);
				this.starter = (Starter) (startercls.newInstance());
				this.starter.start(this.context);
			}
			Container.init();
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
		StringBuffer sb2 = new StringBuffer();
		sb2.append("*************************************").append("\r\n");
		sb2.append("**                                 **").append("\r\n");
		sb2.append("**           JOne Satrtup          **").append("\r\n");
		sb2.append("**                                 **").append("\r\n");
		sb2.append("*************************************");
		System.out.println(sb2);
	}
}