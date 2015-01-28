package action;

import java.util.HashMap;
import java.util.Map;
import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Json;
import com.rz.web.view.Msg;

public class Role
{
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("PmsService.findrole", map));
	}

	public View add()
	{
		String name = WebUtil.getParameter("name");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		WebUtil.call("PmsService.addrole", map);
		return new Msg("add success");
	}

	public String del()
	{
		String id = WebUtil.getParameter("id");
		WebUtil.call("PmsService.delroles", id);
		return null;
	}

	public View allres()
	{
		return new Json(WebUtil.call("PmsService.getRes"));
	}

	public View ownres()
	{
		String role = WebUtil.getParameter("role");
		return new Json(WebUtil.call("PmsService.roleres", role));
	}

	public View setres()
	{
		String role = WebUtil.getParameter("role");
		String res = WebUtil.getParameter("res");
		WebUtil.call("PmsService.setres", role, res);
		return new Msg("设置资源ok...");
	}
}
