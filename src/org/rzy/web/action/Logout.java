package org.rzy.web.action;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Page;

public class Logout
{
	public Result execute()
	{
		WebUtil.clearSession();
		return new Page("login.html", true);
	}
}
