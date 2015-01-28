package org.rzy.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionHandler
{
	private static Logger log = LoggerFactory.getLogger(ActionHandler.class);
	private ServletContext servletContext;

	// private Interceptor[] interceptors = null;

	public ActionHandler(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		ActionContext.create(this.servletContext, request, response);
		String url = request.getServletPath();
		String[] parts = url.substring(1).split("/");
		String pck_name = "action";
		String _action_name = parts[0];
		String action_name = Character.toTitleCase(_action_name.charAt(0)) + _action_name.substring(1);
		String action_method_name = (parts.length > 1) ? parts[1] : "execute";
		String ip = request.getRemoteAddr();
		String m = request.getMethod();
		Object user = ActionContext.getActionContext().getHttpSession(false).getAttribute("RZY_USER");
		Object[] ps = new Object[] { user, ip, m, url };
		log.debug("{} {} {} {}", ps);
		try
		{
			Class<?> cls = Class.forName(pck_name + "." + action_name);
			Method method = cls.getMethod(action_method_name);
			Object result = method.invoke(cls.newInstance());
			if (result instanceof View)
			{
				((View) result).render();
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
		finally
		{
			ActionContext.destroy();
		}
	}
}
