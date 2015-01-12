package org.rzy.web;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SSOSFilter implements Filter
{
	private static final String LOGINPAGE = "login.jsp";
	private static final String NOCHECK = "(captcha|login.*|logout|cookies)$";

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String path = request.getServletPath();
		String go = request.getParameter("go");
		boolean extension = path.lastIndexOf(".") != -1;
		boolean page = Pattern.compile("(.jsp|.html|.htm)$").matcher(path).find();
		boolean nocheck = Pattern.compile(NOCHECK).matcher(path).find();
		if (nocheck || extension && !page)
		{
			chain.doFilter(request, response);
			return;
		}
		String token = getToken(request, "SSOTOKEN");
		if (token == null)
		{
			String xhr = request.getHeader("x-requested-with");
			if (xhr != null && xhr.length() > 0)
			{
				response.sendError(1111);
			}
			else
			{
				String redirectpath = request.getContextPath() + "/" + LOGINPAGE;
				if(go != null){
					redirectpath += "?go=" + go;
				}
				response.sendRedirect(redirectpath);
			}
			return;
		}
		else
		{
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig config) throws ServletException
	{

	}

	public void destroy()
	{
	}

	private String getToken(HttpServletRequest request, String cookieName)
	{
		Cookie[] cookies = request.getCookies();
		String token = null;
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				if (cookie.getName().equals(cookieName))
				{
					token = cookie.getValue();
					break;
				}
			}
		}
		return token;
	}
}