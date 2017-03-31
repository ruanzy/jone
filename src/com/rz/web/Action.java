package com.rz.web;

import java.lang.reflect.Method;
import java.util.List;

public class Action
{
	private Object _action;
	private Method _method;
	private List<Interceptor> inters;
	private int index = 0;

	Action(Object a, Method m)
	{
		this._action = a;
		this._method = m;
		String name = _action.getClass().getSimpleName();
		String method = _method.getName();
		this.inters = Container.findInterceptor(name, method);
	}

	public Object invoke() throws Exception
	{
		Object result = null;
		if (index < inters.size())
		{
			result = inters.get(index++).intercept(this);
		}
		else if (index++ == inters.size())
		{
			result = _method.invoke(_action);
		}
		return result;
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
