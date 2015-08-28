package com.rz.web.view;

import java.io.IOException;
import com.rz.web.ActionContext;
import com.rz.web.View;

public class Html implements View
{
	String txt;

	public Html(String txt)
	{
		this.txt = txt;
	}

	public void render(ActionContext ac)
	{
		ac.getResponse().setContentType("text/html;charset=UTF-8");
		try
		{
			ac.getResponse().getWriter().print(txt);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
