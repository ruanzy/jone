package com.rz.aop;

import java.lang.reflect.Method;

public abstract class BeforeHandler extends AbstractHandler {

	/**
	 * Handles before execution of actual method.
	 *
	 * @param proxy the proxy
	 * @param method the method
	 * @param args the args
	 */
	public abstract void handleBefore(Object proxy, Method method, Object[] args);
	

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		handleBefore(proxy, method, args);
		return method.invoke(getTargetObject(), args);
	}
}
