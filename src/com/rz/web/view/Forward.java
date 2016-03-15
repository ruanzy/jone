package com.rz.web.view;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rz.web.ActionContext;
import com.rz.web.View;

public class Forward implements View
{
	String url;

	public Forward(String url)
	{
		this.url = url;
	}

	public void handle()
	{
		try
		{
			HttpServletRequest request = ActionContext.getRequest();
			HttpServletResponse response = ActionContext.getResponse();
			request.getRequestDispatcher(url).forward(request, response);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ServletException e)
		{
			e.printStackTrace();
		}
	}

}
