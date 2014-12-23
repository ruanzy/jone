package org.rzy.web.result;

import org.rzy.web.ResponseUtil;
import org.rzy.web.Result;

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
				ResponseUtil.redirect(page);
			}
			else
			{
				ResponseUtil.forward(page);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
