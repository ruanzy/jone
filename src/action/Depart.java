package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.rzy.util.JSONUtil;
import org.rzy.web.RequestUtil;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Ftl;
import org.rzy.web.result.Json;
import org.rzy.web.result.Msg;

public class Depart
{
	public Result list()
	{
		Map<String, String> map = RequestUtil.getParameters();
		return new Json(WebUtil.call("DepartService.find", map));
	}

	public Result add()
	{
		Map<String, String> map = RequestUtil.getParameters();
		WebUtil.call("DepartService.add", map);
		return new Msg("add success");
	}

	public Result save()
	{
		String data = RequestUtil.getParameter("data");
		List<Map<String, Object>> list = JSONUtil.toList(data);
		for (Map<String, Object> map : list)
		{
			WebUtil.call("PmsService.reg", map);
		}
		return new Msg("add success");
	}

	public Result del()
	{
		String ids = RequestUtil.getParameter("ids");
		WebUtil.call("DepartService.del", ids);
		return new Msg("del success");
	}

	public String active()
	{
		String ids = RequestUtil.getParameter("ids");
		WebUtil.call("PmsService.activeuser", ids);
		return null;
	}

	public String cancel()
	{
		String ids = RequestUtil.getParameter("ids");
		WebUtil.call("PmsService.canceluser", ids);
		return null;
	}

	public Result toaddemp()
	{
		String departname = RequestUtil.getParameter("departname");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("departname", departname);
		return new Ftl("setrole.ftl", map);
	}

	public Result allrole()
	{
		return new Json(WebUtil.call("PmsService.allrole"));
	}

	public Result assignedroles()
	{
		String user = RequestUtil.getParameter("user");
		return new Json(WebUtil.call("PmsService.assignedroles", user));
	}

	public Result setrole()
	{
		String user = RequestUtil.getParameter("user");
		String roles = RequestUtil.getParameter("roles");
		WebUtil.call("PmsService.setrole", user, roles);
		return new Msg("设置角色ok...");
	}
}
