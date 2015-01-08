package org.rzy.web;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter
{
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getServletPath();
		boolean extension = url.lastIndexOf(".") != -1;
		boolean page = Pattern.compile("(.jsp|.html|.htm)$").matcher(url).find();
		boolean nologin = Pattern.compile("(captcha|login.*|logout)$").matcher(url).find();
		if (nologin || extension && !page)
		{
			chain.doFilter(request, response);
			return;
		}
		HttpSession session = request.getSession();
		if (session.getAttribute("RZY_USER") == null)
		{
			String xhr = request.getHeader("x-requested-with");
			if (xhr != null && xhr.length() > 0)
			{
				response.sendError(1111);
			}
			else
			{
				response.sendRedirect(request.getContextPath() + "/login.html");
			}
		}
		else
		{
			chain.doFilter(request, response);
			return;
		}
	}

	public void init(FilterConfig config) throws ServletException
	{
	}

	public void destroy()
	{
	}
}