package com.rz.web.view;

import java.io.IOException;
import javax.servlet.ServletException;
import com.rz.web.ActionHandler;
import com.rz.web.View;

public class Forward implements View
{
	String url;

	public Forward(String url)
	{
		this.url = url;
	}

	public void render()
	{
		try
		{
			ActionHandler.getRequest().getRequestDispatcher(url).forward(ActionHandler.getRequest(), ActionHandler.getResponse());
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
