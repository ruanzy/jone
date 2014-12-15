package org.rzy.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	private ServletContext context;
	Logger log = LoggerFactory.getLogger(Filter.class);

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Context rc = Context.begin(this.context, request, response);
		String url = request.getServletPath();
		try
		{
			long t1 = System.currentTimeMillis();
			boolean extension = url.lastIndexOf(".") != -1;
			if (extension)
			{
				chain.doFilter(Context.getRequest(), Context.getResponse());
				return;
			}
			String[] parts = url.substring(1).split("/");
			if (parts.length < 1)
			{
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			String pck_name = "action";
			String _action_name = parts[0];
			String action_name = Character.toTitleCase(_action_name.charAt(0)) + _action_name.substring(1);
			String action_method_name = (parts.length > 1) ? parts[1] : "execute";
			if ("Captcha".equals(action_name) || "Logout".equals(action_name))
			{
				pck_name = "org.rzy.web.action";
			}
			Class<?> cls = Class.forName(pck_name + "." + action_name);
			// Object result = MethodUtils.invokeMethod(cls.newInstance(),
			// action_method_name, null);
			Method method = cls.getMethod(action_method_name);
			Object result = method.invoke(cls.newInstance());
			if (result instanceof Result)
			{
				((Result) result).render();
			}
			long t2 = System.currentTimeMillis();
			String action = pck_name + "." + action_name + "." + action_method_name;
			String t = (t2 - t1) + "ms";
			String ip = request.getRemoteAddr();
			Object[] ps = new Object[] { ip, url, action, t };
			log.debug("{} {} {} {}", ps);
		}
		catch (Exception e)
		{
			if (e instanceof ClassNotFoundException || e instanceof NoSuchMethodException)
			{
				log.error("请求路径" + url + "错误!");
				if (Context.isAjax())
				{
					response.setStatus(500);
					response.setCharacterEncoding("UTF-8");
					response.getWriter().print("请求路径" + url + "错误!");
				}
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				log.debug(t.getMessage());
				t.printStackTrace();
				if (Context.isAjax())
				{
					response.setStatus(9999);
					response.getWriter().print(t.getMessage());
				}
			}
			else
			{
				throw new ServletException(e);
			}
		}
		finally
		{
			if (rc != null)
			{
				rc.end();
			}
		}
	}

	public void destroy()
	{

	}

	public void init(FilterConfig cfg) throws ServletException
	{
		this.context = cfg.getServletContext();
		JvmMonitor.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n");
		sb.append("*************************************").append("\r\n");
		sb.append("*          JOne satrting...         *").append("\r\n");
		sb.append("*************************************");
        log.debug(sb.toString());
	}
}