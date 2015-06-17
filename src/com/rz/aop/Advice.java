package com.rz.aop;

import java.lang.reflect.Method;

public interface Advice
{
	void error(Method method, Object[] args, Exception e);

	void before(Method method, Object[] args);

	void after(Method method, Object[] args, Object retVal);
}
