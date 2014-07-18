package action;

import java.util.Map;
import rzy.core.XUtil;

public class Project
{
	public String list()
	{
		Map<String, Object> map = XUtil.getParameterMap();
		Object username = XUtil.getUsername();
		map.put("username", username);
		XUtil.calljson("0202", map);
		return null;
	}

	public String add()
	{
		String creator = XUtil.getUsername();
		Map<String, Object> map = XUtil.getParameterMap();
		map.put("creator", creator);
		XUtil.call("0204", map);
		XUtil.redirect("projectmgr.jsp");
		return null;
	}

	public String del()
	{
		String ids = XUtil.getParameter("ids");
		XUtil.call("0205", ids);
		return null;
	}
}