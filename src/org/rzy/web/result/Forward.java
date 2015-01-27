package org.rzy.web.result;

import java.io.IOException;
import javax.servlet.ServletException;
import org.rzy.web.ActionContext;
import org.rzy.web.Result;

public class Forward implements Result
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
