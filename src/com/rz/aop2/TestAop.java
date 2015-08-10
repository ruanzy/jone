package com.rz.aop2;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
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
		ProxyFactory pf = ProxyFactory.getInstance();
		List<AOPHandler> handlers = new ArrayList<AOPHandler>();
		handlers.add(new MyAOPHandler());
		handlers.add(new MyAOPHandler2());
		Object proxy = pf.create(UserService.class, handlers);
		Object r = null;
		r = MethodUtils.invokeMethod(proxy, "save", new Object[] { "abc" });
		System.out.println("FInal Result :::" + r);

	}

}
