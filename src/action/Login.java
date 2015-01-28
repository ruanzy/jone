package action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Msg;

public class Login
{
	static Logger log = LoggerFactory.getLogger(Login.class);

	public View execute()
	{
		String svc = WebUtil.getCaptcha();
		String vc = WebUtil.getParameter("vc");
		String username = WebUtil.getParameter("username");
		String password = WebUtil.getParameter("password");
		String go = WebUtil.getParameter("go");
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
			WebUtil.Session.attr("res", WebUtil.call("PmsService.userres", username));
		}
		WebUtil.setUserinfo(username + "_" + password);
		Object allres = WebUtil.Application.attr("allres");
		if (allres == null)
		{
			WebUtil.Application.attr("allres", WebUtil.call("PmsService.res"));
		}
		StringBuffer logs = new StringBuffer();
		String user = WebUtil.getUser();
		String ip = WebUtil.getIP();
		String ua = WebUtil.getHeader("User-Agent");
		logs.append(user).append("|");
		logs.append(ip).append("|");
		logs.append(ua);
		log.debug(logs.toString());
		if(go != null && go.length() > 0){
			return new Msg(true, "SSO?go=" + go);
		}
		return new Msg(true, "index.jsp");
	}
}
