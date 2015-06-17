package com.rz.aop;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyUtil implements MethodInterceptor
{

	private List<Advice> advices;

	private ProxyUtil()
	{
	}

	private static class SingletonHolder
	{
		private final static ProxyUtil INSTANCE = new ProxyUtil();
	}

	public static ProxyUtil getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	public Object getProxy(Object target, List<Advice> advices)
	{
		this.advices = advices;
		if (target == null)
			throw new NullPointerException("目标对象不能为空");
		Enhancer en = new Enhancer();
		en.setSuperclass(target.getClass());
		en.setCallback(this);
		return en.create();
	}

	public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws java.lang.Throwable
	{
		Object rvt = null;
		try
		{
			for (Advice advice : advices)
			{
				advice.before(method, args);
			}
			rvt = proxy.invokeSuper(target, args);
			for (Advice advice : advices)
			{
				advice.after(method, args, rvt);
			}
		}
		catch (Exception e)
		{
			for (Advice advice : advices)
			{
				advice.error(method, args, e);
			}
			e.printStackTrace();
		}
		return rvt;
	}
}