package action;

import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Redirect;

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
