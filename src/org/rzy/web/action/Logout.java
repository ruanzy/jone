package org.rzy.web.action;

import org.rzy.web.Result;
import org.rzy.web.SessionUtil;
import org.rzy.web.result.Page;

public class Logout
{
	public Result execute()
	{
		SessionUtil.clear();
		return new Page("login.html", true);
	}
}
