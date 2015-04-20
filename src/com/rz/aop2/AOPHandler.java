package com.rz.aop2;

public interface AOPHandler
{
	void doBefore();

	void doAfter();

	void doException();

	void doFinally();
}
