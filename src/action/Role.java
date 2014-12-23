package action;

import java.util.HashMap;
import java.util.Map;
import org.rzy.web.RequestUtil;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Json;
import org.rzy.web.result.Msg;

public class Role
{
	public Result list()
	{
		Map<String, String> map = RequestUtil.getParameters();
		return new Json(WebUtil.call("PmsService.findrole", map));
	}

	public Result add()
	{
		String name = RequestUtil.getParameter("name");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		WebUtil.call("PmsService.addrole", map);
		return new Msg("add success");
	}

	public String del()
	{
		String id = RequestUtil.getParameter("id");
		WebUtil.call("PmsService.delroles", id);
		return null;
	}

	public Result allres()
	{
		return new Json(WebUtil.call("PmsService.getRes"));
	}

	public Result ownres()
	{
		String role = RequestUtil.getParameter("role");
		return new Json(WebUtil.call("PmsService.roleres", role));
	}

	public Result setres()
	{
		String role = RequestUtil.getParameter("role");
		String res = RequestUtil.getParameter("res");
		WebUtil.call("PmsService.setres", role, res);
		return new Msg("设置资源ok...");
	}
}
