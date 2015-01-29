package com.rz.web;

import java.util.ArrayList;
import java.util.List;

public class InterceptorChain
{
	private final List<Interceptor> interceptors = new ArrayList<Interceptor>();
	private int index = 0;
	private boolean isPass = false;

	boolean isPass()
	{
		return isPass;
	}

	public InterceptorChain(Interceptor[] interceptors, String url)
	{
		for (Interceptor itcp : interceptors)
		{
			if (true)
			{
				this.interceptors.add(itcp);
			}
		}
	}

	public void doInterceptor(ActionHandler ah) throws Exception
	{
		if (index == interceptors.size())
		{
			this.isPass = true;
		}
		else
		{
			index++;
			interceptors.get(index - 1).intercept(ah, this);
		}
	}
}