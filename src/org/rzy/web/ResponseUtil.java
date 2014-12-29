package org.rzy.web;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

public class ResponseUtil
{
	public static void setContentType(String type)
	{
		WebContext.getResponse().setContentType(type);
	}

	public static void setCharacterEncoding(String encoding)
	{
		WebContext.getResponse().setCharacterEncoding(encoding);
	}

	public static void redirect(String url)
	{
		try
		{
			HttpServletRequest request = WebContext.getRequest();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/";
			WebContext.getResponse().sendRedirect(basePath + url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void forward(String url)
	{
		RequestDispatcher rd = WebContext.getRequest().getRequestDispatcher(url);
		try
		{
			rd.forward(WebContext.getRequest(), WebContext.getResponse());
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
			return WebContext.getResponse().getOutputStream();
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
			WebContext.getResponse().getWriter().print(txt);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
