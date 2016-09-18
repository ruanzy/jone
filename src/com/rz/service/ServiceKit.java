package com.rz.service;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

public class ServiceKit
{
	static MethodInterceptor interceptor = new MethodInterceptor()
	{
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
		{
			Object result = null;
			try
			{
				System.out.println("Operation= " + method.getAnnotation(Operation.class).value());
				result = methodProxy.invokeSuper(obj, args);
			}
			catch (Exception e)
			{
				throw e;
			}
			return result;
		}
	};

	static CallbackFilter filter = new CallbackFilter()
	{
		public int accept(Method method)
		{
			if (method.isAnnotationPresent(Operation.class))
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	};

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<? extends T> cls) throws Exception
	{
		try
		{
			Enhancer en = new Enhancer();
			en.setSuperclass(cls);
			en.setCallbacks(new Callback[] { interceptor, NoOp.INSTANCE });
			en.setCallbackFilter(filter);
			return (T) en.create();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
}