package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rz.util.JSONUtil;
import com.rz.util.WebUtil;
import com.rz.web.Json;
import com.rz.web.View;
import com.rz.web.view.Ftl;
import com.rz.web.view.Msg;

public class Depart
{
	public View tree()
	{
		return new Json(WebUtil.call("DepartService.tree"));
	}
	
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("DepartService.find", map));
	}

	public View add()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("DepartService.add", map);
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

	public View del()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("DepartService.del", ids);
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

	public View toaddemp()
	{
		String departname = WebUtil.getParameter("departname");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("departname", departname);
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
