package action;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Redirect;

public class Logout
{
	public Result execute()
	{
		WebUtil.Session.clear();
		WebUtil.Cookies.clear("SSOTOKEN");
		return new Redirect("login.jsp");
	}
}
