package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import com.alibaba.fastjson.JSON;

public class Jsonp implements Result
{
	String callback;
	Object data;

	public Jsonp(String callback, Object data)
	{
		this.callback = callback;
		this.data = data;
	}

	public void render()
	{
		WebUtil.Response.setContentType("text/javascript;charset=UTF-8");
		try
		{
			WebUtil.Response.write(callback + "(" + JSON.toJSONString(data) + ")");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
