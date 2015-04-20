package com.rz.aop2;

public class MyAOPHandler implements AOPHandler
{

	public void doBefore()
	{
		System.out.println("我是doBefore通知");
	}

	public void doAfter()
	{
		System.out.println("我是doAfter通知");
	}

	public void doException()
	{
		System.out.println("我是doException通知");
	}

	public void doFinally()
	{
		System.out.println("我是doFinally通知");
	}

}
