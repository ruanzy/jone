package action;

import java.util.HashMap;
import java.util.Map;
import org.rzy.mvc.XUtil;

public class Role
{
	public String list()
	{
		Map<String, String> map = XUtil.getParameters();
		XUtil.calljson("PmsService.findrole", map);
		return null;
	}

	public String add()
	{
		String name = XUtil.getParameter("name");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		XUtil.call("PmsService.addrole", map);
		XUtil.ok();
		return null;
	}
	
	public String del()
	{
		String id = XUtil.getParameter("id");
		XUtil.call("PmsService.delroles", id);
		return null;
	}

	public String allres()
	{
		XUtil.calljson("PmsService.getRes");
		return null;
	}

	public String ownres()
	{
		String role = XUtil.getParameter("role");
		XUtil.calljson("PmsService.roleres", role);
		return null;
	}

	public String setres()
	{
		String role = XUtil.getParameter("role");
		String res = XUtil.getParameter("res");
		XUtil.call("PmsService.setres", role, res);
		XUtil.ok("设置资源ok...");
		return null;
	}
}
