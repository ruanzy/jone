package org.rzy.web.view;

import java.io.IOException;
import javax.servlet.ServletException;
import org.rzy.web.ActionContext;
import org.rzy.web.View;

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
			ActionContext.getActionContext().getHttpServletRequest().getRequestDispatcher(url).forward(ActionContext.getActionContext().getHttpServletRequest(), ActionContext.getActionContext().getHttpServletResponse());
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
