package com.rz.aop;

import java.util.ArrayList;
import java.util.List;

public class ProxyTest
{
	public static void main(String[] args)
	{
		ProxyUtil pu = ProxyUtil.getInstance();
		List<Advice> advices = new ArrayList<Advice>();
		advices.add(new MyAdvice());
		Business bs = (Business) pu.getProxy(new Business(), advices);
		bs.hello1("haha");
		bs.hello2("haha");
	}
}