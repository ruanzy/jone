package action;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Msg;

public class Login
{
	public Result execute()
	{
		String svc = WebUtil.getVC();
		String vc = WebUtil.getParameter("vc");
		String username = WebUtil.getParameter("username");
		String password = WebUtil.getParameter("password");
		if (!svc.equalsIgnoreCase(vc))
		{
			return new Msg(false, "验证码不正确!");
		}
		if (!WebUtil.isAdmin(username, password))
		{
			Object user = WebUtil.call("PmsService.login", username, password);
			if (user == null)
			{
				return new Msg(false, "用户名或密码错误!");
			}
			WebUtil.attr("res", WebUtil.call("PmsService.userres", username), "session");
		}
		WebUtil.setUser(username);
		Object allres = WebUtil.attr("allres", "application");
		if (allres == null)
		{
			WebUtil.attr("allres", WebUtil.call("PmsService.res"), "application");
		}
		return new Msg(true, "login success");
	}
}
