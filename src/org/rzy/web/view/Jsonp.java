package org.rzy.web.view;

import java.io.IOException;
import org.rzy.web.ActionContext;
import org.rzy.web.View;
import com.alibaba.fastjson.JSON;

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
		ActionContext.getActionContext().getHttpServletResponse().setContentType("text/javascript;charset=UTF-8");
		try
		{
			ActionContext.getActionContext().getHttpServletResponse().getWriter().print(callback + "(" + JSON.toJSONString(data) + ")");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
