package com.rz.web;

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
import com.rz.common.TokenUtil;

public class LoginFilter implements Filter
{
	private static final String NOCHECK = "(captcha|login.*|logout)$";

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String path = request.getServletPath();
		if ("/".equals(path))
		{
			chain.doFilter(request, response);
			return;
		}
		boolean extension = path.lastIndexOf(".") != -1;
		boolean page = Pattern.compile("(.jsp|.html|.htm)$").matcher(path).find();
		boolean nocheck = Pattern.compile(NOCHECK).matcher(path).find();
		if (nocheck || extension && !page)
		{
			chain.doFilter(request, response);
			return;
		}
		if (page)
		{
			chain.doFilter(request, response);
			return;
		}
		String token = getToken(request);
		boolean valid = TokenUtil.validateToken(token);
		if (!valid)
		{
			if (isAjax(request))
			{
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
			else
			{
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
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

	// private String getToken(HttpServletRequest request, String cookieName)
	// {
	// Cookie[] cookies = request.getCookies();
	// String token = null;
	// if (cookies != null)
	// {
	// for (Cookie cookie : cookies)
	// {
	// if (cookie.getName().equals(cookieName))
	// {
	// token = cookie.getValue();
	// break;
	// }
	// }
	// }
	// return token;
	// }

	private String getToken(HttpServletRequest request)
	{
		return request.getHeader("Authorization");
	}

	private boolean isAjax(HttpServletRequest request)
	{
		String xhr = request.getHeader("X-Requested-With");
		return (xhr != null) && ("XMLHttpRequest".equals(xhr));
	}
}