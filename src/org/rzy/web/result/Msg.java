package org.rzy.web.result;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import com.alibaba.fastjson.JSON;

public class Msg implements Result
{
	boolean result = true;

	String msg;

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

	public Msg(String msg)
	{
		this.msg = msg;
	}

	public Msg(boolean result, String msg)
	{
		this.result = result;
		this.msg = msg;
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