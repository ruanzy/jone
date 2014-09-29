package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rzy.web.Context;
import org.rzy.web.Result;
import org.rzy.web.result.Ftl;
import org.rzy.web.result.Json;
import org.rzy.web.util.XUtil;

public class User
{
	public Result list()
	{
		Map<String, String> map = Context.getParameters();
		return new Json(XUtil.call("PmsService.finduser", map));
	}

	public String add()
	{
		Map<String, String> map = Context.getParameters();
		XUtil.call("PmsService.reg", map);
		XUtil.ok();
		return null;
	}

	public String save()
	{
		String data = Context.getParameter("data");
		List<Map<String, Object>> list = XUtil.toList(data);
		for (Map<String, Object> map : list)
		{
			XUtil.call("PmsService.reg", map);
		}
		XUtil.ok();
		return null;
	}

	public String del()
	{
		String ids = Context.getParameter("ids");
		XUtil.call("PmsService.deluser", ids);
		return null;
	}

	public String active()
	{
		String ids = Context.getParameter("ids");
		XUtil.call("PmsService.activeuser", ids);
		return null;
	}

	public String cancel()
	{
		String ids = Context.getParameter("ids");
		XUtil.call("PmsService.canceluser", ids);
		return null;
	}

	public Result tosetrole()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", Context.getParameter("id"));
		map.put("roles", XUtil.call("PmsService.allrole"));
		return new Ftl("setrole.ftl", map);
	}

	public Result allrole()
	{
		return new Json(XUtil.call("PmsService.allrole"));
	}

	public Result assignedroles()
	{
		String user = Context.getParameter("id");
		return new Json(XUtil.call("PmsService.assignedroles", user));
	}

	public String setrole()
	{
		String user = Context.getParameter("user");
		String roles = Context.getParameter("roles");
		XUtil.call("PmsService.setrole", user, roles);
		XUtil.ok("设置角色ok...");
		return null;
	}
}
