package action;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Page;

public class SSO
{
	public Result execute()
	{
		String tk = WebUtil.Cookies.get("SSOTOKEN");
		String go = WebUtil.getParameter("go");
		if (tk != null)
		{
			String path = "http://11.0.0.106:8088/LogStat/setCookie?tk=" + tk + "&go=" + go;
			return new Page(path, true);
		}
		else
		{
			return new Page("login.jsp?go=" + go, true);
		}
	}
}
