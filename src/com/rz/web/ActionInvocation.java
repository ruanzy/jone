package com.rz.web;

import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.web.interceptor.Interceptor;

public class ActionInvocation
{
	private Logger log = LoggerFactory.getLogger(ActionInvocation.class);
	private String action;
	private String method;
	private List<Interceptor> inters;
	private int index = 0;

	public ActionInvocation()
	{
		try
		{
			String url = ActionContext.getRequest().getServletPath();
			String[] parts = url.substring(1).split("/");
			this.action = WebKit.capitalize(parts[0]);
			this.method = (parts.length > 1) ? parts[1] : "execute";
			this.inters = Container.getInterceptor(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void invoke()
	{
		try
		{
//			String _m = actionContext.getRequest().getMethod();
//			if ("OPTIONS".equalsIgnoreCase(_m))
//			{
//				actionContext.getResponse().addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
//				actionContext.getResponse().addHeader("Access-Control-Allow-Headers",
//						"X-Requested-With, Content-Type, Accept");
//				actionContext.getResponse().addHeader("Access-Control-Max-Age", "1728000");
//				actionContext.getResponse().setStatus(204);
//			}
			if (index < inters.size())
			{
				inters.get(index++).intercept(this);
			}
			else if (index++ == inters.size())
			{
				Object result = null;
				//String ip = actionContext.getRequest().getRemoteAddr();
				Object a = Container.getAction(action);
				Method m = a.getClass().getMethod(method);
				Object[] ps = new Object[] { action, method };
				log.debug("Action={}, method={}", ps);
				result = m.invoke(a);
				if (result instanceof View)
				{
					((View) result).handle();
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
