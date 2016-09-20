package com.rz.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.util.WebUtil;

public class ServiceKit
{
	static Logger log = LoggerFactory.getLogger(ServiceKit.class);

	static MethodInterceptor interceptor = new MethodInterceptor()
	{
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
		{
			Object result = null;
			try
			{
				result = methodProxy.invokeSuper(obj, args);
				String operation = method.getAnnotation(Operation.class).value();
				StringBuffer log = new StringBuffer(); 
				String user = "";
				if("login".equals(method.getName())){
					user = WebUtil.getParameter("username");
				}else{
					user = WebUtil.getUser();
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String time = df.format(new Date());
				log.append(user).append("|");
				log.append(time).append("|");
				log.append(operation).append("|");
				log.append(operation);
				LogHandle.put(log.toString());
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