package action;

import org.rzy.web.View;
import org.rzy.web.WebUtil;
import org.rzy.web.view.Redirect;

public class Logout
{
	public View execute()
	{
		WebUtil.Session.clear();
		WebUtil.Cookies.clear("SSOTOKEN");
		return new Redirect("login.jsp");
	}
}
