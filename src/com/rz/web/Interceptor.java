package com.rz.web;

import com.rz.web.ActionInvocation;

public interface Interceptor
{
	void intercept(ActionInvocation invocation);
}
