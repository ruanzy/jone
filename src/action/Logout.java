package action;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Page;

public class Logout
{
	public Result execute()
	{
		WebUtil.Session.clear();
		WebUtil.Cookies.clear();
		return new Page("login.html", true);
	}
}
