package com.rz.web.view;

import java.io.IOException;
import javax.servlet.ServletException;
import com.rz.web.ActionContext;
import com.rz.web.View;

public class Forward implements View
{
	String url;

	public Forward(String url)
	{
		this.url = url;
	}

	public void render(ActionContext ac)
	{
		try
		{
			ac.getRequest().getRequestDispatcher(url).forward(ac.getRequest(), ac.getResponse());
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
