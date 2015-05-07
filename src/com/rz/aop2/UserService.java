package com.rz.aop2;

import com.rz.dao.Dao;

public class UserService
{
	Dao dao = Dao.getInstance();
	
	public void save(String name)
	{
		System.out.println("我是save()方法");
	}
	
	public void update(String name)
	{
		System.out.println("我是update()方法");
	}
}