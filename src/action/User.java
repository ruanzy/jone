package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rzy.core.Ftl;
import rzy.core.Json;
import rzy.core.Result;
import rzy.core.XUtil;

public class User
{
	public Result list()
	{
		Map<String, Object> map = XUtil.getParameterMap();
		return new Json(XUtil.call("PmsService.finduser", map));
	}

	public String add()
	{
		Map<String, Object> map = XUtil.getParameterMap();
		XUtil.call("PmsService.reg", map);
		XUtil.ok();
		return null;
	}

	public String save()
	{
		String data = XUtil.getParameter("data");
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
		String ids = XUtil.getParameter("ids");
		XUtil.call("PmsService.deluser", ids);
		return null;
	}

	public String active()
	{
		String ids = XUtil.getParameter("ids");
		XUtil.call("PmsService.activeuser", ids);
		return null;
	}

	public String cancel()
	{
		String ids = XUtil.getParameter("ids");
		XUtil.call("PmsService.canceluser", ids);
		return null;
	}

	public Result tosetrole()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", XUtil.getParameter("id"));
		map.put("roles", XUtil.call("PmsService.allrole"));
		return new Ftl("setrole.ftl", map);
	}

	public String allrole()
	{
		XUtil.calljson("PmsService.allrole");
		return null;
	}

	public String assignedroles()
	{
		String user = XUtil.getParameter("id");
		XUtil.calljson("PmsService.assignedroles", user);
		return null;
	}

	public String setrole()
	{
		String user = XUtil.getParameter("user");
		String roles = XUtil.getParameter("roles");
		XUtil.call("PmsService.setrole", user, roles);
		XUtil.ok("设置角色ok...");
		return null;
	}
}
