package com.rz.web.interceptor;

import com.rz.util.WebUtil;
import com.rz.web.ActionInvocation;

@InterceptorPath("*")
public class ExceptionInterceptor implements Interceptor
{

	public void intercept(ActionInvocation ai)
	{
		System.out.println("LoginInterceptor before");
		try
		{
			ai.invoke();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("LoginInterceptor after");
		StringBuffer logs = new StringBuffer();
		String user = WebUtil.getUser();
		String ip = WebUtil.getIP();
		String ua = WebUtil.getHeader("User-Agent");
		logs.append(user).append("|");
		logs.append(ip).append("|");
		logs.append(ua);
		System.out.println(logs.toString());
	}

}
