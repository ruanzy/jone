package com.rz.interceptor;

import java.lang.reflect.Method;
import com.rz.web.ActionContext;
import com.rz.web.View;

public class ActionInvocation
{
	private Interceptor[] inters;
	private ActionContext actionContext;
	private int index = 0;

	public ActionContext getActionContext()
	{
		return actionContext;
	}

	public ActionInvocation(ActionContext ac)
	{
		this.actionContext = ac;
		String url = actionContext.getRequest().getServletPath();
		this.inters = Interceptors.match(url);
	}

	public void invoke() throws Exception
	{
		if (index < inters.length)
		{
			inters[index++].intercept(this);
		}
		else if (index++ == inters.length)
		{
			Object result = null;
			String url = actionContext.getRequest().getServletPath();
			String[] parts = url.substring(1).split("/");
			String _action = parts[0];
			String action = Character.toTitleCase(_action.charAt(0)) + _action.substring(1);
			String actionMethod = (parts.length > 1) ? parts[1] : "execute";
			Class<?> cls = Class.forName("action." + action);
			Method method = cls.getMethod(actionMethod);
			result = method.invoke(cls.newInstance());
			if (result instanceof View)
			{
				((View) result).render(actionContext);
			}
		}
	}
}
