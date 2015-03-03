package com.rz.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.interceptor.ActionInvocation;

public class DefaultActionHandler implements ActionHandler
{
	private static Logger log = LoggerFactory.getLogger(DefaultActionHandler.class);

	public void handle(ActionContext ac) throws IOException, ServletException
	{
		HttpServletRequest request = ac.getRequest();
		HttpServletResponse response = ac.getResponse();
		String url = request.getServletPath();
		String ip = request.getRemoteAddr();
		String m = request.getMethod();
		Object[] ps = new Object[] { ip, m, url };
		log.debug("{} {} {}", ps);
		try
		{
			new ActionInvocation(ac).invoke();
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
