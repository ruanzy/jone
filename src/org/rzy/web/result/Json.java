package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import com.alibaba.fastjson.JSON;

public class Json implements Result
{
	boolean result = true;

	String msg;

	Object data;

	public boolean isResult()
	{
		return result;
	}

	public void setResult(boolean result)
	{
		this.result = result;
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

	public Json(boolean result, String msg, Object data)
	{
		this.result = result;
		this.msg = msg;
		this.data = data;
	}

	public Json(boolean result, String msg)
	{
		this.result = result;
		this.msg = msg;
	}

	public Json(Object data)
	{
		this.data = data;
	}

	public void render()
	{
		WebUtil.getResponse().setContentType("text/javascript;charset=UTF-8");
		try
		{
			WebUtil.getResponse().getWriter().print(JSON.toJSONString(this));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
