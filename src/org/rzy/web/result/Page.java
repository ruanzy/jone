package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;

public class Page implements Result
{
	String page;

	boolean redirect;

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
				String basePath = WebUtil.getRequest().getScheme() + "://" + WebUtil.getRequest().getServerName() + ":"
						+ WebUtil.getRequest().getServerPort() + WebUtil.getRequest().getContextPath() + "/";
				WebUtil.getResponse().sendRedirect(basePath + page);
			}
			else
			{
				WebUtil.getRequest().getRequestDispatcher(page).forward(WebUtil.getRequest(), WebUtil.getResponse());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
