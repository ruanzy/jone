package com.rz.interceptor;

public interface Interceptor
{
	String intercept(ActionInvocation invocation) throws Exception;
}
