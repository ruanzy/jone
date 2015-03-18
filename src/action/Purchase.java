package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rz.util.JSONUtil;
import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Ftl;
import com.rz.web.view.Json;
import com.rz.web.view.Msg;

public class Purchase
{
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("PurchaseService.list", map));
	}

	public View add()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("PmsService.reg", map);
		return new Msg("add success");
	}

	public View save()
	{
		String data = WebUtil.getParameter("data");
		List<Map<String, Object>> list = JSONUtil.toList(data);
		for (Map<String, Object> map : list)
		{
			WebUtil.call("PmsService.reg", map);
		}
		return new Msg("add success");
	}

	@SuppressWarnings("unchecked")
	public View changed()
	{
		String data = WebUtil.getParameter("changed");
		Map<String, Object> map = JSONUtil.toMap(data);
		List<Map<String, Object>> inserted = (List<Map<String, Object>>) map.get("inserted");
		List<Map<String, Object>> deleted = (List<Map<String, Object>>) map.get("deleted");
		List<Map<String, Object>> updated = (List<Map<String, Object>>) map.get("updated");
		for (Map<String, Object> m : inserted)
		{
			WebUtil.call("PmsService.reg", m);
		}
		for (Map<String, Object> m : deleted)
		{
			WebUtil.call("PmsService.moduser", m);
		}
		for (Map<String, Object> m : updated)
		{
			WebUtil.call("PmsService.moduser", m);
		}
		return new Msg("save success");
	}

	public View del()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.deluser", ids);
		return new Msg("del success");
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

	public View tosetrole()
	{
		String user = WebUtil.getParameter("user");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getParameter("user"));
		map.put("roles", WebUtil.call("PmsService.userrole", user));
		return new Ftl("setrole.ftl", map);
	}

	public View allrole()
	{
		return new Json(WebUtil.call("PmsService.allrole"));
	}

	public View assignedroles()
	{
		String user = WebUtil.getParameter("user");
		return new Json(WebUtil.call("PmsService.assignedroles", user));
	}

	public View setrole()
	{
		String user = WebUtil.getParameter("user");
		String roles = WebUtil.getParameter("roles");
		WebUtil.call("PmsService.setrole", user, roles);
		return new Msg("设置角色ok...");
	}
}
