package rzy.core;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

public class ServiceProxy
{
	static MethodInterceptor interceptor = new MethodInterceptor()
	{
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
		{
			Object result = null;
			try
			{
				Dao.begin();
				result = methodProxy.invokeSuper(obj, args);
				Dao.commit();
			}
			catch (Exception e)
			{
				Dao.rollback();
				throw e;
			}
			return result;
		}
	};

	static CallbackFilter filter = new CallbackFilter()
	{
		public int accept(Method arg0)
		{
			return Pattern.compile("^(add|del|mod|set|reg|active|cancel)").matcher(arg0.getName()).find() ? 0 : 1;
		}
	};

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> cls)
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
			return null;
		}
	}
}