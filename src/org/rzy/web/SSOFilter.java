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

public class SSOFilter implements Filter
{
	private String loginPage = "http://localhost:8088/UMS/login.jsp";

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ path;
		String url = request.getServletPath();
		boolean extension = url.lastIndexOf(".") != -1;
		boolean page = Pattern.compile("(.jsp|.html|.htm)$").matcher(url).find();
		boolean logout = Pattern.compile("logout$").matcher(url).find();
		if (extension && !page)
		{
			chain.doFilter(request, response);
			return;
		}
		if (logout)
		{
			Cookie token = new Cookie("SSOTOKEN", null);
			token.setDomain("11.0.0.106:8088");
			token.setMaxAge(0);
			token.setPath("/");
			response.addCookie(token);
			response.sendRedirect(loginPage + "?go=" + basePath);
			return;
		}
		Cookie[] diskCookies = request.getCookies();
		boolean isLogin = false;
		if (diskCookies != null)
		{
			for (int i = 0; i < diskCookies.length; i++)
			{
				String domain = diskCookies[i].getDomain();
				System.out.println(domain);
				if (diskCookies[i].getName().equals("SSOTOKEN"))
				{
					String cookieValue = diskCookies[i].getValue();
					System.out.println(cookieValue);
					isLogin = true;
					break;
				}
			}
		}
		if (!isLogin)
		{
			// String xhr = request.getHeader("x-requested-with");
			// if (isNotBlank(xhr))
			// {
			// response.sendError(1111);
			// }
			// else
			// {
			// response.sendRedirect(request.getContextPath() + "/login.html");
			// }
			response.sendRedirect(loginPage + "?go=" + basePath + url);
			return;
		}
		else
		{
			chain.doFilter(request, response);
			return;
		}
	}

	public void init(FilterConfig config) throws ServletException
	{
		String _loginPage = config.getInitParameter("loginPage");
		if (_loginPage != null)
		{
			loginPage = _loginPage;
		}
	}

	public void destroy()
	{

	}
}
