package org.rzy.web;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Pattern;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import org.rzy.dao.Dao;
import org.rzy.web.log.Log;
import org.rzy.web.log.LogHandler;

class ServiceProxy
{
	static Properties config = new Properties();
	static
	{
		InputStream is = null;
		try
		{
			is = ServiceProxy.class.getClassLoader().getResourceAsStream("service.properties");
			if (is == null)
			{
				is = new FileInputStream("service.properties");
			}
			config.load(is);
		}
		catch (Exception e)
		{

		}
	}
	static MethodInterceptor interceptor = new MethodInterceptor()
	{
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
		{
			Object result = null;
			Dao dao = Dao.getInstance();
			String pcls = obj.getClass().getSimpleName();
			String sid = pcls.split("\\$\\$")[0] + "." + method.getName();
			Log log = new Log(sid, args);
			try
			{
				dao.begin();
				result = methodProxy.invokeSuper(obj, args);
				dao.commit();
				log.setResult(true);
			}
			catch (Exception e)
			{
				dao.rollback();
				log.setResult(false);
				throw e;
			}
			finally
			{
				String logHandlerName = config.getProperty("logHandler");
				if (logHandlerName != null)
				{
					LogHandler logHandler = (LogHandler) Class.forName(logHandlerName).newInstance();
					logHandler.handler(log);
				}
			}
			return result;
		}
	};

	static CallbackFilter filter = new CallbackFilter()
	{
		public int accept(Method arg0)
		{
			String express = config.getProperty("express", "^(add|delete|update)");
			return Pattern.compile(express).matcher(arg0.getName()).find() ? 0 : 1;
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