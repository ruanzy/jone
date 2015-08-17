package interceptor;

import com.rz.web.ActionInvocation;
import com.rz.web.interceptor.Interceptor;
import com.rz.web.interceptor.InterceptorPath;

@InterceptorPath("klogi")
public class LoginInterceptor2 implements Interceptor
{

	public void intercept(ActionInvocation ai)
	{
		System.out.println("LoginInterceptor2 before");
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
		System.out.println("LoginInterceptor2 after");
	}

}
