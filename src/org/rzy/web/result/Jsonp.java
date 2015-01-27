package org.rzy.web.result;

import java.io.IOException;
import org.rzy.web.ActionContext;
import org.rzy.web.Result;
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
