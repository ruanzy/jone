package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebContext;
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
		WebContext.getResponse().setContentType("text/javascript;charset=UTF-8");
		try
		{
			WebContext.getResponse().getWriter().print(JSON.toJSONString(data));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
