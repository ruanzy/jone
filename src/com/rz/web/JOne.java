package com.rz.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.rz.schedule.Schedules;
import com.rz.web.interceptor.Interceptors;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class JOne implements Filter
{
	private ServletContext context;
	static Configuration conf = new Configuration();
	private Initializer initializer;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getServletPath();
		request.setCharacterEncoding("UTF-8");
		boolean isStatic = (url.lastIndexOf(".") != -1);
		boolean isHtml = url.endsWith(".html") || url.endsWith(".htm");
		if (isStatic && !isHtml)
		{
			chain.doFilter(request, response);
			return;
		}
		try
		{
			ActionContext ac = ActionContext.create(context, request, response);
			if (isHtml)
			{
				String fn = url.substring(1);
				response.setContentType("text/html;charset=UTF-8");
				Template t = conf.getTemplate(fn, "UTF-8");
				Map<String, Object> data = getScopeMap(ac);
				t.process(data, response.getWriter());
			}
			else
			{
				new ActionInvocation(ac).invoke();
			}
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
		String _initializer = cfg.getInitParameter("initializer");
		try
		{
			this.context = cfg.getServletContext();
			if (_initializer != null)
			{
				Class<?> initializercls = Class.forName(_initializer);
				this.initializer = (Initializer) (initializercls.newInstance());
				this.initializer.init(this.context);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("*************************************").append("\r\n");
			sb.append("**                                 **").append("\r\n");
			sb.append("**          JOne Satrting...       **").append("\r\n");
			sb.append("**                                 **").append("\r\n");
			sb.append("*************************************");
			System.out.println(sb);
			Schedules.init();
			Interceptors.init();
			conf.setServletContextForTemplateLoading(this.context, "/");
			conf.setDefaultEncoding("UTF-8");
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}

	public Map<String, Object> getScopeMap(ActionContext ac)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.putAll(getApplicationMap(ac.getServletContext()));
		ps.putAll(getSessionMap(ac.getRequest().getSession()));
		ps.putAll(getRequestMap(ac.getRequest()));
		ps.putAll(getParametersMap(ac.getRequest()));
		ps.put("Application", getApplicationMap(ac.getServletContext()));
		ps.put("Session", getSessionMap(ac.getRequest().getSession()));
		ps.put("Request", getRequestMap(ac.getRequest()));
		ps.put("Parameters", getParametersMap(ac.getRequest()));
		return ps;
	}

	public Map<String, Object> getParametersMap(HttpServletRequest request)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		Enumeration<?> em = request.getParameterNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				String v = request.getParameter(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	public Map<String, Object> getRequestMap(HttpServletRequest request)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		Enumeration<?> em = request.getAttributeNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				Object v = request.getAttribute(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	public Map<String, Object> getSessionMap(HttpSession session)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		Enumeration<?> em = session.getAttributeNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				Object v = session.getAttribute(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	public Map<String, Object> getApplicationMap(ServletContext servletContext)
	{
		Map<String, Object> ps = new HashMap<String, Object>();
		Enumeration<?> em = servletContext.getAttributeNames();
		if (em.hasMoreElements())
		{
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				Object v = servletContext.getAttribute(k);
				ps.put(k, v);
			}
		}
		return ps;
	}
}