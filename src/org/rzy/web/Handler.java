package org.rzy.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler
{
	private static Logger log = LoggerFactory.getLogger(JOne.class);

	public static void handle(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		String url = request.getServletPath();
		boolean ext = url.lastIndexOf(".") != -1;
		if (ext)
		{
			chain.doFilter(request, response);
			return;
		}
		String[] parts = url.substring(1).split("/");
		String pck_name = "action";
		String _action_name = parts[0];
		String action_name = Character.toTitleCase(_action_name.charAt(0)) + _action_name.substring(1);
		String action_method_name = (parts.length > 1) ? parts[1] : "execute";
		String ip = WebUtil.getIP();
		String m = WebUtil.getMethod();
		String user = WebUtil.getUser();
		Object[] ps = new Object[] { user, ip, m, url };
		log.debug("{} {} {} {}", ps);
		for (Cookie c : CookieManager.getAll())
		{
			System.out.println(c.getName());
		}
		try
		{
			Class<?> cls = Class.forName(pck_name + "." + action_name);
			Method method = cls.getMethod(action_method_name);
			Object result = method.invoke(cls.newInstance());
			if (result instanceof Result)
			{
				((Result) result).render();
			}
		}
		catch (Exception e)
		{
			if (e instanceof ClassNotFoundException || e instanceof NoSuchMethodException)
			{
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				t.printStackTrace();
				if (WebUtil.isAjax())
				{
					response.setStatus(9999);
					response.setCharacterEncoding("UTF-8");
					response.getWriter().print(t.getMessage());
				}
			}
			else
			{
				throw new ServletException(e);
			}
		}
	}
}
