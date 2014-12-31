package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;

public class Page implements Result
{
	String page;

	boolean redirect;

	public Page(String page)
	{
		this.page = page;
	}

	public Page(String page, boolean redirect)
	{
		this.page = page;
		this.redirect = redirect;
	}

	public void render()
	{
		try
		{

			if (redirect)
			{
				WebUtil.redirect(page);
			}
			else
			{
				WebUtil.forward(page);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
