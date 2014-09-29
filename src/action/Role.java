package action;

import java.util.HashMap;
import java.util.Map;

import org.rzy.web.Context;
import org.rzy.web.Result;
import org.rzy.web.result.Json;
import org.rzy.web.util.XUtil;

public class Role
{
	public Result list()
	{
		Map<String, String> map = Context.getParameters();
		return new Json(XUtil.call("PmsService.findrole", map));
	}

	public String add()
	{
		String name = Context.getParameter("name");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		XUtil.call("PmsService.addrole", map);
		XUtil.ok();
		return null;
	}
	
	public String del()
	{
		String id = Context.getParameter("id");
		XUtil.call("PmsService.delroles", id);
		return null;
	}

	public Result allres()
	{
		return new Json(XUtil.call("PmsService.getRes"));
	}

	public Result ownres()
	{
		String role = Context.getParameter("role");
		return new Json(XUtil.call("PmsService.roleres", role));
	}

	public String setres()
	{
		String role = Context.getParameter("role");
		String res = Context.getParameter("res");
		XUtil.call("PmsService.setres", role, res);
		XUtil.ok("设置资源ok...");
		return null;
	}
}
