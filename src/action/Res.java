package action;

import java.util.HashMap;
import java.util.Map;
import rzy.core.XUtil;

public class Res
{
	public String list()
	{
		XUtil.calljson("PmsService.getRes");
		return null;
	}

	public String add()
	{
		String type = XUtil.getParameter("type");
		String url = XUtil.getParameter("url");
		String name = XUtil.getParameter("name");
		String method = XUtil.getParameter("method");
		String pid = XUtil.getParameter("pid");
		String icon = XUtil.getParameter("icon");
		String flag = XUtil.getParameter("flag");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("url", url);
		map.put("name", name);
		map.put("method", method);
		map.put("pid", pid);
		map.put("icon", icon);
		map.put("flag", flag);
		XUtil.call("PmsService.addres", map);
		XUtil.ok("增加成功");
		return null;
	}

	public String del()
	{
		String id = XUtil.getParameter("id");
		XUtil.call("PmsService.delres", id);
		XUtil.ok("删除成功");
		return null;
	}

	public String menubymoudle()
	{
		String pid = XUtil.getParameter("pid");
		XUtil.calljson("PmsService.menubymoudle", pid);
		return null;
	}
}
