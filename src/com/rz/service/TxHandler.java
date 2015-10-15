package com.rz.service;

import com.rz.aop2.AOPHandler;
import com.rz.dao.DB;

public class TxHandler implements AOPHandler
{
	DB dao = DB.getInstance();

	public void doBefore()
	{
		dao.begin();
	}

	public void doAfter()
	{
		dao.commit();
	}

	public void doException()
	{
		dao.rollback();
	}

	public void doFinally()
	{
		// TODO Auto-generated method stub

	}

}
