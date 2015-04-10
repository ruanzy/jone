package com.rz.web.interceptor;

import com.rz.web.ActionInvocation;

public interface Interceptor
{
	void intercept(ActionInvocation invocation);
}
