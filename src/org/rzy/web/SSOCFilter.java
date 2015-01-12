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

public class SSOCFilter implements Filter
{
	private static final String SSOURL = "http://localhost:8088/JOne/SSO";
	private static final String NOCHECK = "(captcha|login.*|logout|setCookie)$";

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String path = request.getServletPath();
		String url = request.getRequestURL().toString();
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
			response.sendRedirect(SSOURL + "?go=" + url);
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