package action;

import java.util.Map;

import org.rzy.web.Context;
import org.rzy.web.Result;
import org.rzy.web.result.Json;
import org.rzy.web.util.XUtil;

public class Res
{
	public Result list()
	{
		return new Json(XUtil.call("PmsService.getRes"));
	}

	public String add()
	{
		Map<String, String> map = Context.getParameters();
		XUtil.call("PmsService.addres", map);
		XUtil.ok("增加成功");
		return null;
	}

	public String del()
	{
		String id = Context.getParameter("id");
		XUtil.call("PmsService.delres", id);
		XUtil.ok("删除成功");
		return null;
	}

	public Result menubymoudle()
	{
		String pid = Context.getParameter("pid");
		return new Json(XUtil.call("PmsService.menubymoudle", pid));
	}
}
