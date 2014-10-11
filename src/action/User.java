package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Ftl;
import org.rzy.web.result.Json;

public class User
{
	public Result list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("PmsService.finduser", map));
	}

	public String add()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("PmsService.reg", map);
		WebUtil.ok();
		return null;
	}

	public String save()
	{
		String data = WebUtil.getParameter("data");
		List<Map<String, Object>> list = WebUtil.toList(data);
		for (Map<String, Object> map : list)
		{
			WebUtil.call("PmsService.reg", map);
		}
		WebUtil.ok();
		return null;
	}

	public String del()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.deluser", ids);
		return null;
	}

	public String active()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.activeuser", ids);
		return null;
	}

	public String cancel()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.canceluser", ids);
		return null;
	}

	public Result tosetrole()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getParameter("id"));
		map.put("roles", WebUtil.call("PmsService.allrole"));
		return new Ftl("setrole.ftl", map);
	}

	public Result allrole()
	{
		return new Json(WebUtil.call("PmsService.allrole"));
	}

	public Result assignedroles()
	{
		String user = WebUtil.getParameter("id");
		return new Json(WebUtil.call("PmsService.assignedroles", user));
	}

	public String setrole()
	{
		String user = WebUtil.getParameter("user");
		String roles = WebUtil.getParameter("roles");
		WebUtil.call("PmsService.setrole", user, roles);
		WebUtil.ok("设置角色ok...");
		return null;
	}
}
