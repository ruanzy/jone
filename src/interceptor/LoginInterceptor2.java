package interceptor;

import com.rz.web.Action;
import com.rz.web.Interceptor;
import com.rz.web.InterceptorPath;

@InterceptorPath("klogi")
public class LoginInterceptor2 implements Interceptor
{

	public void intercept(Action ai)
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
