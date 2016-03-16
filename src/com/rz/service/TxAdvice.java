package com.rz.service;

import com.rz.aop2.Advice;
import com.rz.dao.DB;

public class TxAdvice implements Advice
{
	DB dao = DB.getInstance();

	public void before()
	{
		dao.begin();
	}

	public void after()
	{
		dao.commit();
	}

	public void exception()
	{
		dao.rollback();
	}

	public void around()
	{
		// TODO Auto-generated method stub

	}

}
