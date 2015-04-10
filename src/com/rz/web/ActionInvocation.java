package com.rz.web;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.web.interceptor.Interceptor;
import com.rz.web.interceptor.Interceptors;

public class ActionInvocation
{
	private Logger log = LoggerFactory.getLogger(ActionInvocation.class);
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

	public void invoke()
	{
		try
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
				String ip = actionContext.getRequest().getRemoteAddr();
				String m = actionContext.getRequest().getMethod();
				Object[] ps = new Object[] { ip, m, url };
				log.debug("{} {} {}", ps);
				result = method.invoke(cls.newInstance());
				if (result instanceof View)
				{
					((View) result).render(actionContext);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
