package com.rz.aop2;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyFactory implements MethodInterceptor
{
	List<AOPHandler> handlers;

	private ProxyFactory()
	{

	}

	private static class SingletonHolder
	{
		private final static ProxyFactory INSTANCE = new ProxyFactory();
	}

	public static ProxyFactory getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	public Object create(Class<?> clazz, List<AOPHandler> handlers)
	{
		Enhancer enhancer = new Enhancer();
		this.handlers = handlers;
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		return enhancer.create();
	}

	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
	{
		Object result = null;
		for (AOPHandler handler : handlers)
		{
			handler.doBefore();
		}
		try
		{
			result = methodProxy.invokeSuper(proxy, args);
			for (AOPHandler handler : handlers)
			{
				handler.doAfter();
			}
		}
		catch (Exception e)
		{
			for (AOPHandler handler : handlers)
			{
				handler.doException();
			}
		}
		finally
		{
			for (AOPHandler handler : handlers)
			{
				handler.doFinally();
			}
		}
		return result;
	}
}