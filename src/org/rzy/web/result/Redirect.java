package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;

public class Redirect implements Result
{
	String url;

	public Redirect(String url)
	{
		this.url = url;
	}

	public void render()
	{
		WebUtil.redirect(url);
	}

}
