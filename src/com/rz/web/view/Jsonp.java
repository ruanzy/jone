package com.rz.web.view;

import java.io.IOException;
import com.alibaba.fastjson.JSON;
import com.rz.web.ActionHandler;
import com.rz.web.View;

public class Jsonp implements View
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
		ActionHandler.getResponse().setContentType("text/javascript;charset=UTF-8");
		try
		{
			ActionHandler.getResponse().getWriter().print(callback + "(" + JSON.toJSONString(data) + ")");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
