package action;

import org.rzy.util.WebUtil;
import org.rzy.web.View;
import org.rzy.web.view.Redirect;

public class SSO
{
	public View execute()
	{
		String tk = WebUtil.Cookies.get("SSOTOKEN");
		String go = WebUtil.getParameter("go");
		String path = "http://11.0.0.106:8088/LogStat/setCookie?tk=" + tk + "&go=" + go;
		return new Redirect(path);
	}
}
