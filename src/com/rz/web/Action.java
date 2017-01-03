package com.rz.web;

import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Action
{
	private Logger log = LoggerFactory.getLogger(Action.class);
	private String name;
	private String method;
	private List<Interceptor> inters;
	private int index = 0;

	Action(String name, String method)
	{
		this.name = name;
		this.method = method;
		this.inters = Container.findInterceptor(name, method);
	}

	public void invoke() throws Exception
	{
		if (index < inters.size())
		{
			inters.get(index++).intercept(this);
		}
		else if (index++ == inters.size())
		{
			Object[] ps = new Object[] { name, method };
			log.debug("Action={}, method={}", ps);
			Object result = null;
			try {
				Object a = Container.findAction(name);
				if(a == null){
					String error = "Action " + name + " not found!";
					log.error(error);
					throw new ClassNotFoundException(error);
				}
				Method m = a.getClass().getMethod(method);
				if(m == null){
					String error = "Method " + method + " not found!";
					log.error(error);
					throw new NoSuchMethodException(error);
				}
				result = m.invoke(a);
				if (result instanceof View)
				{
					((View) result).handle();
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public String getMethod()
	{
		return method;
	}
}
