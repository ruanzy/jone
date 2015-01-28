package com.rz.web.view;

import java.io.IOException;
import com.rz.web.ActionContext;
import com.rz.web.View;

public class Redirect implements View
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
