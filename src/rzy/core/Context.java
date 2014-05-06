package rzy.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;

public class Context
{
    private ServletContext servletContext;

	private HttpSession session;

	private HttpServletRequest request;

	private HttpServletResponse response;
	
	private boolean isAjax;

	private final static ThreadLocal<Context> context = new ThreadLocal<Context>();

	protected static Context begin(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res)
			throws UnsupportedEncodingException
	{
		Context ac = new Context();
		ac.servletContext = servletContext;
		req.setCharacterEncoding("UTF-8");
		ac.request = req;
		String xhr = req.getHeader("x-requested-with");
		if (StringUtils.isNotBlank(xhr))
		{
			ac.isAjax = true;
		}
		ac.response = res;
		ac.session = req.getSession();
		context.set(ac);
		return ac;
	}

	protected void end()
	{
		this.servletContext = null;
		this.request = null;
		this.response = null;
		this.session = null;
		context.remove();
	}

	protected static ServletContext getServletContext()
	{
		return context.get().servletContext;
	}

	protected static HttpSession getSession()
	{
		return context.get().session;
	}

	protected static HttpServletRequest getRequest()
	{
		return context.get().request;
	}

	protected static HttpServletResponse getResponse()
	{
		return context.get().response;
	}
	
	protected static boolean isAjax()
	{
		return context.get().isAjax;
	}

	protected static void redirect(String url)
	{
		try
		{
			HttpServletRequest request = getRequest();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/";
			getResponse().sendRedirect(basePath + url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected static void forward(String url)
	{
		RequestDispatcher rd = getRequest().getRequestDispatcher(url);
		try
		{
			rd.forward(getRequest(), getResponse());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}