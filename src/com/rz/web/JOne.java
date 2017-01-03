package com.rz.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JOne implements Filter
{
	static final Logger logger = LoggerFactory.getLogger(JOne.class);
	private ServletContext context;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		ActionContext.create(context, request, response);
		String path = request.getServletPath();
		if ("/".equals(path))
		{
			chain.doFilter(request, response);
			return;
		}
		String url = path.substring(1);
		request.setCharacterEncoding("UTF-8");
		boolean isStatic = (url.lastIndexOf(".") != -1);
		// boolean isHtml = url.endsWith(".html") || url.endsWith(".htm");
		try
		{
			// if (isHtml)
			// {
			// new Html(url).handle();
			// return;
			// }
			if (isStatic)
			{
				chain.doFilter(request, response);
				return;
			}
			String name = getActionName(url);
			String method = getActionMethod(url);
			new Action(name, method).invoke();
		}
		catch (InvocationTargetException e)
		{
			Throwable t = e.getTargetException();
			t.printStackTrace();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			throw new ServletException(t);
		}
		finally
		{
			ActionContext.destroy();
		}
	}

	public void destroy()
	{
		List<Plugin> plugins = Container.findPlugins();
		for (Plugin plugin : plugins)
		{
			plugin.stop();
		}
	}

	public void init(FilterConfig cfg) throws ServletException
	{
		logger.debug("JOne Starting...");
		try
		{
			this.context = cfg.getServletContext();
			String basePackage = cfg.getInitParameter("base-package");
			Container.init(basePackage);
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
		logger.debug("JOne Started");
	}

	private String getActionName(String url)
	{
		String[] parts = url.split("/");
		String str = parts[0];
		return Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}

	private String getActionMethod(String url)
	{
		String[] parts = url.split("/");
		return (parts.length > 1) ? parts[1] : "execute";
	}
}