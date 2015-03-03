package interceptor;

import com.rz.interceptor.ActionInvocation;
import com.rz.interceptor.Interceptor;
import com.rz.interceptor.InterceptorPath;
import com.rz.util.WebUtil;

@InterceptorPath("login")
public class LoginInterceptor implements Interceptor
{

	public void intercept(ActionInvocation ai)
	{
		System.out.println("LoginInterceptor before");
		System.out.println("action");
		try
		{
			ai.invoke();
//			Class<?> cls = Class.forName("action.HAction");
//			Method method = cls.getMethod("execute");
//			Object result = method.invoke(cls.newInstance());
//			if (result instanceof View)
//			{
//				((View) result).render(ai.getActionContext());
//			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("LoginInterceptor after");
		StringBuffer logs = new StringBuffer();
		String user = WebUtil.getUser();
		String ip = WebUtil.getIP();
		String ua = WebUtil.getHeader("User-Agent");
		logs.append(user).append("|");
		logs.append(ip).append("|");
		logs.append(ua);
		System.out.println(logs.toString());
	}

}
