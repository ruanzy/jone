package com.rz.aop2;

public class MyAOPHandler2 implements AOPHandler
{

	public void doBefore()
	{
		System.out.println("我是doBefore通知2");
	}

	public void doAfter()
	{
		System.out.println("我是doAfter通知2");
	}

	public void doException()
	{
		System.out.println("我是doException通知2");
	}

	public void doFinally()
	{
		System.out.println("我是doFinally通知2");
	}

}
