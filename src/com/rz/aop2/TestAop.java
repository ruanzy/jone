package com.rz.aop2;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.MethodUtils;

public class TestAop
{

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException
	{
		ProxyFactory pf = new ProxyFactory();
		Object proxy = pf.create(UserService.class, new MyAOPHandler());
		Object r = null;
		r = MethodUtils.invokeMethod(proxy, "save", new Object[] { "abc" });
		System.out.println("FInal Result :::" + r);

	}

}
