package org.rzy.web;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import org.rzy.dao.Dao;

class ServiceProxy
{
	static MethodInterceptor interceptor = new MethodInterceptor()
	{
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
		{
			Object result = null;
			Dao dao = Dao.getInstance();
			try
			{
				dao.begin();
				result = methodProxy.invokeSuper(obj, args);
				dao.commit();
			}
			catch (Exception e)
			{
				dao.rollback();
				throw e;
			}
			return result;
		}
	};

	static CallbackFilter filter = new CallbackFilter()
	{
		public int accept(Method method)
		{	
			if(method.isAnnotationPresent(Transaction.class)){
				return 0;
			}else{
				return 1;
			}
		}
	};

	public static Object get(String className)
	{
		try
		{
			Class<?> cls = Class.forName(className);
			Enhancer en = new Enhancer();
			en.setSuperclass(cls);
			en.setCallbacks(new Callback[] { interceptor, NoOp.INSTANCE });
			en.setCallbackFilter(filter);
			return en.create();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}