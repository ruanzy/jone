package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;

public class Forward implements Result
{
	String url;

	boolean redirect;

	public Forward(String url)
	{
		this.url = url;
	}

	public void render()
	{

		WebUtil.forward(url);
	}

}
