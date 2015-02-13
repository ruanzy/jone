package com.rz.web.view;

import java.io.IOException;
import com.alibaba.fastjson.JSON;
import com.rz.web.ActionContext;
import com.rz.web.View;

public class Json implements View
{
	Object data;

	public Json(Object data)
	{
		this.data = data;
	}

	public void render(ActionContext ac)
	{
		ac.getResponse().setContentType("text/javascript;charset=UTF-8");
		try
		{
			ac.getResponse().getWriter().print(JSON.toJSONString(data));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
