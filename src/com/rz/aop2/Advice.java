package com.rz.aop2;

public interface Advice
{
	void before();

	void after();

	void exception();

	void around();
}
