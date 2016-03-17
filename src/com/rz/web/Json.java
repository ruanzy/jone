package com.rz.web;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
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

	public void handle()
	{
		HttpServletResponse response = ActionContext.getResponse();
		response.setContentType("text/javascript;charset=UTF-8");
		try
		{
			response.getWriter().print(JSON.toJSONString(data));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
