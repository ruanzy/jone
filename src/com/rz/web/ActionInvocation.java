package com.rz.web;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.util.StringUtils;
import com.rz.web.interceptor.Interceptor;

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
		this.inters = Container.getInterceptor(url);
	}

	public void invoke()
	{
		try
		{
			String m = actionContext.getRequest().getMethod();
			if ("OPTIONS".equalsIgnoreCase(m))
			{
				actionContext.getResponse().addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
				actionContext.getResponse().addHeader("Access-Control-Allow-Headers",
						"X-Requested-With, Content-Type, Accept");
				actionContext.getResponse().addHeader("Access-Control-Max-Age", "1728000");
				actionContext.getResponse().setStatus(204);
			}
			if (index < inters.length)
			{
				inters[index++].intercept(this);
			}
			else if (index++ == inters.length)
			{
				Object result = null;
				String url = actionContext.getRequest().getServletPath();
				String[] parts = url.substring(1).split("/");
				String actionName = StringUtils.capitalize(parts[0]);
				String actionMethod = (parts.length > 1) ? parts[1] : "execute";
				// Class<?> cls = Class.forName("action." + action);
				// Method method = cls.getMethod(actionMethod);
				Object action = Container.getAction(actionName);
				Method method = action.getClass().getMethod(actionMethod);
				String ip = actionContext.getRequest().getRemoteAddr();
				Object[] ps = new Object[] { ip, m, url };
				log.debug("{} {} {}", ps);
				result = method.invoke(action);
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
