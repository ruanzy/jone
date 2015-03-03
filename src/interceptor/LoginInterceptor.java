package interceptor;

import com.rz.interceptor.ActionInvocation;
import com.rz.interceptor.Interceptor;
import com.rz.interceptor.InterceptorPath;

@InterceptorPath("login")
public class LoginInterceptor implements Interceptor
{

	public String intercept(ActionInvocation ai)
	{
		return null;
	}

}
