package com.rz.aop2;

public class MyAdvice2 implements Advice
{

	public void before()
	{
		System.out.println("我是doBefore通知2");
	}

	public void after()
	{
		System.out.println("我是doAfter通知2");
	}

	public void exception()
	{
		System.out.println("我是doException通知2");
	}

	public void around()
	{
		System.out.println("我是doFinally通知2");
	}

}
