package org.rzy.web.result;

import org.rzy.web.Context;
import org.rzy.web.Result;
import com.alibaba.fastjson.JSON;

public class Json implements Result
{
	Object data;

	public Json(Object data)
	{
		this.data = data;
	}

	public void render()
	{
		Context.getResponse().setContentType("text/javascript;charset=UTF-8");
		try
		{
			Context.getResponse().getWriter().print(JSON.toJSONString(data));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
