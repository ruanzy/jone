package com.rz.aop2;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
		List<Advice> advices = new ArrayList<Advice>();
		advices.add(new MyAdvice());
		advices.add(new MyAdvice2());
		UserService userService = pf.create(UserService.class, advices);
		userService.save("abc");
	}

}
