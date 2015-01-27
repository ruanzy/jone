package org.rzy.web.result;

import java.io.IOException;
import org.rzy.web.ActionContext;
import org.rzy.web.Result;

public class Text implements Result
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
