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

public class JOne implements Filter
{
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
			throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(e);
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable t)
		{
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
		StringBuffer sb = new StringBuffer();
		sb.append("|-----------------------------------|").append("\r\n");
		sb.append("|                                   |").append("\r\n");
		sb.append("|          JOne Initing...          |").append("\r\n");
		sb.append("|                                   |").append("\r\n");
		sb.append("|-----------------------------------|");
		System.out.println(sb);
		try
		{
			this.context = cfg.getServletContext();
			Container.init();
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
		StringBuffer sb2 = new StringBuffer();
		sb2.append("|-----------------------------------|").append("\r\n");
		sb2.append("|                                   |").append("\r\n");
		sb2.append("|           JOne Started            |").append("\r\n");
		sb2.append("|                                   |").append("\r\n");
		sb2.append("|-----------------------------------|");
		System.out.println(sb2);
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