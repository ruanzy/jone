package com.rz.web;

import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Action
{
	private Logger log = LoggerFactory.getLogger(Action.class);
	private String name;
	private Object _action;
	private String method;
	private Method _method;
	private List<Interceptor> inters;
	private int index = 0;

	Action(String name, String method)
	{
		this.name = name;
		this.method = method;
		this.inters = Container.findInterceptor(name, method);
	}

	private void init() throws Exception
	{
		Object a = Container.findAction(name);
		if (a == null)
		{
			String error = "Action " + name + " not found!";
			log.error(error);
			throw new ClassNotFoundException(error);
		}
		this._action = a;
		Method m = a.getClass().getMethod(method);
		if (m == null)
		{
			String error = "Method " + method + " not found!";
			log.error(error);
			throw new NoSuchMethodException(error);
		}
		this._method = m;
	}

	public void invoke() throws Exception
	{
		try
		{
			init();
		}
		catch (Exception e)
		{
			throw e;
		}
		if (index < inters.size())
		{
			inters.get(index++).intercept(this);
		}
		else if (index++ == inters.size())
		{
			Object[] ps = new Object[] { name, method };
			log.debug("Action={}, method={}", ps);
			Object result = null;
			try
			{
				Method m = _action.getClass().getMethod(method);
				result = m.invoke(_action);
				if (result instanceof View)
				{
					((View) result).handle();
				}
			}
			catch (Exception e)
			{
				throw e;
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public Object getAction()
	{
		return _action;
	}

	public Method getMethod()
	{
		return _method;
	}
}
