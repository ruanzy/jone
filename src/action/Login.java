package action;

import org.rzy.web.ApplicationUtil;
import org.rzy.web.RequestUtil;
import org.rzy.web.Result;
import org.rzy.web.SessionUtil;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login
{
	static Logger log = LoggerFactory.getLogger(Login.class);

	public Result execute()
	{
		String svc = SessionUtil.getCaptcha();
		String vc = RequestUtil.getParameter("vc");
		String username = RequestUtil.getParameter("username");
		String password = RequestUtil.getParameter("password");
		if (!svc.equalsIgnoreCase(vc))
		{
			return new Msg(false, "验证码不正确!");
		}
		if (!RequestUtil.isAdmin(username, password))
		{
			Object user = WebUtil.call("PmsService.login", username, password);
			if (user == null)
			{
				return new Msg(false, "用户名或密码错误!");
			}
			SessionUtil.attr("res", WebUtil.call("PmsService.userres", username));
		}
		SessionUtil.setUser(username);
		Object allres = ApplicationUtil.attr("allres");
		if (allres == null)
		{
			ApplicationUtil.attr("allres", WebUtil.call("PmsService.res"));
		}
		StringBuffer logs = new StringBuffer();
		String user = SessionUtil.getUser();
		String ip = RequestUtil.getIP();
		String ua = RequestUtil.getUserAgent();
		logs.append(user).append("|");
		logs.append(ip).append("|");
		logs.append(ua);
		log.debug(logs.toString());
		return new Msg(true, "login success");
	}
}
