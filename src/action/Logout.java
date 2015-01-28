package action;

import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Redirect;

public class Logout
{
	public View execute()
	{
		WebUtil.Session.clear();
		WebUtil.Cookies.clear("SSOTOKEN");
		return new Redirect("login.jsp");
	}
}
