package com.rz.web;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import com.rz.web.ActionContext;

public class Result
{
	private int code;
	private String msg;
	private Object data;

	public Result()
	{

	}

	public Result(int code, String msg, Object data)
	{
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}
	
	public void render()
	{
		HttpServletResponse response = ActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");
		try
		{
			response.getWriter().print(JSON.toJSONString(this));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
