package jone.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CorsFilter implements Filter
{
	String[] allowHeaders = new String[] { "Content-Type", "X-Requested-With" };

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String method = request.getMethod();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", StringUtils.join(allowHeaders));
		if ("OPTIONS".equals(method))
		{
			response.getWriter().write("CORS");
			return;
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException
	{
	}

	public void destroy()
	{
	}
}
