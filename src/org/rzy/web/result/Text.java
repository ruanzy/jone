package org.rzy.web.result;

import java.io.IOException;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;

public class Text implements Result
{
	String txt;

	public Text(String txt)
	{
		this.txt = txt;
	}

	public void render()
	{
		try
		{
			WebUtil.getResponse().getWriter().write(txt);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
