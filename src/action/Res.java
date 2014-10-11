package action;

import java.util.Map;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Json;

public class Res
{
	public Result list()
	{
		return new Json(WebUtil.call("PmsService.getRes"));
	}

	public String add()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("PmsService.addres", map);
		WebUtil.ok("增加成功");
		return null;
	}

	public String del()
	{
		String id = WebUtil.getParameter("id");
		WebUtil.call("PmsService.delres", id);
		WebUtil.ok("删除成功");
		return null;
	}

	public Result menubymoudle()
	{
		String pid = WebUtil.getParameter("pid");
		return new Json(WebUtil.call("PmsService.menubymoudle", pid));
	}
}
