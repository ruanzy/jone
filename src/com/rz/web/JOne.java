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
import com.rz.schedule.Schedules;

public class JOne implements Filter
{
	private ServletContext context;
	private ActionHandler ah;

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
			ah.handle(ac);
		}
		catch (IOException e)
		{
			throw new IOException(e);
		}
		catch (ServletException e)
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
		Schedules.stop();
	}

	public void init(FilterConfig cfg) throws ServletException
	{
		String _ah = "com.rz.web.DefaultActionHandler";
		try
		{
			this.context = cfg.getServletContext();
			String _ActionHandler = cfg.getInitParameter("ActionHandler");
			if (_ActionHandler != null)
			{
				_ah = _ActionHandler;
			}
			Class<?> cls = Class.forName(_ah);
			this.ah = (ActionHandler) (cls.newInstance());
			StringBuffer sb = new StringBuffer();
			sb.append("*************************************").append("\r\n");
			sb.append("**                                 **").append("\r\n");
			sb.append("**          JOne Satrting...       **").append("\r\n");
			sb.append("**                                 **").append("\r\n");
			sb.append("*************************************");
			System.out.println(sb);
			Schedules.start();
			Plugins.init(this.context);
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}
}