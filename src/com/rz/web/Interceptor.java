package com.rz.web;

public interface Interceptor
{
	Object intercept(Action action) throws Exception;
}
