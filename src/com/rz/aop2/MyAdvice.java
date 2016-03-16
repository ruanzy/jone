package com.rz.aop2;

public class MyAdvice implements Advice
{

	public void before()
	{
		System.out.println("我是doBefore通知");
	}

	public void after()
	{
		System.out.println("我是doAfter通知");
	}

	public void exception()
	{
		System.out.println("我是doException通知");
	}

	public void around()
	{
		System.out.println("我是doFinally通知");
	}

}
