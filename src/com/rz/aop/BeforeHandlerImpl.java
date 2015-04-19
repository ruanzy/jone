package com.rz.aop;

import java.lang.reflect.Method;

public class BeforeHandlerImpl extends BeforeHandler {

	/* (non-Javadoc)
	 * @see com.ddlab.rnd.aop.BeforeHandler#handleBefore(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public void handleBefore(Object proxy, Method method, Object[] args) {
		//Provide your own cross cutting concern
		System.out.println("Handling before actual method execution ........");
	}
}