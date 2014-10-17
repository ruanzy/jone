package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
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
		WebUtil.getResponse().setContentType("text/javascript;charset=UTF-8");
		try
		{
			WebUtil.getResponse().getWriter().print(JSON.toJSONString(data));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
