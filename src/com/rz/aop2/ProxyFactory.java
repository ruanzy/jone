package com.rz.aop2;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyFactory implements MethodInterceptor
{
	List<Advice> advices;

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

	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> cls, List<Advice> advices)
	{
		Enhancer enhancer = new Enhancer();
		this.advices = advices;
		enhancer.setSuperclass(cls);
		enhancer.setCallback(this);
		return (T) enhancer.create();
	}

	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
	{
		Object result = null;
		for (Advice advice : advices)
		{
			advice.before();
		}
		try
		{
			result = methodProxy.invokeSuper(proxy, args);
			for (Advice advice : advices)
			{
				advice.after();
			}
		}
		catch (Exception e)
		{
			for (Advice advice : advices)
			{
				advice.exception();
			}
		}
		finally
		{
			for (Advice advice : advices)
			{
				advice.around();
			}
		}
		return result;
	}
}