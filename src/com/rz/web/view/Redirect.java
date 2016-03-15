package com.rz.web.view;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.rz.web.ActionContext;
import com.rz.web.View;

public class Redirect implements View
{
	String url;

	public Redirect(String url)
	{
		this.url = url;
	}

	public void handle()
	{
		try
		{
			HttpServletResponse response = ActionContext.getResponse();
			response.sendRedirect(url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
