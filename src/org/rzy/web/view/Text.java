package org.rzy.web.view;

import java.io.IOException;
import org.rzy.web.ActionContext;
import org.rzy.web.View;

public class Text implements View
{
	String txt;

	public Text(String txt)
	{
		this.txt = txt;
	}

	public void render()
	{
		ActionContext.getActionContext().getHttpServletResponse().setContentType("text/plain;charset=UTF-8");
		try
		{
			ActionContext.getActionContext().getHttpServletResponse().getWriter().print(txt);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
