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
import org.rzy.util.HttpResult;
import org.rzy.util.HttpUtil;

public class SSOFilter implements Filter
{
	private String loginPage = "http://localhost:8088/JOne/login.html";

	private String excludeReg;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
		String url = request.getServletPath();
		boolean extension = url.lastIndexOf(".") != -1;
		boolean page = Pattern.compile("(.jsp|.html|.htm)$").matcher(url).find();
		boolean exclude = Pattern.compile(excludeReg).matcher(url).find();
		if (exclude || extension && !page)
		{
			chain.doFilter(request, response);
			return;
		}
		HttpResult result = HttpUtil.get("http://localhost:8088/JOne/common/cookies", null, null);
		String str = result.getBody();
		System.out.println(str);
		boolean isLogin = false;
//		if (diskCookies != null)
//		{
//			for (int i = 0; i < diskCookies.length; i++)
//			{
//				String domain = diskCookies[i].getDomain();
//				System.out.println(domain);
//				if (diskCookies[i].getName().equals("SSOTOKEN"))
//				{
//					String cookieValue = diskCookies[i].getValue();
//					System.out.println(cookieValue);
//					isLogin = true;
//					break;
//				}
//			}
//		}
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
		excludeReg = config.getInitParameter("exclude");
		if (_loginPage != null)
		{
			loginPage = _loginPage;
		}
	}

	public void destroy()
	{

	}
}
