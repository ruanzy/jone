package interceptor;

import com.rz.web.Action;
import com.rz.web.Expression;
import com.rz.web.Interceptor;
import com.rz.web.WebUtil;

@Expression("klogin")
public class LoginInterceptor implements Interceptor
{

	public Object intercept(Action ai)
	{
		System.out.println("LoginInterceptor before");
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
		return null;
	}

}
