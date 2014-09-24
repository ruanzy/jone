package org.rzy.web.result;

import org.rzy.web.Context;
import org.rzy.web.Result;
import com.alibaba.fastjson.JSON;

public class Json implements Result
{
	Object o;

	public Json(Object o)
	{
		this.o = o;
	}

	public void render()
	{
		Context.getResponse().setContentType("text/javascript;charset=UTF-8");
		try
		{
			Context.getResponse().getWriter().print(JSON.toJSONString(o));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
