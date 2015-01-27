package org.rzy.web.result;

import java.io.IOException;
import org.rzy.web.ActionContext;
import org.rzy.web.Result;

public class Redirect implements Result
{
	String url;

	public Redirect(String url)
	{
		this.url = url;
	}

	public void render()
	{
		try
		{
			ActionContext.getActionContext().getHttpServletResponse().sendRedirect(url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
