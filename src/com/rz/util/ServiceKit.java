package com.rz.util;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

public class ServiceKit
{
	static Logger log = LoggerFactory.getLogger(WebUtil.class);
	
	static MethodInterceptor interceptor = new MethodInterceptor()
	{
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
		{
			Object result = null;
			try
			{
				result = methodProxy.invokeSuper(obj, args);
				String operation = method.getAnnotation(Operation.class).value();
				log.debug(operation);
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
	public static <T> T get(Class<? extends T> cls)
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
		}
		return null;
	}
}