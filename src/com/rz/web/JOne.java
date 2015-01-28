package com.rz.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JOne implements Filter
{
	private ServletContext context;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getServletPath();
		boolean isStatic = url.lastIndexOf(".") != -1;
		if(isStatic){
			chain.doFilter(request, response);
			return;
		}
		new ActionHandler(context).handle(request, response);
	}

	public void destroy()
	{
	}

	public void init(FilterConfig cfg) throws ServletException
	{
		this.context = cfg.getServletContext();
		StringBuffer sb = new StringBuffer();
		sb.append("*************************************").append("\r\n");
		sb.append("**                                 **").append("\r\n");
		sb.append("**          JOne Satrting...       **").append("\r\n");
		sb.append("**                                 **").append("\r\n");
		sb.append("*************************************");
		System.out.println(sb);
		Plugins.init(this.context);
	}
}