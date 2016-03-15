package com.rz.web.view;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.rz.web.ActionContext;
import com.rz.web.View;

public class Text implements View
{
	String txt;

	public Text(String txt)
	{
		this.txt = txt;
	}

	public void handle()
	{
		HttpServletResponse response = ActionContext.getResponse();
		response.setContentType("text/plain;charset=UTF-8");
		try
		{
			response.getWriter().print(txt);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
