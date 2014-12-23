package org.rzy.web;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

public class ResponseUtil
{
	public static void setContentType(String type)
	{
		Context.getResponse().setContentType(type);
	}

	public static void setCharacterEncoding(String encoding)
	{
		Context.getResponse().setCharacterEncoding(encoding);
	}

	public static void redirect(String url)
	{
		try
		{
			HttpServletRequest request = Context.getRequest();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/";
			Context.getResponse().sendRedirect(basePath + url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void forward(String url)
	{
		RequestDispatcher rd = Context.getRequest().getRequestDispatcher(url);
		try
		{
			rd.forward(Context.getRequest(), Context.getResponse());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static OutputStream getOutputStream()
	{
		try
		{
			return Context.getResponse().getOutputStream();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void write(String txt)
	{
		try
		{
			Context.getResponse().getWriter().print(txt);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
