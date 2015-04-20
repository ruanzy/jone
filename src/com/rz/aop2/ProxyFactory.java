package com.rz.aop2;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyFactory implements MethodInterceptor
{
	private Enhancer enhancer = new Enhancer();
	AOPHandler handler;

	public Object create(Class<?> clazz, AOPHandler handler)
	{
		this.handler = handler;
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		return enhancer.create();
	}

	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
	{
		Object result = null;
		handler.doBefore();
		try
		{
			result = methodProxy.invokeSuper(proxy, args);
			handler.doAfter();
		}
		catch (Exception e)
		{
			handler.doException();
		}
		finally
		{
			handler.doFinally();
		}
		return result;
	}
}