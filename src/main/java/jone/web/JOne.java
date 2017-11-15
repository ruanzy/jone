package jone.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import com.alibaba.fastjson.JSON;

public class JOne implements Filter
{
	static final Logger logger = LoggerFactory.getLogger(JOne.class);
	private ServletContext context;

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException
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
			String[] parts = url.split("/");
			String str = parts[0];
			String name = Character.toTitleCase(str.charAt(0))
					+ str.substring(1);
			String method = (parts.length > 1) ? parts[1] : "execute";
			Object a = Container.findAction(name);
			if (a == null)
			{
				String error = "Action " + name + " not found!";
				throw new ClassNotFoundException(error);
			}
			Method m = a.getClass().getMethod(method);
			if (m == null)
			{
				String error = "Method " + method + " not found!";
				throw new NoSuchMethodException(error);
			}
			Object[] ps = new Object[] { name, method };
			logger.debug("Action={}, method={}", ps);
			Object result = new Action(a, m).invoke();
			NoRender nr = m.getAnnotation(NoRender.class);
			if (nr == null)
			{
				if (result == null)
				{
					response.setStatus(HttpServletResponse.SC_NO_CONTENT);
					response.flushBuffer();
				}
				else if (result instanceof String)
				{
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().print(result);
				}
				else
				{
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().print(JSON.toJSONString(result));
				}
			}
		}
		catch (InvocationTargetException e)
		{
			Throwable t = e.getTargetException();
			t.printStackTrace();
			Throwable tt = t;
			while ((tt.getCause()) != null)
			{
				tt = tt.getCause();
			}
			response.sendError(500, tt.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.sendError(500, e.toString());
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
		Banner.print();
		logger.debug("JOne Starting...");
		try
		{
			this.context = cfg.getServletContext();
			String basePackage = cfg.getInitParameter("basepackage");
			logger.debug("JOne basePackage {}", basePackage);
			Container.init(basePackage);
			logger.debug("JOne Started Successfully!");
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}
}