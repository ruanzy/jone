package org.rzy.web.result;

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
		WebUtil.Response.setContentType("text/plain;charset=UTF-8");
		WebUtil.Response.write(txt);
	}

}
