package com.rz.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultActionHandler implements ActionHandler
{
	private static Logger log = LoggerFactory.getLogger(DefaultActionHandler.class);

	public void handle(ActionContext ac) throws IOException, ServletException
	{
		HttpServletRequest request = ac.getRequest();
		HttpServletResponse response = ac.getResponse();
		String url = request.getServletPath();
		String[] parts = url.substring(1).split("/");
		String _action = parts[0];
		String action = Character.toTitleCase(_action.charAt(0)) + _action.substring(1);
		String actionMethod = (parts.length > 1) ? parts[1] : "execute";
		String ip = request.getRemoteAddr();
		String m = request.getMethod();
		Object[] ps = new Object[] { ip, m, url };
		log.debug("{} {} {}", ps);
		try
		{
			Class<?> cls = Class.forName("action." + action);
			Method method = cls.getMethod(actionMethod);
			Object result = method.invoke(cls.newInstance());
			if (result instanceof View)
			{
				((View) result).render(ac);
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
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t.getMessage());
				return;
			}
			else
			{
				throw new ServletException(e);
			}
		}
	}
}
