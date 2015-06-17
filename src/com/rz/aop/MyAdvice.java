package com.rz.aop;

import java.lang.reflect.Method;

public class MyAdvice implements Advice
{

	public void error(Method method, Object[] args, Exception e)
	{
		System.out.println("error:" + e.getMessage());
	}

	public void before(Method method, Object[] args)
	{
		System.out.println("before:" + method.getName());
	}

	public void after(Method method, Object[] args, Object retVal)
	{
		System.out.println("after:" + retVal);
	}

}
