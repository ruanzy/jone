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

public class LoginFilter implements Filter
{
	private static final String LOGINPAGE = "login.jsp";
	private static final String NOCHECK = "(captcha|login.*|logout|cookies)$";

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getServletPath();
		boolean extension = url.lastIndexOf(".") != -1;
		boolean page = Pattern.compile("(.jsp|.html|.htm)$").matcher(url).find();
		boolean nocheck = Pattern.compile(NOCHECK).matcher(url).find();
		if (nocheck || extension && !page)
		{
			chain.doFilter(request, response);
			return;
		}
		String token = getToken(request);
		if (token == null)
		{
			String xhr = request.getHeader("x-requested-with");
			if (xhr != null && xhr.length() > 0)
			{
				response.sendError(1111);
			}
			else
			{
				response.sendRedirect(request.getContextPath() + "/" + LOGINPAGE);
			}
			return;
		}else{
		chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig config) throws ServletException
	{

	}

	public void destroy()
	{
	}

	private String getToken(HttpServletRequest request)
	{
		Cookie[] cookies = request.getCookies();
		String token = null;
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				if (cookie.getName().equals("SSOTOKEN"))
				{
					token = cookie.getValue();
					break;
				}
			}
		}
		return token;
	}
}