package org.rzy.web.result;

import org.rzy.web.ResponseUtil;
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
		ResponseUtil.setContentType("text/plain;charset=UTF-8");
		ResponseUtil.write(txt);
	}

}
