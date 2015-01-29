package com.rz.web.view;

import java.io.IOException;
import com.rz.web.ActionHandler;
import com.rz.web.View;

public class Text implements View
{
	String txt;

	public Text(String txt)
	{
		this.txt = txt;
	}

	public void render()
	{
		ActionHandler.getResponse().setContentType("text/plain;charset=UTF-8");
		try
		{
			ActionHandler.getResponse().getWriter().print(txt);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
